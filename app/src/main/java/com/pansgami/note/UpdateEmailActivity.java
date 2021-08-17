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

import java.util.Objects;


public class UpdateEmailActivity extends AppCompatActivity {
    TextView email,email2,oldemail;
    Button btneupdate;
    Validate validate;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        email =findViewById(R.id.etemail1);
        email2 =findViewById(R.id.etemail2);
        btneupdate =findViewById(R.id.btnupdateemail);
        oldemail=findViewById(R.id.etoldemail);
        validate = new Validate(this);
        progressBar=findViewById(R.id.progressBaremailup);

        btneupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateemailonfirebase();

            }
        });
    }

    private void updateemailonfirebase() {
        progressBar.setVisibility(View.VISIBLE);
        String p1=email.getText().toString();
        String p2=email2.getText().toString();

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser =firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        if((Objects.requireNonNull(firebaseUser.getEmail())).matches(oldemail.getText().toString()))
        {
            //Toast.makeText(UpdateEmailActivity.this,"Old Email match!",Toast.LENGTH_SHORT).show();
            if(validate.emailisvalid(p1))
            {
                if(p1.equals(p2))
                {
                    //update in firebase
                    firebaseUser.updateEmail(p1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(UpdateEmailActivity.this,"Email update successfully!",Toast.LENGTH_SHORT).show();
                                Intent intentforhome = new Intent(UpdateEmailActivity.this,HomeActivity.class);
                                intentforhome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentforhome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentforhome);
                                finish();

                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(UpdateEmailActivity.this,"Log out and Try Again! ",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(this,"Email didn't match !",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(UpdateEmailActivity.this,"Old Email didn't match!",Toast.LENGTH_SHORT).show();
        }


    }
}