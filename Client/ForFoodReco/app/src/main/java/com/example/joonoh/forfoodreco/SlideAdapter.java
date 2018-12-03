package com.example.joonoh.forfoodreco;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joonoh.forfoodreco.Helper.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import io.paperdb.Paper;

public class SlideAdapter extends PagerAdapter{

    Context m_context;
    LayoutInflater inflater;

    TextView food_category, food_cook, main_ingr1, main_ingr2, main_ingr3, sub_ingr1, food_name,  food_intro;
    TextView m_ingr, sub_ingr, f_category, f_intro, f_cook;

    String key = "AIzaSyD2pVXUddcdEasBJPfLylj91HsFxHaq7p0";

    ImageButton tts_button;

    String category, m_ingr1, m_ingr2,  m_ingr3, s_ingr1, imageURL,  name, t_data;

    String f_name, end_name, start_name;

    Bitmap[] bitmaps, t_bitmaps;

    String[] categories, categories1, m_ingr1s, m_ingr2s,  m_ingr3s, s_ingr1s, imageURLs,  names, intros;
    String[] t_categories, t_categories1, t_m_ingr1s, t_m_ingr2s,  t_m_ingr3s, t_s_ingr1s, t_imageURLs,  t_names, t_intros;

    int length = 0;


    public int[] lst_images = {
        R.drawable.gyudong,
        R.drawable.buta,
        R.drawable.oggo,
    };

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

                    String urlStr = "https://www.googleapis.com/language/translate/v2?key=" + key + "&q=" + t_text + "&target=" + Paper.book().read("language") + "&source=en";

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

    public SlideAdapter(Context context, String d_fname, String d_cname, String s_cname){

        m_context = context;
        this.f_name = d_fname;
        this.end_name = d_cname;
        this.start_name = s_cname;

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {

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
                                imageURL = json_data.getString("ImageURL");

                                Log.d("image", imageURL);

                                URL url = new URL(imageURL);

                                // Web에서 이미지를 가져온 뒤
                                // ImageView에 지정할 Bitmap을 만든다
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true); // 서버로 부터 응답 수신
                                conn.connect();

                                InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                                t_bitmaps[i] = BitmapFactory.decodeStream(is); // Bitmap으로 변환
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
                            t_bitmaps[0] = BitmapFactory.decodeResource(m_context.getResources(),R.drawable.no_food_big);

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

        Thread loading = new Thread() {
            @Override
            public void run() {

            }
        };

        mThread.start();
        loading.start();

        try{
            mThread.join();

            if(length > 0){
                bitmaps = new Bitmap[length];
                categories = new String[length];
                categories1 = new String[length];
                m_ingr1s = new String[length];
                m_ingr2s = new String[length];
                m_ingr3s = new String[length];
                s_ingr1s = new String[length];
                imageURLs = new String[length];
                names = new String[length];
                intros = new String[length];

                for(int i = 0; i < length; i++){
                    bitmaps[i] = t_bitmaps[i];
                    categories[i] = translate(t_categories[i]);
                    categories1[i] = translate(t_categories1[i]);
                    m_ingr1s[i] = translate(t_m_ingr1s[i]);
                    m_ingr2s[i] = translate(t_m_ingr2s[i]);
                    m_ingr3s[i] = translate(t_m_ingr3s[i]);
                    s_ingr1s[i] = translate(t_s_ingr1s[i]);
                    imageURLs[i] = t_imageURLs[i];
                    names[i] = t_names[i];
                    intros[i] = translate(t_intros[i]);
                }
            }
            else{
                categories = new String[1];
                categories1 = new String[1];
                m_ingr1s = new String[1];
                m_ingr2s = new String[1];
                m_ingr3s = new String[1];
                s_ingr1s = new String[1];
                imageURLs = new String[1];
                names = new String[1];
                intros = new String[1];
                bitmaps = new Bitmap[1];

                bitmaps[0] = t_bitmaps[0];
                names[0] = translate(t_names[0]);

                Log.d("2222", "2222");
            }

        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {

        if(length == 0){
            return length + 1;
        }
        return length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view==(LinearLayout)o);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) m_context.getSystemService(m_context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_result, container, false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slide_result);


        ImageView imgslides = (ImageView) view.findViewById(R.id.foodImage);
        imgslides.setImageBitmap(bitmaps[position]);

        Log.d("aaa2", "image");

        tts_button = (ImageButton) view.findViewById(R.id.tts_button);

        food_category = (TextView) view.findViewById(R.id.food_class);
        food_cook = (TextView) view.findViewById(R.id.food_cook);
        main_ingr1 = (TextView) view.findViewById(R.id.main_ingr1);
        main_ingr2 = (TextView) view.findViewById(R.id.main_ingr2);
        main_ingr3 = (TextView) view.findViewById(R.id.main_ingr3);
        sub_ingr1 = (TextView) view.findViewById(R.id.sub_ingr1);
        food_name = (TextView) view.findViewById(R.id.food_name);
        food_intro = (TextView) view.findViewById(R.id.food_intro);

        f_intro = (TextView) view.findViewById(R.id.f_intro);
        f_category = (TextView) view.findViewById(R.id.f_class);
        f_cook = (TextView) view.findViewById(R.id.f_cook);

        m_ingr = (TextView) view.findViewById(R.id.m_ingr);
        sub_ingr = (TextView) view.findViewById(R.id.s_ingr);

        String language = Paper.book().read("language").toString();

        Resources resources = LocaleHelper.setLocale(m_context, language).getResources();
//
//        if(length == 0){
//            f_intro.setText("");
//            f_category.setText("");
//            f_cook.setText("");
//
//            m_ingr.setText("");
//            sub_ingr.setText("");
//        }
//        else{
            f_intro.setText(resources.getString(R.string.food_intro));
            f_category.setText(resources.getString(R.string.food_class));
            f_cook.setText(resources.getString(R.string.food_cook));

            m_ingr.setText(resources.getString(R.string.main_ingr));


        food_category.setText(categories[position]);
        food_cook.setText(categories1[position]);
        main_ingr1.setText(m_ingr1s[position]);
        main_ingr2.setText(m_ingr2s[position]);
        main_ingr3.setText(m_ingr3s[position]);
        sub_ingr1.setText(s_ingr1s[position]);
        food_name.setText(names[position]);
        food_intro.setText(intros[position]);

        container.addView(view);
        return view;
    }

//    public static void showFullSizePhoto(ImageView imageView, Context context) {
//
//        ImageView tempImageView = imageView;
//
//        AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View layout = inflater.inflate(R.layout.image_dialog,
//                (ViewGroup) ((Activity) context).findViewById(R.id.layout_root));
//
//        ImageView image = (ImageView) layout.findViewById(R.id.foodImage);
//        image.setImageDrawable(tempImageView.getDrawable());
//        imageDialog.setView(layout);
//        AlertDialog dialog = imageDialog.create();
//        dialog.show();
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}


