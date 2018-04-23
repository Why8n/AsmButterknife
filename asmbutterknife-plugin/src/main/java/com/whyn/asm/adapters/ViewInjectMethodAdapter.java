package com.whyn.asm.adapters;

import com.whyn.define.Const;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class ViewInjectMethodAdapter extends AdviceAdapter {
    protected ViewInjectMethodAdapter(MethodVisitor mv, int access, String name, String desc) {
        super(Const.ASM_API, mv, access, name, desc);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
//        if (Const.Annotation.VIEWINJECT_DESC.equals(desc))
        return av;

    }

    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
    }
}
