package com.whyn.asm.recorders.interfaces.impl;

import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.asm.recorders.interfaces.IClassReader;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.LinkedList;
import java.util.List;

public class ClassReaderDispatcher implements IClassReader {
    private List<IClassReader> mViewRecorders = new LinkedList<>();

    public boolean register(IClassReader reader) {
        if (reader == null)
            return false;
        return mViewRecorders.add(reader);
    }

    public boolean unregister(IClassReader reader) {
        if (reader == null)
            return true;
        return mViewRecorders.remove(reader);
    }

    @Override
    public void visitVersion(int version) {
        for (IClassReader reader : mViewRecorders) {
            reader.visitVersion(version);
        }
    }

    @Override
    public void visitClass(int access, String name, String signature, String superName, String[] interfaces) {
        for (IClassReader reader : mViewRecorders) {
            reader.visitClass(access, name, signature, superName, interfaces);
        }

    }

    @Override
    public void visitInnerClass(InnerClassBean bean) {
        for (IClassReader reader : mViewRecorders) {
            reader.visitInnerClass(bean);
        }
    }

    @Override
    public void visitField(FieldBean bean) {
        for (IClassReader reader : mViewRecorders) {
            reader.visitField(bean);
        }
    }

    @Override
    public void visitMethod(MethodBean bean) {
        for (IClassReader reader : mViewRecorders) {
            reader.visitMethod(bean);
        }
    }

    @Override
    public MethodVisitor injectMethod(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = mv;
        MethodVisitor tempMethodVisitor;
        for (IClassReader reader : mViewRecorders) {
            tempMethodVisitor = reader.injectMethod(methodVisitor, access, name, desc, signature, exceptions);
            //ignnore those who don't wanna inject
            if (tempMethodVisitor != methodVisitor)
                methodVisitor = tempMethodVisitor;
        }
        return methodVisitor;
    }

    @Override
    public void visitEnd(ClassVisitor cv) {
        for (IClassReader reader : mViewRecorders) {
            reader.visitEnd(cv);
        }
    }
}
