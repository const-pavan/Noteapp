package com.pansgami.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SingUPActivity extends AppCompatActivity {

    EditText etemail,etpassword,etpassword2,etname;
    Validate validate;
    String email,password,password2,name;
    Button signup;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_upactivity);

        Objects.requireNonNull(getSupportActionBar()).hide();
        //start sign up
        etname =findViewById(R.id.etNamesup);
        etemail =findViewById(R.id.etEmailsup);
        etpassword=findViewById(R.id.etPasswordsup);
        etpassword2=findViewById(R.id.etpassword2sup);
        signup =findViewById(R.id.btnsignup);
        progressBar =findViewById(R.id.progressBar);

        validate = new Validate(this);
        mAuth =FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> {
            //signup handle
            handlesignupclick();

        });


    }

    private void handlesignupclick() {
        progressBar.setVisibility(View.VISIBLE);
        //store value
        email =etemail.getText().toString();
        password =etpassword.getText().toString();
        password2=etpassword2.getText().toString();
        name=etname.getText().toString();

        if(!name.isEmpty())
        {
            if(validate.emailisvalid(email) && validate.passwordisvalid(password))
            {
                if(password.equals(password2))
                {
                    //sign up user
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //this is trigger when user is created
                            if(task.isSuccessful())
                            {
                                //user have user info like email,password
                                FirebaseUser user =mAuth.getCurrentUser();
                                assert user!=null;
                                Toast.makeText(SingUPActivity.this,"Sign Up is Successfully for "+ Objects.requireNonNull(user).getEmail(),Toast.LENGTH_SHORT).show();
                                //save name in firebase real time database
                                passema passema =new passema(name,password);

                                savenameindatabase(user,passema);
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intentforhome = new Intent(SingUPActivity.this,HomeActivity.class);
                                intentforhome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentforhome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentforhome);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SingUPActivity.this,"Please try again!",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(this,"Passwords didn't Match Try agin|",Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                progressBar.setVisibility(View.INVISIBLE);
            }

        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"Please Enter your name|",Toast.LENGTH_SHORT).show();
        }


    }

    private void savenameindatabase(FirebaseUser user, passema passema) {
        //firebase method
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootreference = firebaseDatabase.getReference();
        DatabaseReference namereference = rootreference.child("Users").child(user.getUid());

        namereference.setValue(passema);



    }
}