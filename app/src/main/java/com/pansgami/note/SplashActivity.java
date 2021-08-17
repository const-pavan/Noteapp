package com.pansgami.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        final FirebaseUser user =firebaseAuth.getCurrentUser();

        Handler handler = new Handler();

        handler.postDelayed(() -> {
            if(user!=null)
            {
                //user already log in
                Intent homeintent = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(homeintent);
                finish();
            }
            else
            {
                Intent homeintent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(homeintent);
                finish();

            }

        },2200);


    }
}