package com.example.findingidealtypeapp.utility;

import static okhttp3.internal.Internal.instance;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenDTO {

    public static String Token = null;
    public static boolean isImage = false;
    /*
    private static final String PREFS = "prefs";
    private static final String Token = "Access_Token";
    private Context mContext;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefsEditor;
    private static TokenDTO instance;


    public static synchronized TokenDTO init(Context context) {
        if(instance == null)
            instance = new TokenDTO(context);
        return instance;
    }

    private TokenDTO(Context context) {
        mContext = context;
        prefs = mContext.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
    }

    public static void setToken(String Token) {
        this.Token = Token;
    }

    public static String getToken(String defValue) {
        return prefs.getString(Token,defValue);
    }

    public static void clearToken() {
        prefsEditor.clear().apply();
    }

     */

}
