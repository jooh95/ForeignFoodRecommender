package com.example.joonoh.forfoodreco;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import io.paperdb.Paper;


public class LoadingActivity extends Activity {

    static String[] t_categories, t_categories1, t_m_ingr1s, t_m_ingr2s,  t_m_ingr3s, t_s_ingr1s, t_imageURLs,  t_names, t_intros;
    static Bitmap[] t_bitmaps;

    String t_data;

    String key = "AIzaSyD2pVXUddcdEasBJPfLylj91HsFxHaq7p0";

    String kakao_key = "KakaoAK 8ee4cb57a2fefea0816aab7090190b44";

    byte[] byteArray;

    ArrayList<byte[]> byte_images = new ArrayList<byte[]>();

    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new backTask().execute();

    }

    private class backTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            getData();
            return null;
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is), 4096);
        String line;
        StringBuilder sb =  new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String contentOfMyInputStream = sb.toString();
        return contentOfMyInputStream;
    }

    private String translate(String f_data){
        t_data = f_data;

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    String t_text = URLEncoder.encode(t_data, "UTF-8");

                    String language = Paper.book().read("language");

                    if(language.equals("ko")){
                        String urlStr = "https://kapi.kakao.com/v1/translation/translate?src_lang=en&target_lang=kr&query=" + t_text;

                        Log.d("url", urlStr);

                        URL url = new URL(urlStr);

                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                            conn.setRequestProperty("Authorization", kakao_key);

                        if (conn.getResponseCode() == 200) {

                            InputStream responseBody = conn.getInputStream();

                            String temp = convertStreamToString(responseBody);

                            JSONObject json = new JSONObject(temp);

                            t_data = json.getString("translated_text");

                            t_data = t_data.replaceAll("\\[", "");
                            t_data = t_data.replaceAll("\\]", "");
                            t_data = t_data.replaceAll("\"", "");

                            Log.d("aaaaaaaa", temp);

//                            JSONObject d_json = json.getJSONObject("translatedText");
//
//                            JSONArray jsonArray = d_json.getJSONArray("translations");
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject t_json = jsonArray.getJSONObject(i);
//                                Log.d("llllllllll", t_json.getString("translatedText"));
//                                t_data = t_json.getString("translatedText");
//                            }


                        }

                    }
                    else{
                        String urlStr = "https://www.googleapis.com/language/translate/v2?key=" + key + "&q=" + t_text + "&target=" + language + "&source=en";

                        Log.d("url", urlStr);

                        URL url = new URL(urlStr);

                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                        if (conn.getResponseCode() == 200) {

                            InputStream responseBody = conn.getInputStream();

                            String temp = convertStreamToString(responseBody);

                            JSONObject json = new JSONObject(temp);

                            JSONObject d_json = json.getJSONObject("data");

                            JSONArray jsonArray = d_json.getJSONArray("translations");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject t_json = jsonArray.getJSONObject(i);
                                Log.d("llllllllll", t_json.getString("translatedText"));
                                t_data = t_json.getString("translatedText");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try{
            mThread.join();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return t_data;
    }


//    private String translate2(String f_data){
//        t_data = f_data;
//
//        Thread mThread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    String t_text = URLEncoder.encode(t_data, "UTF-8");
//
//                    String urlStr = "https://www.googleapis.com/language/translate/v2?key=" + key + "&q=" + t_text + "&target=" + Paper.book().read("language") + "&source=en";
//
//                    Log.d("url", urlStr);
//
//                    URL url = new URL(urlStr);
//
//                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//
//                    if (conn.getResponseCode() == 200) {
//
//                        InputStream responseBody = conn.getInputStream();
//
//                        String temp = convertStreamToString(responseBody);
//
//                        JSONObject json = new JSONObject(temp);
//
//                        JSONObject d_json = json.getJSONObject("data");
//
//                        JSONArray jsonArray = d_json.getJSONArray("translations");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject t_json = jsonArray.getJSONObject(i);
//                            Log.d("llllllllll", t_json.getString("translatedText"));
//                            t_data = t_json.getString("translatedText");
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        mThread.start();
//
//        try{
//            mThread.join();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return t_data;
//    }

    private void getData(){

        Bundle intent_data = getIntent().getExtras();
        String f_name = intent_data.getString("food_name");
        String end_name = intent_data.getString("end_country");
        String start_name = intent_data.getString("start_country");

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {

                    Bundle intent_data = getIntent().getExtras();
                    String f_name = intent_data.getString("food_name");
                    String end_name = intent_data.getString("end_country");
                    String start_name = intent_data.getString("start_country");

                    Log.d("fname", f_name);
                    Log.d("cname", start_name);
                    Log.d("c1name", end_name );

                    String server_url = "https://f2vqdqpw58.execute-api.us-east-2.amazonaws.com/foodbeta2//food-recommendation?name=" + f_name +  "&country1=" + start_name + "&country=" + end_name;

                    URL dbServer = new URL(server_url);

                    Log.d("url", server_url);
                    // Create connection
                    HttpsURLConnection myConnection =  (HttpsURLConnection) dbServer.openConnection();
//                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
//                    myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    Log.d("here", " after connection");

                    if (myConnection.getResponseCode() == 200) {

                        InputStream responseBody = myConnection.getInputStream();

                        String temp = convertStreamToString(responseBody);

                        JSONObject json = new JSONObject(temp);

                        length = json.getInt("length");

                        Log.d("length", Integer.toString(length));
                        if(length > 0){
                            t_bitmaps = new Bitmap[length];
                            t_categories = new String[length];
                            t_categories1 = new String[length];
                            t_m_ingr1s = new String[length];
                            t_m_ingr2s = new String[length];
                            t_m_ingr3s = new String[length];
                            t_s_ingr1s = new String[length];
                            t_imageURLs = new String[length];
                            t_names = new String[length];
                            t_intros = new String[length];
                            byteArray = new byte[length];


                            JSONArray jsonArray = json.getJSONArray("Food");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_data = jsonArray.getJSONObject(i);

                                t_categories[i] = json_data.getString("Category");
                                t_categories1[i] = json_data.getString("Category2");
                                //food_class.setText(value);

                                t_m_ingr1s[i] = json_data.getString("Main Ingredient1");
                                t_m_ingr2s[i] = json_data.getString("Main Ingredient2");
                                t_m_ingr3s[i] = json_data.getString("Main Ingredient3");
                                t_s_ingr1s[i] = json_data.getString("Sub Ingredient1");

                                t_names[i] = json_data.getString("Name");
                                t_intros[i] = json_data.getString("detail");
                                // Fetch the value as a String
                                String imageURL = json_data.getString("ImageURL");

                                Log.d("image", imageURL);

                                URL url = new URL(imageURL);

                                // Web에서 이미지를 가져온 뒤
                                // ImageView에 지정할 Bitmap을 만든다
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true); // 서버로 부터 응답 수신
                                conn.connect();

                                InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                                t_bitmaps[i] = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                t_bitmaps[i].compress(Bitmap.CompressFormat.PNG, 100, stream);
                                //byteArray[i] = stream.toByteArray();
                                byte_images.add(stream.toByteArray());
                            }

                        }
                        else{
                            t_categories = new String[1];
                            t_m_ingr1s = new String[1];
                            t_m_ingr2s = new String[1];
                            t_m_ingr3s = new String[1];
                            t_s_ingr1s = new String[1];
                            t_imageURLs = new String[1];
                            t_names = new String[1];
                            t_intros = new String[1];
                            t_bitmaps = new Bitmap[1];
                            t_bitmaps[0] = BitmapFactory.decodeResource(getResources(),R.drawable.no_food_big);

                            t_categories[0] = new String();
                            t_m_ingr1s[0] =  new String();
                            t_m_ingr2s[0] = new String();
                            t_m_ingr3s[0] =  new String();
                            t_s_ingr1s[0] =  new String();
                            t_imageURLs[0] =  new String();
                            t_names[0] =  new String("No Result Found");
                            t_intros[0] =  new String();
                            Log.d("111", "1111");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try {
            mThread.join();

            if(length == 0){
                startActivity(new Intent(LoadingActivity.this, NoResultActivity.class));
                finish();
            }
            else{
                Intent it = new Intent(LoadingActivity.this, ResultFragment2.class);

                Log.d("send", "send");
                it.putExtra("length", length);
                it.putExtra("language", end_name);

                for(int i = 0; i < length; i++){
                    t_categories[i] = translate(t_categories[i]);
                    t_categories1[i] = translate(t_categories1[i]);
                    t_m_ingr1s[i] = translate(t_m_ingr1s[i]);
                    t_m_ingr2s[i] = translate(t_m_ingr2s[i]);
                    t_m_ingr3s[i] = translate(t_m_ingr3s[i]);
                    t_s_ingr1s[i] = translate(t_s_ingr1s[i]);
                    t_names[i] = t_names[i];
                    t_intros[i] = translate(t_intros[i]);
                }

                Log.d("ccccccccc", t_categories[0]);

                int sync = ResultIPC.get().setData(length, t_categories, t_categories1, t_m_ingr1s, t_m_ingr2s, t_m_ingr3s, t_s_ingr1s, t_names, t_intros, byte_images);
                it.putExtra("sync", sync);

                startActivity(it);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
