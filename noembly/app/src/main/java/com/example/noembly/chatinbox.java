package com.example.noembly;

public class chatinbox {

    String aliciUid;
    String gönderenUid;
    String inBoxKey;
    String okundu;

    public chatinbox(){

    }

    public chatinbox(String aliciUid, String gönderenUid, String inBoxKey, String okundu) {
        this.aliciUid = aliciUid;
        this.gönderenUid = gönderenUid;
        this.inBoxKey = inBoxKey;
        this.okundu = okundu;
    }

    public String getAliciUid() {
        return aliciUid;
    }

    public void setAliciUid(String aliciUid) {
        this.aliciUid = aliciUid;
    }

    public String getGönderenUid() {
        return gönderenUid;
    }

    public void setGönderenUid(String gönderenUid) {
        this.gönderenUid = gönderenUid;
    }

    public String getInBoxKey() {
        return inBoxKey;
    }

    public void setInBoxKey(String inBoxKey) {
        this.inBoxKey = inBoxKey;
    }


    public String getOkundu() {
        return okundu;
    }

    public void setOkundu(String okundu) {
        this.okundu = okundu;
    }
}
