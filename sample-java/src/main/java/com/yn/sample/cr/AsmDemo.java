package com.yn.sample.cr;


import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.ClassReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AsmDemo {
    private String before;

    public static void main(String[] args) throws IOException {
        ClassReader cr = new ClassReader(AsmDemo.class.getName());
        cr.accept(new AsmClassVisitor(), ClassReader.SKIP_DEBUG);
    }

    class TestInner extends AsmDemo {
        @BindView(1233)
        private String str;

        @Override
        protected void normalMethod() {
            super.normalMethod();
        }
    }

    static class TestStaticInner extends FileOutputStream {
        @BindView(3331111)
        private int i;

        public TestStaticInner(String s) throws FileNotFoundException {
            super(s);
        }
    }

    protected void normalMethod() {
    }

    private int[] middle;

    protected static void sop(String msg) {
        System.out.println("-------------------------------");
        System.out.println(msg);
    }

    static void sop(String msg, Object... args) {
        sop(String.format(msg, args));
    }

    private double end;
}
