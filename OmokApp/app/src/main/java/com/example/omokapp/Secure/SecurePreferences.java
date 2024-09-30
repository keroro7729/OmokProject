package com.example.omokapp.Secure;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.omokapp.DTO.AccessToken;
import com.example.omokapp.DTO.RefreshToken;
import com.google.gson.Gson;

import javax.crypto.SecretKey;


public class SecurePreferences {
    private static SecurePreferences instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private SecurePreferences(Context context) {
        gson = new Gson();
        try {
            // create master key
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // create EncryptedSharedPreferences
            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "secret_shared_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.e("SecurePreferences Build error: ", e.getMessage());
        }
    }

    public static SecurePreferences getInstance(Context context){
        if(instance == null)
            instance = new SecurePreferences(context);
        return instance;
    }

    public void saveAccessToken(AccessToken token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access-token", gson.toJson(token));
        editor.apply();
    }

    public AccessToken getAccessToken() {
        String data = sharedPreferences.getString("access-token", null);
        if(data == null) return null;
        return gson.fromJson(data, AccessToken.class);
    }

    public void saveRefreshToken(RefreshToken token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refresh-token", gson.toJson(token));
        editor.apply();
    }

    public RefreshToken getRefreshToken(){
        String data = sharedPreferences.getString("refresh-token", null);
        if(data == null) return null;
        return gson.fromJson(data, RefreshToken.class);
    }

    public void saveSecretKey(String keyData){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("secret-key", keyData);
        editor.apply();
    }
    public void saveSecretKey(SecretKey secretKey){
        String keyData = EncryptUtil.secretKeyToString(secretKey);
        saveSecretKey(keyData);
    }

    public SecretKey getSecretKey(){
        String keyData = sharedPreferences.getString("secret-key", null);
        if(keyData == null) return null;
        return EncryptUtil.stringToSecretKey(keyData);
    }

    public void saveString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key, null);
    }
}
