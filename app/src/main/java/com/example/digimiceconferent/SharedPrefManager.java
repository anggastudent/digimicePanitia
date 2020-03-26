package com.example.digimiceconferent;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String FILE = "data_token";
    public static final String SP_TOKEN = "token";
    public static final String SP_BOOLEAN = "boolean";
    public static final String SP_ROLE = "role_team";
    public static final String SP_ID_USER = "id_user";
    public static final String SP_EMAIL = "email";
    public static final String SP_NAME = "name_user";
    public static final String SP_NAME_TEAM = "name_team";
    public static final String SP_ID_EVENT = "id_event";

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

    public String getSpEmail() {
        return sharedPreferences.getString(SP_EMAIL, "");
    }

    public String getSpName() {
        return sharedPreferences.getString(SP_NAME, "");
    }

    public String getSpNameTeam() {
        return sharedPreferences.getString(SP_NAME_TEAM, "");
    }

    public String getSpIdEvent() {
        return sharedPreferences.getString(SP_ID_EVENT, "");
    }
}
