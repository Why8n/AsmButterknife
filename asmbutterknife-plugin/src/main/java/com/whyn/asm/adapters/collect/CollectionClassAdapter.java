package com.whyn.asm.adapters.collect;

import com.whyn.asm.adapters.base.BaseClassVisitor;
import com.whyn.asm.recorders.interfaces.impl.ViewInjectCollector;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class CollectionClassAdapter extends BaseClassVisitor {

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        ViewInjectCollector.getInstance().visitVersion(version);
        ViewInjectCollector.getInstance().visitClass(access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
        FieldBean fieldBean = new FieldBean(name, desc, value);
        return new CollectionFieldAdapter(fv, fieldBean);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        MethodBean methodBean = new MethodBean(name, desc, access);
        methodBean.recordArguments(Type.getArgumentTypes(desc));
        return new CollectionMethodAdapter(mv, methodBean);
    }


    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        ViewInjectCollector.getInstance().visitInnerClass(new InnerClassBean(name, outerName, innerName, access));
    }

    @Override
    public void visitEnd() {
//        ViewInjectCollector.getInstance().visitEnd(cv);
    }
}

