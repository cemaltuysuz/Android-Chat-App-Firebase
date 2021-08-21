package com.example.noembly;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class Helper {

    //        android:orientation="horizontal"

    public static boolean isOnline(){
        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            return true;
        }
        else{

            return false;
        }
    }

    public static void imageload (Context context, String url, CircleImageView circleView){

        Glide.with(context).load(url).into(circleView);
    }
}
