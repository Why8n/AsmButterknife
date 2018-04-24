package com.whyn.asm.adapters.collect;

import com.whyn.asm.adapters.base.BaseClassVisitor;
import com.whyn.bean.ViewInjectClassRecorder;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.Log;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;

public class CollectionClassAdapter extends BaseClassVisitor {

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Log.v("collect class: %s", name);
        ViewInjectClassRecorder.getInstance().recordClass(name);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        Log.v("CollectionClassAdapter:visitField: name=%s,desc=%s", name, desc);
        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
        FieldBean fieldBean = new FieldBean(name, desc, value);
        return new CollectionFieldAdapter(fv, fieldBean);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Log.v("CollectionClassAdapter:visitMethod: name=%s,desc=%s", name, desc);
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        MethodBean methodBean = new MethodBean(name, desc);
        methodBean.recordArguments(Type.getArgumentTypes(desc));
        return new CollectionMethodAdapter(mv, methodBean);
    }


    @Override
    public void visitSource(String source, String debug) {
        Log.v("visitSource:source=%s", source);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        Log.v("visitOuterClass:owner=%s,name=%s,desc=%s", owner, name, desc);

    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        Log.v("visitTypeAnnotation:");
        return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {
        Log.v("CollectionClassAdapter:visitAttribute:attr=%s", attr);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        Log.v("CollectionClassAdapter:visitInnerClass: name=%s,outerName=%s,innerName=%s", name, outerName, innerName);
    }

    @Override
    public void visitEnd() {
        Log.v("visitEnd:collect class done");
    }
}

