package com.yn.sample.cr;

public final class Utils {
    private Utils() {
    }

    public static void sop(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}
