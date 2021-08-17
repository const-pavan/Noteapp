package com.pansgami.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    TextView welcometext;
    FloatingActionButton createnote;
    RecyclerView recyclerView;

    List<Note> noteArrayList = new ArrayList<>();
    NoteAdapter adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcometext=findViewById(R.id.tvwelcometext);
        createnote =findViewById(R.id.createNotefab);
        context=HomeActivity.this;


        //name from firebase
        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
        DatabaseReference rootreference=firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        DatabaseReference namereference=rootreference.child("Users").child(user.getUid()).child("name");
        namereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //datasnapshot have ={name:"pavan"}
                welcometext.setText("Hi, welcome " + Objects.requireNonNull(snapshot.getValue()).toString() + " !");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createnote.setOnClickListener(v -> opencreteNoteacitivity());

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //TODO: set adapter


        readnotefromfirebase();
    }

    private void readnotefromfirebase() {
        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootreference=firebaseDatabase.getReference();

        assert user != null;
        DatabaseReference notereference=rootreference.child("Users").child(user.getUid()).child("Notes");

        notereference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // datasnapshot contains : {NoteID : {noteTitle:"title",noteContent:"content"}....}
                Note note;
                noteArrayList.clear();
                for (DataSnapshot notesnapshot : snapshot.getChildren())
                {
                    note =notesnapshot.getValue(Note.class);

                    assert note != null;
                    note.setNoteID(notesnapshot.getKey());
                    //add to array list
                    noteArrayList.add(note);
                }
                Collections.reverse(noteArrayList);
                adapter = new NoteAdapter(HomeActivity.this);
                adapter.setNoteArrayList(noteArrayList);

                //set adapter
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this,"Please Check Network!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void opencreteNoteacitivity() {
        Intent createnoteintent = new Intent(HomeActivity.this,CreateNoteActivity.class);
        startActivity(createnoteintent);
    }

    public void deletenoteinhome(String noteID) {
        //delete note
        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootreference=firebaseDatabase.getReference();
        assert user != null;
        DatabaseReference notereference=rootreference.child("Users").child(user.getUid()).child("Notes");
        DatabaseReference deletenote=notereference.child(noteID);
        deletenote.removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                /*finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);*/
                Intent refresh = new Intent(this, HomeActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(refresh);
                this.finish();
            }
            else
            {
                deletenoteinhome(noteID);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.useraccount:
                Intent usetaccountintent = new Intent(HomeActivity.this,UserAccountActivity.class);
                startActivity(usetaccountintent);
                break;
            case R.id.logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            //logout here
                            FirebaseAuth.getInstance().signOut();
                            ((Activity)context).finish();
                            Intent loginintent = new Intent(HomeActivity.this,MainActivity.class);
                            startActivity(loginintent);

                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog=builder.create();
                dialog.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}