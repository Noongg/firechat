package com.example.firebase_app_chat.Splash_Screen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class AppUtil {
    public static boolean checkNetwork(Context context){
     if (context==null){
         return false;
     }
     else {
         ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         if (connectivityManager ==null){
             return false;
         }else {
             if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                 Network network=connectivityManager.getActiveNetwork();
                 if (network==null){
                     return false;
                 }else {
                     NetworkCapabilities capabilities=connectivityManager.getNetworkCapabilities(network);
                     return capabilities!=null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                             capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
                 }

             }else {
                 NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                 return networkInfo!=null && networkInfo.isConnected();
             }
         }
     }
    }
}
