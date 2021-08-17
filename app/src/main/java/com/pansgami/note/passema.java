package com.pansgami.note;

public class passema {
    String password;
    String name;

    public passema() {
    }

    public passema( String name,String password) {
        this.password = password;
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
