package com.sb.wordbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sb.data.DataManager;
import com.sb.utils.Utils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_main);
        relativeLayout.setBackgroundResource(R.drawable.mainbackimage);

        // 루트 폴더 생성
        {
            File f = new File(Utils.getRootDir(this));
            if( !f.exists() ) {
                f.mkdirs();
            }
        }

        init();
    }

    //액션바 정보 제공
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 0, Menu.NONE,"도움말");
        menu.add(0, 1, Menu.NONE,"개발자 정보");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 0 :
                Toast.makeText(MainActivity.this,"(임시) 슬라이딩 페이지로 도움말 예정", Toast.LENGTH_SHORT).show();
                break;

            case 1 :
                Toast.makeText(MainActivity.this,"개발팀 : 캡스토 30조 아기자기", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {

        DataManager.getInstance().purgeManager();

        super.onDestroy();
    }

    private void init() {

        // 데이터 초기화
        DataManager.getInstance().init(this);

        // 단어 추가하기 버튼
        Button addBtn = (Button)findViewById(R.id.btn_word_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), WordAddActivity.class);
                startActivity(it);
            }
        });

        // 단어 하나씩 보기
        Button viewOneBtn = (Button)findViewById(R.id.btn_word_view_one);
        viewOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), ViewOneActivity.class);
                startActivity(it);
            }
        });

        // 단어장 보기
        Button viewAllBtn = (Button)findViewById(R.id.btn_word_view_all);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), ViewAllActivity.class);
                startActivity(it);
            }
        });
    }

    //  뒤로가기 방지
    private long lastTimeBackPressed;

    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}