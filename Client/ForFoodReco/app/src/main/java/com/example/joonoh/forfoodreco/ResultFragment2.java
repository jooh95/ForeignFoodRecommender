package com.example.joonoh.forfoodreco;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import io.paperdb.Paper;

import static android.speech.tts.TextToSpeech.ERROR;


public class ResultFragment2 extends AppCompatActivity {
    private ViewPager viewPager;
    private SlideAdapter2 slideAdapter;

    private TextToSpeech tts;

    String language;
    ImageButton tts_button;
    TextView food_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result);

        int sync = getIntent().getIntExtra("sync", -1);
        language = getIntent().getStringExtra("language");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        slideAdapter = new SlideAdapter2(this, sync, language);
        viewPager.setAdapter(slideAdapter);

//        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != ERROR) {
//
//                    if(language.equals("ko") ){
//                        tts.setLanguage(Locale.KOREAN);
//                    }
//                    else if(language.equals("zh")){
//                        tts.setLanguage(Locale.CHINESE);
//                    }
//                    else if(language.equals("ja")){
//                        tts.setLanguage(Locale.JAPANESE);
//                    }
//                    else if(language.equals("en")){
//                        tts.setLanguage(Locale.ENGLISH);
//                    }
//
//                }
//            }
//        });

//        tts_button = (ImageButton) findViewById(R.id.tts_button);
//        food_name = (TextView) findViewById(R.id.food_name);
//
//        tts_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tts.speak(food_name.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
//            }
//        });


    }



}
