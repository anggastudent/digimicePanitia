package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.digimiceconferent.Model.Expired;
import com.example.digimiceconferent.Model.Paid;
import com.example.digimiceconferent.Model.Pending;
import com.example.digimiceconferent.R;

public class CheckoutDetail extends AppCompatActivity {
    public static final String EXTRA_PENDING = "extra_pending";
    public static final String EXTRA_PAID = "extra_paid";
    public static final String EXTRA_EXPIRED = "extra_exp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Checkout Detail");
        }

        WebView myweb = findViewById(R.id.web_invoice);
        myweb.getSettings().setJavaScriptEnabled(true);
        myweb.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //  Toast.makeText(CheckoutDetail.this, "Web berhasil di load", Toast.LENGTH_SHORT).show();
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
                builder.setMessage("Paket Gratis tidak ada Checkout");
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



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
