package com.example.joonoh.forfoodreco;

import android.app.Application;
import android.content.Context;

import com.example.joonoh.forfoodreco.Helper.LocaleHelper;

public class TranslateApplication extends Application{


    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));

    }
}
