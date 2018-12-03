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

import io.paperdb.Paper;

public class ResultActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_result);



//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        slideAdapter = new SlideAdapter(this);
//        viewPager.setAdapter(slideAdapter);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        food_category = (TextView) findViewById(R.id.food_class);
        main_ingr1 = (TextView) findViewById(R.id.main_ingr1);
        main_ingr2 = (TextView) findViewById(R.id.main_ingr2);
        main_ingr3 = (TextView) findViewById(R.id.main_ingr3);
        sub_ingr1 = (TextView) findViewById(R.id.sub_ingr1);
        food_image = (ImageView) findViewById(R.id.foodImage);
        food_name = (TextView) findViewById(R.id.food_name);
        food_intro = (TextView) findViewById(R.id.food_intro);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    Bundle intent_data = getIntent().getExtras();
                    String d_fname = intent_data.getString("food_name");
                    String d_cname = intent_data.getString("country_name");

                    Log.d("fname", d_fname);
                    Log.d("cname", d_cname);

                    String food_name = URLEncoder.encode(d_fname, "UTF-8");
                    String server_url = "https://f2vqdqpw58.execute-api.us-east-2.amazonaws.com/foodbeta2//food-recommendation/" + food_name + "/" + d_cname;

                    URL dbServer = new URL(server_url);

                    Log.d("here", server_url);
                    // Create connection
                    HttpsURLConnection myConnection =  (HttpsURLConnection) dbServer.openConnection();
//                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
//                    myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    Log.d("here", " after connection");

                    if (myConnection.getResponseCode() == 200) {
                        Log.d("here2", "2");
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                        JsonReader jsonReader = new JsonReader(responseBodyReader);

                        jsonReader.beginObject(); // Start processing the JSON object
                        while (jsonReader.hasNext()) { // Loop through all keys
                            String key = jsonReader.nextName(); // Fetch the next key
                            Log.d("key", key);
                            if (key.equals("CategoryID")) { // Check if desired key
                                // Fetch the value as a String
                                category = jsonReader.nextString();

                                //food_class.setText(value);
                                Log.d("Category", category);

                            }
                            else if (key.equals("Main Ingredient1")) { // Check if desired key
                                // Fetch the value as a String
                                m_ingr1 = jsonReader.nextString();


                            }
                            else if (key.equals("Main Ingredient2")) { // Check if desired key
                                // Fetch the value as a String
                                m_ingr2 = jsonReader.nextString();
                            }
                            else if (key.equals("Main Ingredient3")) { // Check if desired key
                                // Fetch the value as a String
                                m_ingr3 = jsonReader.nextString();
                            }
                            else if (key.equals("Sub Ingredient1")) { // Check if desired key
                                // Fetch the value as a String
                                s_ingr1 = jsonReader.nextString();
                            }
                            else if (key.equals("Name")) { // Check if desired key
                                // Fetch the value as a String
                                name = jsonReader.nextString();
                            }
                            else if (key.equals("detail")) { // Check if desired key
                                // Fetch the value as a String
                                intro = jsonReader.nextString();
                            }
                            else if (key.equals("ImageURL")) { // Check if desired key
                                // Fetch the value as a String
                                imageURL = jsonReader.nextString();

                                Log.d("image", imageURL);

                                URL url = new URL(imageURL);

                                // Web에서 이미지를 가져온 뒤
                                // ImageView에 지정할 Bitmap을 만든다
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true); // 서버로 부터 응답 수신
                                conn.connect();

                                InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                                bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                            }
                            else {
                                jsonReader.skipValue(); // Skip values of other keys
                            }
                        }

                        jsonReader.close();

                    } else {
                        // Error handling code goes here
                        Log.d("here3", "3");
                    }

                    myConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try {
            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
            // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
            mThread.join();

            food_category.setText(category);
            main_ingr1.setText(m_ingr1);
            main_ingr2.setText(m_ingr2);
            main_ingr3.setText(m_ingr3);
            sub_ingr1.setText(s_ingr1);
            food_name.setText(name);
            food_image.setImageBitmap(bitmap);
            food_intro.setText(intro);

            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
            //foodImage.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

    }



}
