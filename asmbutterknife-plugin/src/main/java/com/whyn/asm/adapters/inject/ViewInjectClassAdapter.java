package com.whyn.asm.adapters.inject;

import com.whyn.asm.adapters.base.BaseClassVisitor;
import com.whyn.asm.recorders.interfaces.impl.ViewInjectCollector;
import com.whyn.utils.Log;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class ViewInjectClassAdapter extends BaseClassVisitor {
    public ViewInjectClassAdapter(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Log.v("----------------------------------------");
        Log.v("start to inject class: %s", name);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
//        access=2,name=str,descriptor=Ljava/lang/String;,signature=null,value=null
        return super.visitField(access, name, desc, signature, value);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Log.v("visitMethod:name=%s,desc=%s", name, desc);
//        visitMethod:access=1,name=<init>,descriptor=()V,signature=null,exception=null
//        visitMethod:access=9,name=main,descriptor=([Ljava/lang/String;)V,signature=null,exception=[Ljava.lang.String;@3764951d])
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return ViewInjectCollector.getInstance().injectMethod(mv, access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        ViewInjectCollector.getInstance().visitEnd(this.cv);
        super.visitEnd();
    }
}
