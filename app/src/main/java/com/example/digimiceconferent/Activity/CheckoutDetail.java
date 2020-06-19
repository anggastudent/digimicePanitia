package com.example.digimiceconferent.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.digimiceconferent.Model.Expired;
import com.example.digimiceconferent.Model.Paid;
import com.example.digimiceconferent.Model.Pending;
import com.example.digimiceconferent.R;

public class CheckoutDetail extends AppCompatActivity {
    public static final String EXTRA_PENDING = "extra_pending";
    public static final String EXTRA_PAID = "extra_paid";
    public static final String EXTRA_EXPIRED = "extra_exp";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Checkout Detail");
        }

        WebView myweb = findViewById(R.id.webview);

        myweb.getSettings().setJavaScriptEnabled(true);
        myweb.getSettings().setUseWideViewPort(true);
        myweb.getSettings().setLoadWithOverviewMode(true);
        myweb.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        Pending pending = getIntent().getParcelableExtra(EXTRA_PENDING);
        Paid paid = getIntent().getParcelableExtra(EXTRA_PAID);
        Expired expired = getIntent().getParcelableExtra(EXTRA_EXPIRED);

        if (pending != null) {
            myweb.loadUrl(pending.getUrl());
        }

        if (paid != null) {
            
            if(paid.getUrl().equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutDetail.this);
                builder.setMessage("Paket tidak perlu pembayaran");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                builder.show();
            }else{
                myweb.loadUrl(paid.getUrl());

            }

        }

        if (expired != null) {
            myweb.loadUrl(expired.getUrl());
        }

        myweb.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
