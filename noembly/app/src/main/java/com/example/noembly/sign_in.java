package com.example.noembly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_in extends AppCompatActivity {

    EditText giris_mail,giris_sifre;
    Button giris;
    TextView kayit_git;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        giris_mail = findViewById(R.id.giris_mail);
        giris_sifre = findViewById(R.id.giris_sifre);
        giris = findViewById(R.id.giris_btn);
        kayit_git = findViewById(R.id.kayittext);
        firebaseAuth = FirebaseAuth.getInstance();

        final Intent intent = new Intent(this,sign_up.class);


        kayit_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                giris.setBackgroundResource(R.drawable.green_bordur);


                firebaseAuth.signInWithEmailAndPassword(giris_mail.getText().toString(),giris_sifre.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                   //Basarili giris oldugu vakit

                        Intent intent = new Intent(sign_in.this,home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Basarisiz giris oldugu vakit
                        Toast.makeText(sign_in.this,"Hata:"+e.getMessage(),Toast.LENGTH_LONG).show();
                        giris.setBackgroundResource(R.drawable.red_burdur);


                    }
                });

            }
        });

        giris_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0){

                    giris_mail.setBackgroundResource(R.drawable.green_bordur);
                }
                else {

                    giris_mail.setBackgroundResource(R.drawable.white_bordur);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        giris_sifre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0){

                    giris_sifre.setBackgroundResource(R.drawable.green_bordur);
                }
                else {

                    giris_sifre.setBackgroundResource(R.drawable.white_bordur);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}