package com.example.noembly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class kullaniciprofili extends AppCompatActivity {



    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    CircleImageView circleImageView;
    TextView kullaniciadi;
    EditText biyografi;
    Glide glide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullaniciprofili);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        circleImageView = findViewById(R.id.profileImg);
        kullaniciadi = findViewById(R.id.username_profile);

        String kullaniciID = firebaseAuth.getUid();

        databaseReference.child("users").orderByChild("userID").equalTo(kullaniciID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    kullaniciadi.setText(snapshot1.child("username").getValue().toString());
                    String imgURL=snapshot1.child("imgURL").getValue().toString();
                    Helper.imageload(kullaniciprofili.this,imgURL,circleImageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Bottom menu ayarlar
        BottomNavigationView bottomNavigationView =findViewById(R.id.navBottomBtn);
        bottomNavigationView.setSelectedItemId(R.id.navBtnProfil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.navBtnInbox:

                        Intent intent = new Intent(getApplicationContext(),gelenkutusu.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navBtnProfil:

                        return true;
                    case R.id.navBtnKesfet:
                        Intent intent2 = new Intent(getApplicationContext(),home.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);

                }

                return false;
            }
        });


    }
}