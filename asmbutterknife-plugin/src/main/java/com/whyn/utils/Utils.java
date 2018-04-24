package com.whyn.utils;

import com.android.annotations.NonNull;
import com.whyn.define.AndroidType;

import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.ast.expr.CastExpression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public final class Utils {
    private Utils() {
    }

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void log(String format, Object... args) {
        log(String.format(format, args));
    }

    public static void close(OutputStream os) {
        try {
            if (os != null)
                os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close(InputStream is) {
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] file2bytes(FileInputStream input) throws IOException {
        return IOUtils.toByteArray(input);
    }

    @Deprecated
    public static byte[] file2bytes(File file) {
        byte[] bytes = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            bytes = new byte[fis.available()];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fis);
        }
        return bytes;
    }

    public static boolean isSubType(@NonNull Class<?> srcTypeCls, @NonNull String dstType) throws ClassNotFoundException {
        boolean bRet = false;
        Class<?> srcSuperClass = srcTypeCls;
        while (srcSuperClass != null) {
            Log.v("isSubType: type=%s", srcSuperClass.getName());
            if (bRet = dstType.equals(srcSuperClass.getName()))
                break;
            srcSuperClass = srcSuperClass.getSuperclass();
        }
        return bRet;
    }

    public static boolean isActivity(@NonNull Class<?> srcTypeCls) {
        boolean bRet = false;
        try {
            bRet = isSubType(srcTypeCls, AndroidType.ACTIVITY_TYPE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bRet;
    }

    public static boolean isView(@NonNull Class<?> type) {
        boolean bRet = false;
        try {
            bRet = isSubType(type, AndroidType.VIEW_TYPE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bRet;
    }

    public static void checkNotNull(final Object target, String errorMsg, Object... args) {
        if (target == null)
            throw new IllegalArgumentException(String.format(errorMsg, args));
    }

    public static <T> T getProperValue(Object value, T defaultValue) {
        if (value == null)
            value = defaultValue;
        return (T) value;
    }

}
