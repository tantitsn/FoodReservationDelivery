package com.example.tugasbesar.foodreservationdelivery.configs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.tugasbesar.foodreservationdelivery.LoginActivity;

import java.util.HashMap;

/**
 * Created by nelitaaas on 17/10/16.
 */

public class SessionManagement {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private static final String PREF_NAME = "LoginPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_MEJA = "meja";


    public SessionManagement(Context context){
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String username, String meja){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_MEJA, meja);

        editor.commit();
    }

    public boolean checkLogin(){

        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
            return true;
        }

        return false;
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_MEJA, pref.getString(KEY_MEJA, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
