package com.example.noembly;

public class chats {

    String inBoxKey;
    String gönderenUid;
    String mesaj;

    public chats(String inBoxKey, String gönderenUid, String mesaj) {
        this.inBoxKey = inBoxKey;
        this.gönderenUid = gönderenUid;
        this.mesaj = mesaj;
    }

    public chats(){

    }

    public String getInBoxKey() {
        return inBoxKey;
    }

    public void setInBoxKey(String inBoxKey) {
        this.inBoxKey = inBoxKey;
    }

    public String getGönderenUid() {
        return gönderenUid;
    }

    public void setGönderenUid(String gönderenUid) {
        this.gönderenUid = gönderenUid;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
}
