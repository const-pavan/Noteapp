package com.pansgami.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNoteActivity extends AppCompatActivity {

    EditText etTitle,etText;
    FloatingActionButton cretenewnote;
    ProgressBar progressBar;
    Context context;
    String ID=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        etTitle=findViewById(R.id.etTitle);
        etText =findViewById(R.id.etNote);
        cretenewnote=findViewById(R.id.btnright);
        progressBar=findViewById(R.id.pbcreatenote);
        context=CreateNoteActivity.this;


        if(getIntent().getExtras()!=null)
        {
            etTitle.setText(getIntent().getStringExtra("noteTitle"));
            etText.setText(getIntent().getStringExtra("noteContent"));
            ID=getIntent().getStringExtra("noteID");


        }



        cretenewnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put note in firebase
                progressBar.setVisibility(View.VISIBLE);
                putnoteinfirebase();

            }
        });

    }

    private void putnoteinfirebase() {
        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootreference=firebaseDatabase.getReference();

        assert user != null;
        DatabaseReference notereference=rootreference.child("Users").child(user.getUid()).child("Notes");

        DatabaseReference newnotereference =notereference.push();//unique id generate by firebase

        Note newnote = new Note(etTitle.getText().toString(),etText.getText().toString());

        newnotereference.setValue(newnote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(CreateNoteActivity.this,"Note Created Successfully!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference rootreference=firebaseDatabase.getReference();
                    assert user != null;
                    DatabaseReference notereference=rootreference.child("Users").child(user.getUid()).child("Notes");
                    if(ID!=null) {
                        DatabaseReference deletenote = notereference.child(ID);
                        deletenote.removeValue();
                    }

                    Intent intentforhome = new Intent(CreateNoteActivity.this,HomeActivity.class);
                    intentforhome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentforhome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentforhome);
                    finish();
                }
                else
                {
                    Toast.makeText(CreateNoteActivity.this,"Please try Again!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

}