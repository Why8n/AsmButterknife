package com.yn.test;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Test extends Thread {

    public Test(Runnable r) {
        super(r);
    }

    public static void main(String[] agrs) throws IOException {
//        String reg = "access\\$\\d{3,}$";
//        String reg = "access\\$[01]+$";
//        System.out.println("access$000".matches(reg));
//        System.out.println("access$100".matches(reg));
//        System.out.println("access$1000".matches(reg));
//        System.out.println("access$0".matches(reg));
//        System.out.println("access$".matches(reg));
//        System.out.println("access$123".matches(reg));
//        System.out.println("access$1100a".matches(reg));
//        System.out.println("access$1a0".matches(reg));
//        System.out.println(
//                String.format("access$%03d", 9 * 100)
//        );

        File file = new File("E:/code/Android/Projects/AsmButterknife/sample-android/build/intermediates/classes/debug/com/yn/asmbutterknife/adapter/RecyclerAdapter$ViewHolder.class");
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        CheckClassAdapter check = new CheckClassAdapter(cw);
        cr.accept(check, 0);
    }
}
