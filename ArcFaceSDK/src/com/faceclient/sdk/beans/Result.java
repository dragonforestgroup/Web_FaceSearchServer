package com.faceclient.sdk.beans;

import com.google.gson.Gson;

public class Result {
    int code;
    String msg;
    float similar = 0.0f;
    Person data;

    public Result(int code, String msg, Person data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Person data) {
        this.data = data;
    }

    public float getSimilar() {
        return similar;
    }

    public void setSimilar(float similar) {
        this.similar = similar;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Result successResult(Person o) {
        return new Result(0, "成功", o);
    }

    public static Result errorResult(String msg, Person o) {
        return new Result(999, msg, o);
    }
}
