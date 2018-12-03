package com.example.joonoh.forfoodreco;

import android.util.Log;

import java.util.ArrayList;

public class ResultIPC {

    private static ResultIPC instance;

    public synchronized static ResultIPC get() {
        if (instance == null) {
            instance = new ResultIPC ();
        }
        return instance;
    }

    private int sync = 0;
    private ArrayList<String> category, cookery, subcategory, name, intro, m_ingr1, m_ingr2, m_ingr3, s_ingr1;
    private ArrayList<byte[]> byte_images = new ArrayList<byte[]>();
    private int length = 0;

    public int setData(Integer length, String[] category, String[] cookery, String[] m_ingr1, String[] m_ingr2, String[] m_ingr3, String[] s_ingr1, String[] name, String[] intro, ArrayList<byte[]> byte_images) {
        this.length = length;

        this.category = new ArrayList<String>();
        this.cookery = new ArrayList<String>();
        this.m_ingr1 = new ArrayList<String>();
        this.m_ingr2 = new ArrayList<String>();
        this.m_ingr3 = new ArrayList<String>();
        this.s_ingr1 = new ArrayList<String>();

        this.name = new ArrayList<String>();
        this.intro = new ArrayList<String>();

        Log.d("aaaaaaaaaaaa", Integer.toString(length));

        for(int i = 0; i < length; i++){
            this.category.add(category[i]);
            this.cookery.add(cookery[i]);
//            this.subcategory.add(subcategory[i]);
            this.name.add(name[i]);
            this.intro.add(intro[i]);
            this.m_ingr1.add(m_ingr1[i]);
            this.m_ingr2.add(m_ingr2[i]);
            this.m_ingr3.add(m_ingr3[i]);
            this.s_ingr1.add(s_ingr1[i]);
        }
        this.byte_images = byte_images;

        return ++sync;
    }

    public int get_length(int request) {
        return (request == sync) ? length : -1;
    }

    public ArrayList<byte[]> get_image(int request) {
        return (request == sync) ? byte_images : null;
    }

    public ArrayList<String> getS_ingr1(int request) {
        return (request == sync) ? s_ingr1 : null;
    }

    public ArrayList<String> getM_ingr3(int request) {
        return (request == sync) ? m_ingr3 : null;
    }

    public ArrayList<String> getM_ingr2(int request) {
        return (request == sync) ? m_ingr2 : null;
    }

    public ArrayList<String> getM_ingr1(int request) {
        return (request == sync) ? m_ingr1 : null;
    }

    public ArrayList<String> getSubCategory(int request) {
        return (request == sync) ? subcategory : null;
    }

    public ArrayList<String> getIntro(int request) {
        return (request == sync) ? intro : null;
    }

    public ArrayList<String> getCategory(int request) {
        return (request == sync) ? category : null;
    }

    public ArrayList<String> getName(int request) {
        return (request == sync) ? name : null;
    }

    public ArrayList<String> getCookery(int request) {
        return (request == sync) ? cookery : null;
    }
}
