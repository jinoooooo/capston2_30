package com.sb.wordbook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sb.data.DataManager;
import com.sb.data.WordData;
import com.sb.utils.Utils;

import java.io.File;

public class WordAddActivity extends Activity {

    private static final int AC_RC_IMAGE_CAPTURE = 100;
    private WordData wordData;
    private Bitmap captureImgBitmap;
    private MediaRecorder recorder;

    private static final int STEP_NONE                         = 0;
    private static final int STEP_IMAGE_CAPTURE                = 1;
    private static final int STEP_INPUT_WORD                   = 2;
    private static final int STEP_AUDIO_RECORD                 = 3;
    private static final int STEP_FINISHED                     = 4;
    private int step = STEP_NONE;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_add);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_word_add);
        relativeLayout.setBackgroundResource(R.drawable.backimage);

        wordData = new WordData();
        wordData.setDate(Utils.getCurrentTimeString());

        handler = new Handler();
    }

    @Override
    protected void onResume() {

        super.onResume();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 최초 한번 사진 촬영 시작
                if( step == STEP_NONE ) {
                    onStartImageCapture();
                }
            }
        }, 50);
    }

    @Override
    protected void onDestroy() {

        if( recorder != null ) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }

        super.onDestroy();

        if( captureImgBitmap != null ) {
            captureImgBitmap.recycle();
        }
    }

    /**
     * 사진 촬영
     */
    private void onStartImageCapture() {

        step = STEP_IMAGE_CAPTURE;

        File file = new File(Utils.getRootDir(getApplicationContext()) + wordData.getImageFile());

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(it, AC_RC_IMAGE_CAPTURE);
    }

    /**
     * 키보드 입력
     */
    private void onStartInputWord() {

        step = STEP_INPUT_WORD;

        // 입력창 출력
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.alert_msg_input_word_guide);
        alert.setCancelable(false);

        // EditText 생성
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setTextColor(Color.BLACK);
        editText.setCursorVisible(false);
        alert.setView(editText);

        alert.setPositiveButton(R.string.alert_title_positive, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                String text = editText.getText().toString();
                wordData.setText(text);

                // 텍스트 뷰 갱신
                TextView wordTextView = (TextView)findViewById(R.id.text_word);
                wordTextView.setText(text);

                // 녹음 시작 알림창
                new AlertDialog.Builder(WordAddActivity.this)
                .setMessage(R.string.alert_msg_audio_record_guide)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_title_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 녹음 시작
                        onStartAudioRecord();
                    }
                })
                .show();
            }
        });
        alert.show();
    }

    /**
     * 녹음
     */
    private void onStartAudioRecord() {

        step = STEP_AUDIO_RECORD;
        final Activity activity = this;

        try {
            // 레코더 생성
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(Utils.getRootDir(this) + wordData.getAudioFile());
            recorder.prepare();
            recorder.start();

            // 정지 버튼 활성화
            final Button stopBtn = (Button)findViewById(R.id.btn_record_stop);
            stopBtn.setVisibility(View.VISIBLE);
            stopBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if( recorder == null ) {
                        return;
                    }

                    step = STEP_FINISHED;

                    // 레코더 해제
                    recorder.stop();
                    recorder.release();
                    recorder = null;

                    // 정비 버튼 비활성화
                    stopBtn.setVisibility(View.GONE);
                    stopBtn.setEnabled(false);
                    stopBtn.invalidate();

                    // db에 등록
                    DataManager.getInstance().addWord(getApplicationContext(), wordData);

                    // 화면 종료
                    activity.finish();
                }
            });

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch( requestCode ) {
            case AC_RC_IMAGE_CAPTURE:
                if( resultCode != RESULT_OK ) {
                    // 카메라 촬영 취소 시 이전 화면으로 이동
                    finish();
                    break;
                }

                final String filePath = Utils.getRootDir(getApplicationContext()) + wordData.getImageFile();

                // 테스트를 위한 스캐닝
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));

                // 이미지 설정
                try {
                    if( captureImgBitmap != null ) {
                        captureImgBitmap.recycle();
                    }

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    captureImgBitmap = BitmapFactory.decodeFile(filePath, options);

                    ImageView iv = (ImageView)findViewById(R.id.img);
                    iv.setImageBitmap(captureImgBitmap);

                } catch(Exception e) {
                    e.printStackTrace();
                }

                // 단어 입력
                onStartInputWord();

                break;
        }
    }
}