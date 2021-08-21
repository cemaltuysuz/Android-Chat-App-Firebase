package com.example.noembly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splash extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splashanim);
        final Intent intent = new Intent(this,MainActivity.class);

        Thread zamanlama = new Thread() {


            public void run () {

                try {
                    sleep(3000);
                } catch (InterruptedException e){

                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }

            }


        };
        zamanlama.start();

    }
}