package com.example.joonoh.forfoodreco;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import static android.speech.tts.TextToSpeech.ERROR;

import com.example.joonoh.forfoodreco.Helper.LocaleHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Locale;


import io.paperdb.Paper;

public class SlideAdapter2 extends PagerAdapter{

    Context m_context;
    LayoutInflater inflater;

    TextView food_category, food_cook, main_ingr1, main_ingr2, main_ingr3, sub_ingr1, food_name,  food_intro;
    TextView m_ingr, sub_ingr, f_category, f_intro, f_cook;

    String t_language;

    private TextToSpeech tts;

    ImageButton tts_button;

    String key = "AIzaSyD2pVXUddcdEasBJPfLylj91HsFxHaq7p0";

    String category, m_ingr1, m_ingr2,  m_ingr3, s_ingr1, imageURL,  name, t_data;

    String f_name, end_name, start_name;

    Bitmap[] bitmaps, t_bitmaps;

//    String[] categories, categories1, m_ingr1s, m_ingr2s,  m_ingr3s, s_ingr1s, imageURLs,  names, intros;

    ArrayList<String> categories, categories1, m_ingr1s, m_ingr2s, m_ingr3s, s_ingr1s, names, intros;
    ArrayList<byte[]> images;

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


    public SlideAdapter2(Context context, int s, String language){

        int sync = s;

        length = ResultIPC.get().get_length(sync);

        m_context = context;
        t_language = language;

        tts = new TextToSpeech(m_context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    if(t_language.equals("Korea") ){
                        tts.setLanguage(Locale.KOREAN);
                    }
                    else if(t_language.equals("China")){
                        tts.setLanguage(Locale.CHINESE);
                    }
                    else if(t_language.equals("Japan")){
                        tts.setLanguage(Locale.JAPANESE);
                    }
                    else if(t_language.equals("English")){
                        tts.setLanguage(Locale.ENGLISH);
                    }
                    else if(t_language.equals("Vietnam")){
                        tts.setLanguage(Locale.ENGLISH);
                    }
                }
            }
        });
//        this.length = food_length;

        try{

            if(length > 0){

                categories = ResultIPC.get().getCategory(sync);
                categories1 = ResultIPC.get().getCookery(sync);
                m_ingr1s = ResultIPC.get().getM_ingr1(sync);
                m_ingr2s = ResultIPC.get().getM_ingr2(sync);
                m_ingr3s = ResultIPC.get().getM_ingr3(sync);
                s_ingr1s = ResultIPC.get().getS_ingr1(sync);
                names = ResultIPC.get().getName(sync);
                intros = ResultIPC.get().getIntro(sync);
                images = ResultIPC.get().get_image(sync);

                bitmaps = new Bitmap[length];
                for(int i = 0; i < length; i++){
                    Bitmap bmp = BitmapFactory.decodeByteArray(images.get(i), 0, images.get(i).length);
                    bitmaps[i] = bmp;
                }
            }
        }catch (Exception e) {
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

        FloatingActionButton floating_btn = (FloatingActionButton) view.findViewById(R.id.floating_btn);

        food_category = (TextView) view.findViewById(R.id.food_class);
        food_cook = (TextView) view.findViewById(R.id.food_cook);
        main_ingr1 = (TextView) view.findViewById(R.id.main_ingr1);
        main_ingr2 = (TextView) view.findViewById(R.id.main_ingr2);
        main_ingr3 = (TextView) view.findViewById(R.id.main_ingr3);
        sub_ingr1 = (TextView) view.findViewById(R.id.sub_ingr1);
        food_name = (TextView) view.findViewById(R.id.food_name);
        food_intro = (TextView) view.findViewById(R.id.food_intro);

        tts_button = (ImageButton) view.findViewById(R.id.tts_button);

        f_intro = (TextView) view.findViewById(R.id.f_intro);
        f_category = (TextView) view.findViewById(R.id.f_class);
        f_cook = (TextView) view.findViewById(R.id.f_cook);

        m_ingr = (TextView) view.findViewById(R.id.m_ingr);
        sub_ingr = (TextView) view.findViewById(R.id.s_ingr);

        String language = Paper.book().read("language").toString();

        Resources resources = LocaleHelper.setLocale(m_context, language).getResources();

        if(length == 0){
            f_intro.setText("");
            f_category.setText("");
            f_cook.setText("");

            m_ingr.setText("");
            sub_ingr.setText("");
        }
        else{
            f_intro.setText(resources.getString(R.string.food_intro));
            f_category.setText(resources.getString(R.string.food_class));
            f_cook.setText(resources.getString(R.string.food_cook));

            m_ingr.setText(resources.getString(R.string.main_ingr));
            sub_ingr.setText(resources.getString(R.string.sub_ingr));
        }

        food_category.setText(categories.get(position));
        food_cook.setText(categories1.get(position));
        main_ingr1.setText(m_ingr1s.get(position));
        main_ingr2.setText(m_ingr2s.get(position));
        main_ingr3.setText(m_ingr3s.get(position));
        sub_ingr1.setText(s_ingr1s.get(position));
        food_name.setText(names.get(position));
        food_intro.setText(intros.get(position));

        final String name_f = names.get(position);

        tts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("food_name", food_name.getText().toString());
                tts.speak(name_f,TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(m_context, MapsActivity.class);
                resultIntent.putExtra("food_name", food_name.getText());
                m_context.startActivity(resultIntent);
            }
        });

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


