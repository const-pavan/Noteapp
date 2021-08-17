package com.pansgami.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etemail,etpassword;
    Button btnlogin;
    TextView tvsingup;
    Validate validate;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //start login
        etemail=findViewById(R.id.etEmailmain);
        etpassword=findViewById(R.id.etPasswordmain);
        btnlogin=findViewById(R.id.btnLoginmain);
        tvsingup =findViewById(R.id.tvsignupmain);
        progressBar =findViewById(R.id.pbmain);

        btnlogin.setOnClickListener(this);
        tvsingup.setOnClickListener(this);
        validate =new Validate(this);

        mAuth=FirebaseAuth.getInstance();

        //end login
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.btnLoginmain:
                handleloginbtnclick();
                break;
            case R.id.tvsignupmain:
                handlesignupbtnclick();
                break;
        }
    }


    private void handleloginbtnclick() {
        showprogessbar();
        String email=etemail.getText().toString();
        String password=etpassword.getText().toString();
        // Validate email and password
        if(validate.emailisvalid(email)&&validate.passwordisvalid(password))
        {
            //
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        hideprogessbar();
                        //Toast.makeText(MainActivity.this,"Sign In Successfully!",Toast.LENGTH_SHORT).show();
                        Intent useractivityintent = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(useractivityintent);
                    }
                    else
                    {
                        hideprogessbar();
                        Toast.makeText(MainActivity.this,"Please try again!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            hideprogessbar();
        }
    }

    private void hideprogessbar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showprogessbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void handlesignupbtnclick() {
        Intent sighupintent=new Intent(MainActivity.this,SingUPActivity.class);
        startActivity(sighupintent);
    }
}