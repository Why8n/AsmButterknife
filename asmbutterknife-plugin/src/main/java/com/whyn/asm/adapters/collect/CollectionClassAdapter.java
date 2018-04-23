package com.whyn.asm.adapters.collect;

import com.android.annotations.NonNull;
import com.whyn.asm.adapters.base.BaseClassVisitor;
import com.whyn.bean.ViewInjectBean;
import com.whyn.utils.Log;
import com.yn.annotations.ViewInject;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;

public class CollectionClassAdapter extends BaseClassVisitor {
    private ViewInjectBean mViewInjectBean;
    private boolean mHasViewInjectAnnotation = false;

    public CollectionClassAdapter(@NonNull ViewInjectBean viewInjectBean) {
        this.mViewInjectBean = viewInjectBean;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Log.v("collect class: %s", name);
        this.mViewInjectBean.setInternalClassNameClassName(name);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//        descriptor=Lcom/yn/annotations/ViewInject;,visible=false
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        if (Type.getDescriptor(ViewInject.class).equals(desc)) {
            this.mHasViewInjectAnnotation = true;
            return new CollectionViewInjectAnnotationAdapter(av, this.mViewInjectBean);
        }
        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (!this.mHasViewInjectAnnotation)
            return null;
        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
        return new CollectionFieldAdapter(fv, name, desc, this.mViewInjectBean);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (!this.mHasViewInjectAnnotation)
            return null;
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return null;
    }


    @Override
    public void visitSource(String source, String debug) {
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    @Override
    public void visitEnd() {
        Log.v("visitEnd:collect class done");
    }
}

