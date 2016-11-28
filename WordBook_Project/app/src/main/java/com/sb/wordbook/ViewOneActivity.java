package com.sb.wordbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sb.data.DataManager;
import com.sb.data.WordData;
import com.sb.utils.Utils;

import java.util.ArrayList;

public class ViewOneActivity extends Activity {

    private ArrayList<WordData> wordDatas;
    private WordData wordData;

    private Bitmap bitmap;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_one);

        wordDatas = DataManager.getInstance().getDatas();

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_view_one);
        relativeLayout.setBackgroundResource(R.drawable.backimage);

        // 전달된 index 확인
        int idx = 0;
        Intent it = getIntent();

        if( it != null ) {
            int receiveIdx = it.getIntExtra("index", -1);
            if( receiveIdx > -1 ) {
                idx = receiveIdx;
            }
        }

        // 화면 초기화
        init(idx);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if( bitmap != null ) {
            bitmap.recycle();
            bitmap = null;
        }

        if( player != null ) {
            if( player.isPlaying() ) {
                player.stop();
            }
            player.release();
        }
    }

    private void init(final int idx) {

        wordData = wordDatas.get(idx);
        final String rootPath = Utils.getRootDir(this);

        Button prevBtn = (Button)findViewById(R.id.btn_prev_word);
        Button nextBtn = (Button)findViewById(R.id.btn_next_word);

        prevBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);

        // 이미지 초기화
        ImageView iv = (ImageView)findViewById(R.id.img);
        iv.setImageBitmap(null);

        if( bitmap != null ) {
            bitmap.recycle();
            bitmap = null;
        }

        try {
            String imgPath = rootPath + wordData.getImageFile();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            bitmap = BitmapFactory.decodeFile(imgPath, options);

            iv.setImageBitmap(bitmap);

        } catch(Exception e) {
            e.printStackTrace();
        }

        // 텍스트 초기화
        TextView textView = (TextView)findViewById(R.id.text_word);
        textView.setText(wordData.getText());

        // Player 객체 초기화
        if( player != null ) {
            if( player.isPlaying() ) {
                player.stop();
            }
            player.release();
        }

        try {
            player = new MediaPlayer();
            player.setDataSource(rootPath + wordData.getAudioFile());
            player.prepare();

        } catch(Exception e) {
            e.printStackTrace();
        }

        // 듣기 버튼 초기화
        final Button playBtn = (Button)findViewById(R.id.btn_play_audio);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( player == null ) {
                    return;
                }

                if( player.isPlaying() ) {
                    player.pause();
                }
                player.seekTo(0);
                player.start();
            }
        });

        // 이전 단어 버튼 초기화
        if( idx > 0 ) {
            prevBtn.setVisibility(View.VISIBLE);
            prevBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    init(idx-1);
                }
            });
        }

        // 다음 단어 버튼 초기화
        if( idx < wordDatas.size()-1 ) {
            nextBtn.setVisibility(View.VISIBLE);
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    init(idx+1);
                }
            });
        }
    }
}
