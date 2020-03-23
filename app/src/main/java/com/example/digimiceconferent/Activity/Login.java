package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnLogin;
    EditText email, password;
    SharedPrefManager sharedPrefManager;
    LinearLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loading = findViewById(R.id.loading_login);
        btnLogin = findViewById(R.id.bt_login);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);

        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPBoolean() && sharedPrefManager.getSPRole().equals("lead eo")) {
            Intent intent = new Intent(Login.this, HomePanitia.class);
            startActivity(intent);
            finish();
        } else if (sharedPrefManager.getSPBoolean() && sharedPrefManager.getSPRole().equals("eo")) {
            Intent intent = new Intent(Login.this, HomeAnggota.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                login();
            }
        });
    }

    private void login() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.4.107/myAPI/public/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                JSONObject data = null;
                try {

                    data = new JSONObject(response);
                    JSONObject getData = data.getJSONObject("result");
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_TOKEN, getData.getString("token"));
                    sharedPrefManager.saveSPBoolean(sharedPrefManager.SP_BOOLEAN, true);
                    String role_team = getData.getString("role_team");
                    String id_user = getData.getString("user_id");
                    String email = getData.getString("email");
                    String name = getData.getString("name");
                    String name_team = getData.getString("name_team");

                    if(role_team.equals("lead eo")) {
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_ROLE, role_team);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_ID_USER, id_user);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_EMAIL, email);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_NAME, name);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_NAME_TEAM, name_team);
                        Intent intent = new Intent(Login.this, HomePanitia.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    } else if (role_team.equals("eo")) {
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_ROLE, role_team);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_ID_USER, id_user);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_EMAIL, email);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_NAME, name);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_NAME_TEAM, name_team);
                        Intent intent = new Intent(Login.this, HomeAnggota.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> data = new HashMap<String, String>();
                data.put("email", email.getText().toString());
                data.put("password", password.getText().toString());
                return data;
            }
        };

        queue.add(stringRequest);
    }
}
