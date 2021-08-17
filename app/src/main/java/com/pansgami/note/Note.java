package com.pansgami.note;

import com.google.firebase.database.Exclude;

public class Note {
    String noteTitle;
    String noteContent;

    @Exclude
    String NoteID;

    @Exclude
    public String getNoteID() {
        return NoteID;
    }

    @Exclude
    public void setNoteID(String noteID) {
        NoteID = noteID;
    }

    public Note()
    {
        //Firebase need empty Constructor
    }

    public Note(String noteTitle, String noteContent) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }



}
