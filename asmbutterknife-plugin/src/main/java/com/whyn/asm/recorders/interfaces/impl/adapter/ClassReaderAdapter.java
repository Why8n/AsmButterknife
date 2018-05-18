package com.whyn.asm.recorders.interfaces.impl.adapter;

import com.whyn.asm.recorders.interfaces.IClassReader;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ClassReaderAdapter implements IClassReader {
    @Override
    public void visitVersion(int version) {

    }

    @Override
    public void visitClass(int access, String name, String signature, String superName, String[] interfaces) {

    }

    @Override
    public void visitInnerClass(InnerClassBean bean) {

    }

    @Override
    public void visitField(FieldBean bean) {

    }

    @Override
    public void visitMethod(MethodBean bean) {

    }

    @Override
    public MethodVisitor injectMethod(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        return mv;
    }

    @Override
    public void visitEnd(ClassVisitor cv) {

    }
}
