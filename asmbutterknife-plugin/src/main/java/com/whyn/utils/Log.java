package com.whyn.utils;

public final class Log {
    private Log() {
    }

    public static void v(String info) {
        System.out.println(info);
    }

    public static void v(String info, Object... args) {
        v(String.format(info, args));
    }
}
