package com.example.noembly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.noembly.Notifications.Token;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class home extends AppCompatActivity {

    class gelenkutusu_items{

        String userID;
        String username;
        String profileUrl;

        public String getUserID() {
            return userID;
        }

        public String getUsername() {
            return username;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public String getSonmesaj() {
            return sonmesaj;
        }

        public gelenkutusu_items(String userID, String username, String profileUrl, String sonmesaj) {
            this.userID = userID;
            this.username = username;
            this.profileUrl = profileUrl;
            this.sonmesaj = sonmesaj;
        }

        String sonmesaj;

    }

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    BottomNavigationItemView bottomNavigationItemView;
    String user;

    ImageView cikis_yap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
       updateToken(FirebaseInstanceId.getInstance().getToken());

        cikis_yap = findViewById(R.id.cikis_yap);
        final String gönderenID = firebaseAuth.getUid();


        ListView listView = findViewById(R.id.sohbetlistview);


        final ArrayList<gelenkutusu_items> arrayList = new ArrayList<>();

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        class custom_adapter extends BaseAdapter{


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

                if (view == null){

                    view = inflater.inflate(R.layout.sohbetler_row,null);

                }

                CircleImageView row_userimg = view.findViewById(R.id.user_img_gelenkutusu);
                TextView  row_username = view.findViewById(R.id.kullaniciadi_gelenkutusu);
                TextView  row_finishms = view.findViewById(R.id.sohmesaj_gelenkutusu);

                row_username.setText("@"+arrayList.get(position).getUsername());
                Helper.imageload(home.this,arrayList.get(position).getProfileUrl(),row_userimg);
                row_finishms.setText(arrayList.get(position).getSonmesaj());

                row_userimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View focus = layoutInflater.inflate(R.layout.image_focus_home,null);
                        ImageView focusimg = focus.findViewById(R.id.focus_home_img);
                        TextView focusUsername= focus.findViewById(R.id.focus_home_username);
                        TextView focustext= focus.findViewById(R.id.focus_home_infotext);
                        Glide.with(home.this).load(arrayList.get(position).getProfileUrl()).into(focusimg);
                        focusUsername.setText(arrayList.get(position).getUsername().toUpperCase());
                        focustext.setText("Hakkında");
                        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                        builder.setView(focus);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                    }
                });

                return view;
            }
        }

        final custom_adapter adapter = new custom_adapter();
        listView.setAdapter(adapter);

        databaseReference.child(Child.users).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    user_info info = snapshot.getValue(user_info.class);
                    if(!info.getUserID().equals(gönderenID)){

                        arrayList.add(new gelenkutusu_items(info.getUserID(),info.getUsername(),info.getImgURL()," "));

                    }
                }
            adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(home.this,Talk.class);
                intent.putExtra("aliciID",arrayList.get(position).getUserID());
                intent.putExtra("alici_username",arrayList.get(position).getUsername());
                intent.putExtra("imgurl",arrayList.get(position).getProfileUrl());
                startActivity(intent);

            }
        });


        //Bottom menu ayarlar
        BottomNavigationView bottomNavigationView =findViewById(R.id.navBottomBtn);
        bottomNavigationView.setSelectedItemId(R.id.navBtnKesfet);
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
                        Intent intent2 = new Intent(getApplicationContext(),kullaniciprofili.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navBtnKesfet:

                }

                return false;
            }
        });




        cikis_yap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setTitle("Çıkış Yap");
                builder.setMessage("Çıkış yapmak istediğinizden emin misiniz ?");
                builder.setPositiveButton("Çıkış yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseAuth.signOut();
                        startActivity(new Intent(home.this,sign_in.class));
                        finish();
                    }
                });

                builder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });





    }
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(user).setValue(token1);
    }


}

