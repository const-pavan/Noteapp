package com.pansgami.note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.viewHolder> {
    List<Note> noteArrayList;

    public void setNoteArrayList(List<Note> noteArrayList) {
        this.noteArrayList = noteArrayList;
        notifyDataSetChanged();
    }

    Context mContext;

    public NoteAdapter(Context mContext)
    {
        this.mContext=mContext;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle,noteContent,tvedit,tvdelete;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle =itemView.findViewById(R.id.tvTitlecard);
            noteContent=itemView.findViewById(R.id.tvContentcard);
            tvedit=itemView.findViewById(R.id.tvEdit);
            tvdelete=itemView.findViewById(R.id.tvDelete);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card,parent,false);

        viewHolder viewHolderhere =new viewHolder(view);

        return viewHolderhere;
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Note note =noteArrayList.get(position);
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteContent.setText(note.getNoteContent());

        holder.tvedit.setOnClickListener(v -> {
            Intent editintent=new Intent(mContext,CreateNoteActivity.class);
            editintent.putExtra("noteTitle",note.getNoteTitle());
            editintent.putExtra("noteContent",note.getNoteContent());
            editintent.putExtra("noteID",note.getNoteID());
            mContext.startActivity(editintent);
        });

        holder.tvdelete.setOnClickListener(v -> showdeletedialog(note.getNoteID()));

    }

    private void showdeletedialog(String noteID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    //delete here
                    ((HomeActivity)mContext).deletenoteinhome(noteID);
                    notifyDataSetChanged();

                }).setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog=builder.create();
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }


}
