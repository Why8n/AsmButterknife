package com.whyn.asm.recorders.interfaces.impl;

import com.android.annotations.NonNull;
import com.whyn.asm.recorders.interfaces.IClassReader;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.Utils;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ViewInjectCollector implements IClassReader {
    private ClassReaderDispatcher mDispatcher;

    private ViewInjectCollector() {
//        throw new AssertionError("No instances.");
    }

    private static final class ViewHolder {
        private static final ViewInjectCollector sInstance = new ViewInjectCollector();
    }

    public static ViewInjectCollector getInstance() {
        return ViewHolder.sInstance;
    }

    public void init(@NonNull ClassReaderDispatcher dispatcher) {
        this.mDispatcher = dispatcher;
    }

    private void check() {
        Utils.checkNotNull(this.mDispatcher,
                "Dispatcher must not be null,did you forget to call ViewInjectCollector.init()");
    }

    @Override
    public void visitVersion(int version) {
        this.check();
        this.mDispatcher.visitVersion(version);

    }

    @Override
    public void visitClass(int access, String name, String signature, String superName, String[] interfaces) {
        this.check();
        this.mDispatcher.visitClass(access, name, signature, superName, interfaces);
    }

    @Override
    public void visitInnerClass(InnerClassBean bean) {
        this.check();
        this.mDispatcher.visitInnerClass(bean);
    }

    @Override
    public void visitField(FieldBean bean) {
        this.check();
        this.mDispatcher.visitField(bean);
    }

    @Override
    public void visitMethod(MethodBean bean) {
        this.check();
        this.mDispatcher.visitMethod(bean);
    }

    @Override
    public MethodVisitor injectMethod(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        return this.mDispatcher.injectMethod(mv, access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd(ClassVisitor cv) {
        this.check();
        this.mDispatcher.visitEnd(cv);
    }
}
