package com.faceclient.sdk.beans;

public class Person {
    int id;
    int groupid;
    byte[] feature;
    String vender;
    String version;
    String appid;
    String uname;
    String uid;

    public Person() {
    }

    public Person(int groupid, byte[] feature, String vender, String version, String appid, String uname, String uid) {
        this.groupid = groupid;
        this.feature = feature;
        this.vender = vender;
        this.version = version;
        this.appid = appid;
        this.uname = uname;
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public byte[] getFeature() {
        return feature;
    }

    public void setFeature(byte[] feature) {
        this.feature = feature;
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
