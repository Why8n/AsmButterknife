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
    public AnnotationVisitor visitAnnotationDefault() {
        Utils.sop("%s:vsiitAnnotatonDefault", TAG);
        return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        Utils.sop("%s:vsiitAnnotation: desc=%s,visible=%s", TAG, descriptor, visible);
        return new AsmAnnotationVisitor(Opcodes.ASM6, super.visitAnnotation(descriptor, visible));
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        Utils.sop("%s:visitLocalVariable: name=%s,desc=%s,signature=%s,start=%s,end=%s,index=%d",
                TAG, name, descriptor, signature, start, end, index);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        Utils.sop("visitMethodInsn+++:opcode=%d,owner=%s,name=%s,desc=%s,isInterfaace=%s",
                opcode, owner, name, descriptor, isInterface);
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
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

    @Override
    public void visitEnd() {
        super.visitEnd();
        Utils.sop("%s:visitEnd", TAG);
    }
}
