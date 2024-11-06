package com.example.omokapp.Tools;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    private static Storage instance;
    private SharedPreferences preferences;
    private static final String TAG = "Storage";

    private Storage(Context context){
        preferences = context.getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public static synchronized Storage getInstance(Context context){
        if(instance == null)
            instance = new Storage(context);
        return instance;
    }
    public static synchronized Storage getInstance(){
        if(instance == null)
            throw new RuntimeException("Storage not initialized!");
        return instance;
    }

    public String get(String name){
        return preferences.getString(name, null);
    }

    public void set(String name, String data){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, data);
        editor.apply();
    }

    public void remove(String name){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(name);
        editor.apply();
    }
}
