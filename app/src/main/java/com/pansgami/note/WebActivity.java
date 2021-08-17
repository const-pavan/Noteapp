package com.pansgami.note;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView=findViewById(R.id.webview);
        webView.loadUrl("https://instagram.com/pavan_gami?utm_medium=copy_link");
    }
}