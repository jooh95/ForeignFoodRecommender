package com.example.joonoh.forfoodreco.Helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach(Context context){
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage){
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static Context setLocale(Context context, String laguage){
        persist(context, laguage);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return updateResources(context, laguage);
        }
        return updateResourcesLegacy(context, laguage);
    }

    private static void persist(Context context, String language){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        return context.createConfigurationContext(config);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

       Resources resources = context.getResources();

       Configuration config = resources.getConfiguration();
       config.locale = locale;

       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLayoutDirection(locale);
       }
       resources.updateConfiguration(config, resources.getDisplayMetrics());
        return context;
    }


    private static String getPersistedData(Context context, String laguage){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, laguage);
    }

}
