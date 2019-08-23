package com.example.arcfaceserver.common;

public class CompareResult {
    String name;
    float similar;

    public CompareResult(String name, float similar) {
        this.name = name;
        this.similar = similar;
    }

    public CompareResult() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSimilar() {
        return similar;
    }

    public void setSimilar(float similar) {
        this.similar = similar;
    }
}
