package com.example.noembly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class sign_up extends AppCompatActivity {

    EditText kayit_kullaniciadi,kayit_mail,kayit_sifre,kayit_sifre_tekrar;
    Button kayit_ol,girise_don;
    ImageView defpp;
    Uri uriphoto;
    CircleImageView defppp;

    //Firebase
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (Build.VERSION.SDK_INT >= 23){

            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED){

                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
            }

        }

        kayit_kullaniciadi = findViewById(R.id.kayit_kullaniciadi);
        kayit_mail = findViewById(R.id.kayit_mail);
        kayit_sifre=findViewById(R.id.kayit_sifre);
        kayit_sifre_tekrar=findViewById(R.id.kayit_sifre_tekrari);

        kayit_ol = findViewById(R.id.kayit_btn);
        girise_don=findViewById(R.id.girise_dön_btn);

        defppp = findViewById(R.id.profile_image);

        final Intent intent = new Intent(this,sign_in.class);

        girise_don.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        defppp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1,100);
            }
        });

        kayit_kullaniciadi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()<5){
                    kayit_kullaniciadi.setBackgroundResource(R.drawable.red_burdur);
                }
                else{
                    kayit_kullaniciadi.setBackgroundResource(R.drawable.green_bordur);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        kayit_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()<7){
                    kayit_mail.setBackgroundResource(R.drawable.red_burdur);
                }
                else{
                    kayit_mail.setBackgroundResource(R.drawable.green_bordur);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        kayit_sifre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>5){
                    kayit_sifre.setBackgroundResource(R.drawable.green_bordur);
                }

                else{
                    kayit_sifre.setBackgroundResource(R.drawable.red_burdur);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        kayit_sifre_tekrar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (kayit_sifre.getText().toString().length()>=s.length()){

                    String kontrol = kayit_sifre.getText().toString().substring(0,s.length());
                    if (kontrol.equals(s.toString())){

                        kayit_sifre.setBackgroundResource(R.drawable.green_bordur);
                        kayit_sifre_tekrar.setBackgroundResource(R.drawable.green_bordur);
                    }
                    else {

                        kayit_sifre.setBackgroundResource(R.drawable.red_burdur);
                        kayit_sifre_tekrar.setBackgroundResource(R.drawable.red_burdur);
                    }

                }
                else{

                    kayit_sifre.setBackgroundResource(R.drawable.red_burdur);
                    kayit_sifre_tekrar.setBackgroundResource(R.drawable.red_burdur);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        kayit_ol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayit_ol.setBackgroundResource(R.drawable.green_bordur);


                if (kayit_kullaniciadi.getText().toString().length()>5 && kayit_mail.getText().toString().length()>7 &&
                        kayit_sifre.getText().toString().length()>5 && kayit_sifre.getText().toString().equals(kayit_sifre_tekrar.getText().toString())
                        && defppp != null ){

                    String posta = kayit_mail.getText().toString();
                    String sifre = kayit_sifre.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(posta,sifre).
                            addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            //Kayit islemi Basarili ise
                            set_Userinfo();
                            Toast.makeText(sign_up.this,"Basarili",Toast.LENGTH_SHORT).show();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Kayit islemi basarisiz ise
                            kayit_ol.setBackgroundResource(R.drawable.red_burdur);
                            Toast.makeText(sign_up.this,"Basarisiz:"+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else{

                    if (defppp == null){

                        Toast.makeText(sign_up.this,"Profil resmin seçiniz!",Toast.LENGTH_SHORT);
                        kayit_ol.setBackgroundResource(R.drawable.red_burdur);
                    }

                }
            }
        });
    }

    //foto gönder
    void set_Userinfo () {

        UUID uuid = UUID.randomUUID();
        final String path = "img/"+uuid+".png";
        storageReference.child(path).putFile(uriphoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String uid = firebaseAuth.getUid();
                        String username = kayit_kullaniciadi.getText().toString();
                        String url = uri.toString();
                        String sifre = kayit_sifre.getText().toString();

                        databaseReference.child(Child.users).push().setValue(

                                new user_info(uid,username,url,sifre)
                        );

                        Intent intent = new Intent(sign_up.this,home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == 100 && data != null){

            uriphoto = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uriphoto);
                defppp.setImageBitmap(bitmap);

            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}