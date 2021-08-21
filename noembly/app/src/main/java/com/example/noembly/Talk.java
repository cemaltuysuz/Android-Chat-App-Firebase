package com.example.noembly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.noembly.Fragments.APIService;
import com.example.noembly.Notifications.Data;
import com.example.noembly.Notifications.MyResponse;
import com.example.noembly.Notifications.Sender;
import com.example.noembly.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Talk extends AppCompatActivity {

    ArrayList<items> arrayList;
    ArrayList <senderUsernm> senderİnfo;
    ListView listView;
    EditText yazilan_mesaj;
    Button yazilan_mesajı_gönder,messagethisbtn;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String alıcıID;
    String alıcıUsername;
    String gönderenID;
    String gonderenUsername;
    chatinbox chatinbox=null;
    LayoutInflater inflater;
    adapter adapter;
    Glide glide;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    TextView gorunentextview;

    //Sonradan
    com.example.noembly.Fragments.APIService apiService;
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        sharedPref= this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        arrayList = new ArrayList<>();
        listView = findViewById(R.id.mesaj_listesi);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Sonradan
        apiService = com.example.noembly.Notifications.Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        String imgurl = getIntent().getExtras().getString("imgurl");

        yazilan_mesaj = findViewById(R.id.message_edit);
        yazilan_mesajı_gönder=findViewById(R.id.mesaj_gonder_btn);

        messagethisbtn = findViewById(R.id.message_this_home_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        gönderenID = firebaseAuth.getUid();
        alıcıID    = getIntent().getExtras().getString("aliciID");
        alıcıUsername  = getIntent().getExtras().getString("alici_username");
        gorunentextview = findViewById(R.id.sohbet_ortası_username);
        gorunentextview.setText(alıcıUsername);
        //Gönderen Kişinin Kullanıcı adını almak için oluşturulmuş dbref ve tarama ardından değişkene atanması
        senderİnfo = new ArrayList<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.child("users").orderByChild("userID").equalTo(gönderenID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String username = snapshot1.child("username").getValue().toString();
                    senderİnfo.add(new senderUsernm(username));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        CircleImageView circleImageView = findViewById(R.id.profile_image);
        glide.with(Talk.this).load(imgurl).into(circleImageView);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View imgFocus = layoutInflater.inflate(R.layout.focus_img_talk,null);
                ImageView imgtalk = imgFocus.findViewById(R.id.focus_img_talk);
                Glide.with(Talk.this).load(imgurl).into(imgtalk);

                AlertDialog.Builder builder = new AlertDialog.Builder(Talk.this);
                builder.setView(imgFocus);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        adapter = new adapter();
        listView.setAdapter(adapter);

        Toast.makeText(Talk.this,"Kullanıcı adı:"+alıcıUsername+"\nAlıcı ID:"+alıcıID,Toast.LENGTH_SHORT).show();


            createChat();


        yazilan_mesajı_gönder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sonradan eklendi
                notify = true;
                databaseReference.child(Child.chats).push().setValue(
                        new chats(chatinbox.getInBoxKey(), gönderenID, yazilan_mesaj.getText().toString()), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                final String mesajkey = ref.getKey();
                                databaseReference.child(Child.chat_last).orderByChild("inBoxKey").equalTo(chatinbox.getInBoxKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                            databaseReference.child(Child.chat_last).child(snapshot.getKey()).child("mesajkey").setValue(mesajkey);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                databaseReference.child(Child.CHAT_INBOX).orderByChild("inBoxKey").equalTo(chatinbox.getInBoxKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                            if (alıcıID.equals(snapshot.child("gönderenUid").getValue().toString())){
                                                databaseReference.child(Child.CHAT_INBOX).child(snapshot.getKey()).child("okundu").setValue("1");
                                                // 1 ---> okundu
                                                // 0 ---> okunmadı
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                );

                //Sonradan eklendi

                final String msg = yazilan_mesaj.getText().toString();


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(gönderenID);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user_info user = dataSnapshot.getValue(user_info.class);
                        if (notify) {
                            sendNotifiaction(alıcıID,alıcıUsername, msg);
                        }
                        notify = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                yazilan_mesaj.setText("");
            }
        });

        messagethisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Talk.this,home.class);
                startActivity(intent);
            }
        });
    }




    //Metodlar

    //Sonradan eklendi
    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(alıcıID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(gönderenID, R.drawable.defpp, alıcıUsername+": "+message, "Yeni Mesaj!",
                            alıcıID);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            //Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void createChat() {

        databaseReference.child(Child.CHAT_INBOX).orderByChild("gönderenUid").equalTo(gönderenID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    if (snapshot.getValue(chatinbox.class).getAliciUid().equals(alıcıID)){

                            chatinbox = snapshot.getValue(chatinbox.class);
                    }

                }
                chatInboxAndChatLast();

                chats();

                chatLast();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public void chatInboxAndChatLast() {


        if (chatinbox == null){
            String key = databaseReference.push().getKey();

            //Gelen kutusu oluşturulması
            databaseReference.child(Child.CHAT_INBOX).push().setValue(

                    new chatinbox(gönderenID,alıcıID,key,"0")
            );
            databaseReference.child(Child.CHAT_INBOX).push().setValue(

                    new chatinbox(alıcıID,gönderenID,key,"0") );

            chatinbox = new chatinbox(gönderenID,alıcıID,key,"0");
            //Sonmesaj bilgisi

            databaseReference.child(Child.chat_last).push().setValue(new ChatLast(key,""));
        }
    }
    public void chats() {

        databaseReference.child(Child.chats).orderByChild("inBoxKey").equalTo(chatinbox.getInBoxKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    chats chats = snapshot.getValue(chats.class);
                    arrayList.add(new items(chats.getGönderenUid(),chats.getMesaj(),alıcıUsername));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    void chatLast() {

        databaseReference.child(Child.chat_last).orderByChild("inBoxKey").equalTo(chatinbox.getInBoxKey()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                databaseReference.child(Child.chats).child(dataSnapshot.child("mesajkey").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        chats chats = dataSnapshot.getValue(com.example.noembly.chats.class);
                        mesajgeldiokudum();
                        arrayList.add(new items(chats.getGönderenUid(),chats.getMesaj(),alıcıUsername));
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void mesajgeldiokudum() {
        if (sharedPref.getInt("durum",3)==1) {
            Toast.makeText(Talk.this,"Kişi uygulamada",Toast.LENGTH_SHORT).show();
            databaseReference.child(Child.CHAT_INBOX).orderByChild("inBoxKey").equalTo(chatinbox.getInBoxKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (gönderenID.equals(snapshot.child("gönderenUid").getValue().toString())) {

                            databaseReference.child(Child.CHAT_INBOX).child(snapshot.getKey()).child("okundu").setValue("0");

                            // 1 ---> okunmadı
                            // 0 ---> okundu

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



    class adapter extends BaseAdapter{


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

            if (view ==null){

                view = inflater.inflate(R.layout.control_row_talk_item,null);

            }

            LinearLayout lineartalk = view.findViewById(R.id.linear_talk);
            LinearLayout linearrow = view.findViewById(R.id.linear_talk_row);
            TextView mesaj = view.findViewById(R.id.mesajtext);



            if (arrayList.get(position).getUserID().equals(gönderenID)){

                lineartalk.setBackgroundResource(R.drawable.draw_talk_me);
                linearrow.setGravity(Gravity.RIGHT);
                mesaj.setTextColor(Color.parseColor("#383838"));
            }

            else{
                lineartalk.setBackgroundResource(R.drawable.draw_talk_alici);
                linearrow.setGravity(Gravity.LEFT);
                mesaj.setTextColor(Color.parseColor("#b3b3b3"));


            }

            mesaj.setText(arrayList.get(position).getMesaj());



            return view;
        }
    }

    //İtemler sınıfı
    class items {

        String userID;
        String mesaj;
        String username;

        public String getUserID() {
            return userID;
        }

        public String getMesaj() {
            return mesaj;
        }

        public String getUsername() {
            return username;
        }

        public items(String userID, String mesaj, String username) {
            this.userID = userID;
            this.mesaj = mesaj;
            this.username = username;
        }
    }

    class senderUsernm{
        String senderUsername;

        public senderUsernm(String senderUsername) {
            this.senderUsername = senderUsername;
        }

        public String getSenderUsername() {
            return senderUsername;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putInt("durum",0);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.putInt("durum",0);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putInt("durum",0);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        editor.putInt("durum",1);
        editor.commit();
    }
}