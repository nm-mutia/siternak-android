package com.project.siternak.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {

    public static boolean isNetworkAvailable(Context context) {
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = null;

            if(connectivityManager != null){
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }

            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch (NullPointerException e){
            return false;
        }
    }

}
