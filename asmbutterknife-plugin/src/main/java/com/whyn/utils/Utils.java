package com.whyn.utils;

import com.android.annotations.NonNull;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public final class Utils {
    private Utils() {
        throw new AssertionError("No instances.");
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

    public static byte[] file2bytes(@NonNull FileInputStream input) throws IOException {
        return IOUtils.toByteArray(input);
    }

    public static boolean write2file(@NonNull File file, byte[] data) {
        boolean bRet = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            bRet = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.close(fos);
        }
        return bRet;
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

    public static void checkNotNull(final Object target, String errorMsg, Object... args) {
        if (target == null)
            throw new NullPointerException(String.format(errorMsg, args));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProperValue(Object value, T defaultValue) {
        if (value == null)
            value = defaultValue;
        return (T) value;
    }

}
