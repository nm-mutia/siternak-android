package com.project.siternak.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.siternak.models.auth.UserModel;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "SiternakSharedPref";
    private static final String TAG_TOKEN = "token";
    private static final String TAG_UID = "uid";
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

    public void saveAccessToken(String token){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TAG_TOKEN, token);
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getAccessToken(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN,null);
    }

    public void saveUid(String uid){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TAG_UID, uid);
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getUid(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_UID,null);
    }

    public void saveUser(UserModel user){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("username", user.getUsername());
        editor.putString("role", user.getRole());
        editor.putString("email", user.getEmail());

        editor.apply();
    }

    public UserModel getUser(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserModel(
                sharedPreferences.getInt("id",-1),
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("username",null),
                sharedPreferences.getString("role",null),
                sharedPreferences.getString("email",null)
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
