package com.example.joonoh.forfoodreco;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class HelpActivity extends AppCompatActivity {

    Button korean_btn, english_btn, chinse_btn, japanese_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        korean_btn = (Button) findViewById(R.id.korean);
        english_btn = (Button) findViewById(R.id.english);
        chinse_btn = (Button) findViewById(R.id.chinese);
        japanese_btn = (Button) findViewById(R.id.japanese);

        korean_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("language", "ko");
                finish();

            }
        });

        english_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("language", "en");
                finish();
            }
        });

        chinse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("language", "zh");
                finish();
            }
        });

        japanese_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("language", "ja");
                finish();
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);


    }
}
