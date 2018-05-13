package com.yn.sample.cr;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmMethodVisitor extends MethodVisitor {
    private static final String TAG = AsmMethodVisitor.class.getSimpleName();

    public AsmMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor);
    }

    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
        Utils.sop("%s:visitParameter: name=%s,access=%d", TAG, name, access);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        Utils.sop("%s:visitLocalVariable: name=%s,desc=%s,signature=%s,start=%s,end=%s,index=%d",
                TAG, name, descriptor, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        Utils.sop("%s:visistParameterAnnotation: index=%d,desc=%s", TAG, parameter, descriptor);
        return super.visitParameterAnnotation(parameter, descriptor, visible);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        Utils.sop("%s:visitCode", TAG);
    }
}
