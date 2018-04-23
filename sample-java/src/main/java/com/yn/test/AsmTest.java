package com.yn.test;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;


public class AsmTest {
    public static void main(String[] args) throws IOException {
        ClassReader classReader = new ClassReader("com.yn.test.Test");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new TestClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);

        byte[] classFile = classWriter.toByteArray();
        File file = new File("E:/code/Android/Projects/AsmButterknife/sample-java/build/classes/java/main/com/yn/test/Test.class");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(classFile);
        fos.close();

    }

    private static class TestClassVisitor extends ClassVisitor {

        public TestClassVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM6, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature,
                                         String[] exceptions) {
            System.out.println("visitMethod: " + name);
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            if ("main".equals(name)) {
                mv = new MainMethodVisitor(mv);
//                mv = new MainMethodAdapter(mv,access,name,desc);
            }
            return mv;
        }
    }

    private static class MainMethodVisitor extends MethodVisitor {

        public MainMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM6, methodVisitor);
        }

        private void sop(String msg) {
            mv.visitFieldInsn(Opcodes.GETSTATIC,
                    Type.getInternalName(System.class), //"java/lang/System"
                    "out",
                    Type.getDescriptor(PrintStream.class) //"Ljava/io/PrintStream;"
            );
            mv.visitLdcInsn(msg);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    Type.getInternalName(PrintStream.class),
                    "println",
                    "(Ljava/lang/String;)V",
                    false);
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            System.out.println("method start to insert code");
            sop("asm insert before");
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.RETURN) {
                System.out.println("method end to insert code");
                sop("asm insert after");
            }
            mv.visitInsn(opcode);
        }
    }


    private static class MainMethodAdapter extends AdviceAdapter {

        /**
         * Constructs a new {@link AdviceAdapter}.
         *
         * @param mv     the method visitor to which this adapter delegates calls.
         * @param access the method's access flags (see {@link Opcodes}).
         * @param name   the method's name.
         * @param desc   the method's descriptor (see {@link Type Type}).
         */
        protected MainMethodAdapter(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM6, mv, access, name, desc);
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            sop("AdviceAdater: asm insert code");
        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);
            sop("AdviceAdater: asm insert code");
        }

        private void sop(String msg) {
            mv.visitFieldInsn(Opcodes.GETSTATIC,
                    Type.getInternalName(System.class), //"java/lang/System"
                    "out",
                    Type.getDescriptor(PrintStream.class) //"Ljava/io/PrintStream;"
            );
            mv.visitLdcInsn(msg);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    Type.getInternalName(PrintStream.class),
                    "println",
                    "(Ljava/lang/String;)V",
                    false);
        }
    }

}
