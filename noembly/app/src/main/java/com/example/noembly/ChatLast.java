package com.example.noembly;

public class ChatLast {

    String inBoxKey;
    String mesajkey;

    public ChatLast(String inBoxKey, String mesajkey) {
        this.inBoxKey = inBoxKey;
        this.mesajkey = mesajkey;
    }

    public ChatLast (){


    }

    public String getInBoxKey() {
        return inBoxKey;
    }

    public void setInBoxKey(String inBoxKey) {
        this.inBoxKey = inBoxKey;
    }

    public String getMesajkey() {
        return mesajkey;
    }

    public void setMesajkey(String mesajkey) {
        this.mesajkey = mesajkey;
    }
}
