package com.pansgami.note;

import android.content.Context;
import android.widget.Toast;
import android.util.Patterns;

public class Validate {

    Context context;
    Validate(Context context)
    {
        this.context=context;
    }

    boolean emailisvalid(String email)
    {
        if(email.length()==0)
        {
            Toast.makeText(context,"Please Enter the Email!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(context,"Please Enter valid the Email!",Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;

    }
    boolean passwordisvalid(String password)
    {
        if(password.length()==0)
        {
            Toast.makeText(context,"Please Enter the Password!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.length()<6)
        {
            Toast.makeText(context,"Please Enter the Password of at least 6 characters!",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
