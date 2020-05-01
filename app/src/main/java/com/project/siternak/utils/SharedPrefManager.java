package com.project.siternak.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.siternak.models.auth.AccessToken;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "SiternakSharedPref";
    private static final String TAG_TOKEN = "token";
    private static final String IS_LOGGED_IN = "isLoggedIn";


    private static SharedPrefManager mInstance ;
    private Context mContext ;


    private SharedPrefManager(Context mContext){
        this.mContext = mContext;
    }

    public static synchronized SharedPrefManager getInstance(Context mContext){
        if(mInstance == null){
            mInstance = new SharedPrefManager(mContext);
        }
        return mInstance;
    }

    public void saveAccessToken(AccessToken token){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TAG_TOKEN, token.getToken());
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.apply();
    }

    public AccessToken getAccessToken(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new AccessToken(
                sharedPreferences.getString(TAG_TOKEN,null)
        );
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    public void logout(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }
}
