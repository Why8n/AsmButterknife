package com.whyn.utils;

public final class Log {
    private static boolean isDebug = true;

    private Log() {
    }

    public static void v(String info) {
        if (isDebug)
            System.out.println(info);
    }

    public static void v(String info, Object... args) {
        v(String.format(info, args));
    }

    public static void w(String info, Object... args) {
        System.out.println(String.format(info, args));
    }

    public static void e(String info, Object... args) {
        System.err.println(String.format(info, args));
    }
}
