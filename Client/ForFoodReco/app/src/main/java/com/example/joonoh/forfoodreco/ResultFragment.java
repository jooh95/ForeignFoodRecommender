package com.example.joonoh.forfoodreco;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;//javaexpert.tistory.com/485 [올해는 블록체인이다.]

public class ResultFragment extends AppCompatActivity {
    private ViewPager viewPager;
    private SlideAdapter slideAdapter;

    TextView food_category;
    TextView main_ingr1;
    TextView main_ingr2;
    TextView main_ingr3;
    TextView sub_ingr1;
    TextView food_name;
    TextView food_intro;
    ImageView food_image;
    Bitmap bitmap;

    String category;
    String m_ingr1;
    String m_ingr2;
    String m_ingr3;
    String s_ingr1;
    String imageURL;
    String name;
    String intro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result);

        Bundle intent_data = getIntent().getExtras();
        String d_fname = intent_data.getString("food_name");
        String d_cname = intent_data.getString("end_country");
        String s_cname = intent_data.getString("start_country");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        slideAdapter = new SlideAdapter(this, d_fname, d_cname, s_cname);
        viewPager.setAdapter(slideAdapter);


    }



}
