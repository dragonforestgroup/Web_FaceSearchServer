package com.example.arcfaceserver.util;

public class LogUtil {
    static boolean isDebug = false;

    public static void D(String msg) {
        if (isDebug)
            System.out.println(msg);
    }

    public static void E(String msg) {
        System.out.println(msg);
    }
}
