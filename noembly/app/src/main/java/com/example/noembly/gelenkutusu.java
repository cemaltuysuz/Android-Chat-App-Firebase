package com.example.noembly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class gelenkutusu extends AppCompatActivity {

    class items {
        String key;
        String inBoxKey;
        String Okundu;
        String sonmsg;
        String aliciUid;
        String username;
        String imgUrl;

        public String getKey() {
            return key;
        }

        public String getInBoxKey() {
            return inBoxKey;
        }

        public String getOkundu() {
            return Okundu;
        }

        public String getSonmsg() {
            return sonmsg;
        }

        public String getAliciUid() {
            return aliciUid;
        }

        public String getUsername() {
            return username;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public items(String key, String inBoxKey, String okundu, String sonmsg, String aliciUid, String username, String imgUrl) {
            this.key = key;
            this.inBoxKey = inBoxKey;
            Okundu = okundu;
            this.sonmsg = sonmsg;
            this.aliciUid = aliciUid;
            this.username = username;
            this.imgUrl = imgUrl;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelenkutusu);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference();

        final ArrayList<items>arrayList=new ArrayList<>();
        final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListView listView = findViewById(R.id.inbox_list);



        class adapter extends BaseAdapter {


            @Override
            public int getCount() {
                return arrayList.size();
            }

            @Override
            public Object getItem(int position) {
                return arrayList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = convertView;

                if (convertView==null){
                    view = inflater.inflate(R.layout.sohbetler_row,null);
                }

                TextView kullanıcıadı = view.findViewById(R.id.kullaniciadi_gelenkutusu);
                TextView sonmsg = view.findViewById(R.id.sohmesaj_gelenkutusu);
                CircleImageView circleImageView = view.findViewById(R.id.user_img_gelenkutusu);

                kullanıcıadı.setText(arrayList.get(position).getUsername());
                sonmsg.setText(" ");
                Helper.imageload(gelenkutusu.this,arrayList.get(position).getImgUrl(),circleImageView);

                circleImageView.setBorderColor(getResources().getColor(R.color.colorprofileborder));
                if (arrayList.get(position).getOkundu().equals("1")) {
                    circleImageView.setBorderWidth(6);
                } else {
                    circleImageView.setBorderWidth(0);
                }

                return view;
            }

        }
        final adapter adapter = new adapter();
        listView.setAdapter(adapter);

        databaseReference.child(Child.CHAT_INBOX).orderByChild("gönderenUid").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot infoInbox : snapshot.getChildren()){
                    arrayList.clear();
                    databaseReference.child(Child.users).orderByChild("userID").equalTo(infoInbox.child("aliciUid").getValue().toString()).
                            addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot infoUser : snapshot.getChildren()){
                                                arrayList.add(new items(infoInbox.getKey(),infoInbox.child("inBoxKey").getValue().toString(),
                                                infoInbox.child("okundu").getValue().toString(),
                                                " ",
                                                infoUser.child("userID").getValue().toString(),
                                                infoUser.child("username").getValue().toString(),
                                                infoUser.child("imgURL").getValue().toString()));


                                    }
                                    adapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(),"Hata:"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (arrayList.get(position).getOkundu().equals("1")) {
                    databaseReference.child(Child.CHAT_INBOX)
                            .child(arrayList.get(position).getKey()).child("okundu").setValue("0");
                }

                Intent intent = new Intent(gelenkutusu.this,Talk.class);
                intent.putExtra("aliciID",arrayList.get(position).getAliciUid());
                intent.putExtra("alici_username",arrayList.get(position).getUsername());
                intent.putExtra("imgurl",arrayList.get(position).getImgUrl());
                startActivity(intent);
            }
        });




        //Bottom menu ayarlar
        BottomNavigationView bottomNavigationView =findViewById(R.id.navBottomBtn);
        bottomNavigationView.setSelectedItemId(R.id.navBtnInbox);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){


                    case R.id.navBtnProfil:
                        Intent intent2 = new Intent(getApplicationContext(),kullaniciprofili.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navBtnKesfet:
                        Intent intent = new Intent(getApplicationContext(),home.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                }

                return false;
            }
        });






    }

}
