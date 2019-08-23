package com.example.arcfaceserver;

public class SearchFaceResult {
    float similar;
    String name;
    String msg;

    public SearchFaceResult(float similar, String name, String msg) {
        this.similar = similar;
        this.name = name;
        this.msg = msg;
    }

    public float getSimilar() {
        return similar;
    }

    public void setSimilar(float similar) {
        this.similar = similar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
