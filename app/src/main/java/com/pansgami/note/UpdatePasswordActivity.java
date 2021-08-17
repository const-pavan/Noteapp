package com.pansgami.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdatePasswordActivity extends AppCompatActivity {

    TextView password,password2,oldpassword;
    Button btnupdate;
    Validate validate;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
    DatabaseReference rootreference=firebaseDatabase.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        password =findViewById(R.id.etPasswordsup);
        password2 =findViewById(R.id.etpassword2sup);
        oldpassword=findViewById(R.id.etoldPasswordsup);
        btnupdate =findViewById(R.id.btnsignup);
        validate = new Validate(this);

        progressBar=findViewById(R.id.progressBaruppassword);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser =firebaseAuth.getCurrentUser();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepassworonfirebase();
            }
        });
    }

    private void updatepassworonfirebase() {
        progressBar.setVisibility(View.VISIBLE);

        String p1=password.getText().toString();
        String p2=password2.getText().toString();
        String oldp=oldpassword.getText().toString();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        assert user != null;
        DatabaseReference namereference=rootreference.child("Users").child(user.getUid()).child("password");
        namereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //datasnapshot have ={name:"pavan"}
                //databasepass =(snapshot.getValue()).toString();
                //Toast.makeText(UpdatePasswordActivity.this,"password:"+oldp,Toast.LENGTH_SHORT).show();
                if(!oldp.equals((Objects.requireNonNull(snapshot.getValue())).toString())) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UpdatePasswordActivity.this,"Old Password didn't match !",Toast.LENGTH_SHORT).show();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdatePasswordActivity.this,"Try Again!",Toast.LENGTH_SHORT).show();
            }
        });

        if(validate.passwordisvalid(p1))
        {
            if(p1.equals(p2))
            {
                //update in firebase
                firebaseUser.updatePassword(p1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            DatabaseReference namereference=rootreference.child("Users").child(user.getUid()).child("password");
                            namereference.setValue(p1);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(UpdatePasswordActivity.this,"Password update successfully!",Toast.LENGTH_SHORT).show();
                            Intent intentforhome = new Intent(UpdatePasswordActivity.this,HomeActivity.class);
                            intentforhome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentforhome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentforhome);
                            finish();
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(UpdatePasswordActivity.this,"Try Again!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
            else
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdatePasswordActivity.this,"Passwords didn't match !",Toast.LENGTH_SHORT).show();
            }
        }


    }
}