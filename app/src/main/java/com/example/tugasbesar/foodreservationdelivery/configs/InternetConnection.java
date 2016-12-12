package com.example.tugasbesar.foodreservationdelivery.configs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

/**
 * Created by tanti on 12/12/16.
 */
public class InternetConnection {

    public static boolean checkConnection(@NonNull Context context)
    {
        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
