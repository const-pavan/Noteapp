package com.pansgami.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvwelcome;
    Button btnlogout,btnuppassword,btnupemail,btncontact;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        tvwelcome =findViewById(R.id.tvwelcome);
        btnlogout =findViewById(R.id.btnlogout);
        btnuppassword =findViewById(R.id.btnuppassword);
        btnupemail =findViewById(R.id.btnupemail);
        btncontact=findViewById(R.id.btncontact);


        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser =firebaseAuth.getCurrentUser();


        context =UserAccountActivity.this;

        setwelcomemsg();

        btnlogout.setOnClickListener(this);
        btnuppassword.setOnClickListener(this);
        btnupemail.setOnClickListener(this);
        btncontact.setOnClickListener(this);

    }

    private void setwelcomemsg() {
        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
        DatabaseReference rootreference=firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        DatabaseReference namereference=rootreference.child("Users").child(user.getUid()).child("name");
        namereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //datasnapshot have ={name:"pavan"}
                tvwelcome.setText("Hi, welcome " + Objects.requireNonNull(snapshot.getValue()).toString() + " !");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        switch (id)
        {
            case R.id.btnlogout:
                showlogoutdiallogout();
                break;
            case R.id.btnuppassword:
                showupdatepasswordacivity();
                break;
            case R.id.btnupemail:
                showupdateemailactivity();
                break;
            case R.id.btncontact:
                Intent webintent= new Intent(UserAccountActivity.this,WebActivity.class);
                startActivity(webintent);
                break;
            default:
                break;
        }

    }

    private void showupdateemailactivity() {
        Intent showupdateemailintent = new Intent(UserAccountActivity.this,UpdateEmailActivity.class);
        startActivity(showupdateemailintent);
    }

    private void showupdatepasswordacivity() {
        Intent showupdatepasswordintent = new Intent(UserAccountActivity.this,UpdatePasswordActivity.class);
        startActivity(showupdatepasswordintent);

    }

    private void showlogoutdiallogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //logout here
                        FirebaseAuth.getInstance().signOut();
                        ((Activity)context).finish();
                        Intent loginintent = new Intent(UserAccountActivity.this,MainActivity.class);
                        startActivity(loginintent);

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();


    }
}