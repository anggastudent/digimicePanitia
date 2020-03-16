package com.example.digimiceconferent;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String FILE = "data_token";
    public static final String SP_TOKEN = "token";
    public static final String SP_BOOLEAN = "boolean";
    public static final String SP_ROLE = "role_team";
    public static final String SP_ID_USER = "id_user";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSPString(String keySP, String value) {
        editor.putString(keySP, value);
        editor.commit();
    }

    public void saveSPBoolean(String keySP, Boolean value) {
        editor.putBoolean(keySP, value);
        editor.commit();
    }

    public String getSPToken() {
        return sharedPreferences.getString(SP_TOKEN, "");
    }

    public Boolean getSPBoolean() {
        return sharedPreferences.getBoolean(SP_BOOLEAN, false);
    }

    public String getSPRole() {
        return sharedPreferences.getString(SP_ROLE, "");
    }

    public String getSPIdUser() {
        return sharedPreferences.getString(SP_ID_USER, "");
    }
}
