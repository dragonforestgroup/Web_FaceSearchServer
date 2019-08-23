package com.faceclient.sdk.beans;

/**
 * 实体 Image
 */
public class Image {
    int id;
    String uid;
    byte[] image;
    String base64;

    public Image() {
    }

    public Image(String uid, byte[] image, String base64) {
        this.uid = uid;
        this.image = image;
        this.base64 = base64;
    }

    public int getId() {
        return id;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
