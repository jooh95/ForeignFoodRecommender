package com.example.joonoh.forfoodreco;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joonoh.forfoodreco.Helper.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Resources resources;
    TextView start_country, dest_country;
    String food_name, starting_country, end_country, selected;
    Spinner sp_start;
    ImageView flag_start;
    Spinner sp_end;
    ImageView flag_end;
    ArrayAdapter<String> sp_adapter;
    ImageButton search_btn;
    Context c_context;

    String startCountry = new String("");
    String destinationsCountry = new String("");

    AutoCompleteTextView food_typed;

    String[] countries = {"선택", "한국", "중국", "일본", "베트남", "미국", "프랑스", "이탈리아"}; //서버에서 받아 오겠음.
//    String[] food_list = { "牛丼", "牛肉薄切", "牛肉とねぎの中華まん", "牛肉と長芋の中華炒め", "牛肉とトマトのしょうゆ炒め"}; //자동완성 부분, 서버에서 받아 오겠음.
    String[] food_list;

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.help:
                Intent help = new Intent(this, HelpActivity.class);
                startActivity(help);
                break;

            case R.id.aboutUs:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

//    private void updateView(String lang){
//        Context context =
//        Resources resources = context.getResources();
//    }


    private String[] get_foodList(String selected_country){
        selected = selected_country;
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {

                    String server_url = "https://f2vqdqpw58.execute-api.us-east-2.amazonaws.com/foodbeta2/automake?country=" + selected;

                    URL dbServer = new URL(server_url);

                    Log.d("here", server_url);
                    // Create connection
                    HttpsURLConnection myConnection =  (HttpsURLConnection) dbServer.openConnection();
//                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
//                    myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    Log.d("here", " after connection");

                    if (myConnection.getResponseCode() == 200) {

                        InputStream responseBody = myConnection.getInputStream();

                        String temp = convertStreamToString(responseBody);

                        JSONObject json = new JSONObject(temp);

                        JSONArray jsonArray = json.getJSONArray("Autolist");

                        int count = 0;

                        for(int i = 0; i < jsonArray.length(); i++){

                            count++;
                        }
                        food_list = new String[count];

                        for(int i = 0; i < jsonArray.length(); i++){
                            food_list[i] = jsonArray.getString(i).toString();
                            Log.d("list", food_list[i]);
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

        return food_list;
    }

    @Override
    public void onResume(){
        super.onResume();

        Paper.init(this);

        String language;

        try{
            language = Paper.book().read("language").toString();
        }
        catch(Exception e){
            language = null;
        }

        if(language == null){
            Paper.book().write("language", "en");
        }
        else{
            Context context = LocaleHelper.setLocale(this, language);
            resources = context.getResources();
            start_country.setText(resources.getString(R.string.start_country));
            dest_country.setText(resources.getString(R.string.dest_country));
            food_typed.setHint(resources.getString(R.string.food_hint));


            countries[0] = resources.getString(R.string.select);
            countries[1] = resources.getString(R.string.china);
            countries[2] = resources.getString(R.string.korea);
            countries[3] = resources.getString(R.string.america);
            countries[4] = resources.getString(R.string.japan);
            countries[5] = resources.getString(R.string.vietnam);
            countries[6] = resources.getString(R.string.france);
            countries[7] = resources.getString(R.string.italy);

            sp_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, countries);

            sp_start.setAdapter(sp_adapter);
            sp_end.setAdapter(sp_adapter);

            c_context = this;

            sp_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!destinationsCountry.equals("Korea") && sp_start.getItemAtPosition(position).equals(resources.getString(R.string.korea))){
                        flag_start.setImageResource(R.drawable.korea);
                        startCountry = "Korea";
                        food_typed.setAdapter(new ArrayAdapter<String>(c_context,
                                android.R.layout.simple_dropdown_item_1line, get_foodList(startCountry)));
                    }
                    else if(!destinationsCountry.equals("China") && sp_start.getItemAtPosition(position).equals(resources.getString(R.string.china))){
                        flag_start.setImageResource(R.drawable.china);
                        startCountry = "China";
                        food_typed.setAdapter(new ArrayAdapter<String>(c_context,
                                android.R.layout.simple_dropdown_item_1line, get_foodList(startCountry)));
                    }
                    else if(!destinationsCountry.equals("Japan") && sp_start.getItemAtPosition(position).equals(resources.getString(R.string.japan))){
                        flag_start.setImageResource(R.drawable.japan);
                        startCountry = "Japan";
                        food_typed.setAdapter(new ArrayAdapter<String>(c_context,
                                android.R.layout.simple_dropdown_item_1line, get_foodList(startCountry)));
                    }
                    else if(!destinationsCountry.equals("Vietnam") && sp_start.getItemAtPosition(position).equals(resources.getString(R.string.vietnam))){
                        flag_start.setImageResource(R.drawable.vietnam);
                        startCountry = "Vietnam";
                        food_typed.setAdapter(new ArrayAdapter<String>(c_context,
                                android.R.layout.simple_dropdown_item_1line, get_foodList(startCountry)));
                    }
                    else if(sp_end.getItemAtPosition(position).equals(resources.getString(R.string.select))){
                        flag_start.setImageResource(0);
                        startCountry = "";
                    }
                    else{
                        sp_start.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sp_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!startCountry.equals("Korea") && sp_end.getItemAtPosition(position).equals(resources.getString(R.string.korea))){
                        destinationsCountry = "Korea";
                        flag_end.setImageResource(R.drawable.korea);
                    }
                    else if(!startCountry.equals("China") && sp_end.getItemAtPosition(position).equals(resources.getString(R.string.china))){
                        destinationsCountry = "China";
                        flag_end.setImageResource(R.drawable.china);
                    }
                    else if(!startCountry.equals("Japan") && sp_end.getItemAtPosition(position).equals(resources.getString(R.string.japan))){
                        destinationsCountry = "Japan";
                        flag_end.setImageResource(R.drawable.japan);
                    }
                    else if(!startCountry.equals("Vietnam") && sp_end.getItemAtPosition(position).equals(resources.getString(R.string.vietnam))){
                        flag_end.setImageResource(R.drawable.vietnam);
                        destinationsCountry = "Vietnam";
                    }
                    else if(sp_end.getItemAtPosition(position).equals(resources.getString(R.string.select))){
                        destinationsCountry = "";
                        flag_end.setImageResource(0);
                    }
                    else{
                        sp_end.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        start_country = (TextView) findViewById(R.id.start_country);
        dest_country = (TextView) findViewById(R.id.dest_country);


        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        search_btn = (ImageButton)findViewById(R.id.search_btn);

        sp_start = (Spinner)findViewById(R.id.sp_start);
        flag_start = (ImageView)findViewById(R.id.flag_start);
        sp_end = (Spinner)findViewById(R.id.sp_end);
        flag_end = (ImageView)findViewById(R.id.flag_end);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            ListPopupWindow window1 = (ListPopupWindow)popup.get(sp_start);
            ListPopupWindow window2 = (ListPopupWindow)popup.get(sp_end);

            window1.setHeight(700); //pixel
            window2.setHeight(700);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sp_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,countries);



        sp_start.setAdapter(sp_adapter);
        sp_end.setAdapter(sp_adapter);


        food_typed = (AutoCompleteTextView) findViewById(R.id.food_typed);





//        food_typed.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, food_list));

//        sp_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(!destinationsCountry.equals("한국") && sp_start.getItemAtPosition(position).equals("한국")){
//                    flag_start.setImageResource(R.drawable.korea);
//                    startCountry = "한국";
//                }
//                else if(!destinationsCountry.equals("중국") && sp_start.getItemAtPosition(position).equals("중국")){
//                    flag_start.setImageResource(R.drawable.china);
//                    startCountry = "중국";
//                }
//                else if(!destinationsCountry.equals("일본") && sp_start.getItemAtPosition(position).equals("일본")){
//                    flag_start.setImageResource(R.drawable.japan);
//                    startCountry = "일본";
//                }
//                else if(!destinationsCountry.equals("베트남") && sp_start.getItemAtPosition(position).equals("베트남")){
//                    flag_start.setImageResource(R.drawable.vietnam);
//                    startCountry = "베트남";
//                }
//                else if(sp_end.getItemAtPosition(position).equals("선택")){
//                    flag_start.setImageResource(0);
//                    startCountry = "";
//                }
//                else{
//                    sp_start.setSelection(0);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        sp_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(!startCountry.equals("한국") && sp_end.getItemAtPosition(position).equals("한국")){
//                    destinationsCountry = "Korea";
//                    flag_end.setImageResource(R.drawable.korea);
//                }
//                else if(!startCountry.equals("중국") && sp_end.getItemAtPosition(position).equals("중국")){
//                    destinationsCountry = "China";
//                    flag_end.setImageResource(R.drawable.china);
//                }
//                else if(!startCountry.equals("일본") && sp_end.getItemAtPosition(position).equals("일본")){
//                    destinationsCountry = "Japan";
//                    flag_end.setImageResource(R.drawable.japan);
//                }
//                else if(!startCountry.equals("베트남") && sp_end.getItemAtPosition(position).equals("베트남")){
//                    flag_end.setImageResource(R.drawable.vietnam);
//                    destinationsCountry = "Vietnam";
//                }
//                else if(sp_end.getItemAtPosition(position).equals("선택")){
//                    destinationsCountry = "";
//                    flag_end.setImageResource(0);
//                }
//                else{
//                    sp_end.setSelection(0);
//                }
//            }

//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

            search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!startCountry.isEmpty() && !destinationsCountry.isEmpty() && !food_typed.getText().toString().isEmpty()) {

                        food_name = new String(food_typed.getText().toString());
                        end_country = new String(destinationsCountry);
                        starting_country = new String(startCountry);

                        Log.d("F", food_name);
                        Log.d("C", end_country);

                        Intent resultIntent = new Intent(MainActivity.this, LoadingActivity.class);
                        //Intent resultIntent = new Intent(MainActivity.this, MapsActivity.class);
                        resultIntent.putExtra("food_name", food_name);
                        resultIntent.putExtra("start_country", starting_country);
                        resultIntent.putExtra("end_country", end_country);
                        startActivity(resultIntent);
                    }

                }
            });





    }
}
