package com.yn.sample.cr;


import org.objectweb.asm.AnnotationVisitor;

public class AsmAnnotationVisitor extends AnnotationVisitor {
    private static final String TAG = AsmAnnotationVisitor.class.getSimpleName();

    public AsmAnnotationVisitor(int api, AnnotationVisitor annotationVisitor) {
        super(api, annotationVisitor);
    }

    @Override
    public void visit(String name, Object value) {
        Utils.sop("%s:visit:name=%s,value=%s", TAG, name, value);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        Utils.sop("%s:visitEnum:name=%s,desc=%s,value=%s", TAG, name, descriptor, value);
        super.visitEnum(name, descriptor, value);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        Utils.sop("%s:visitArray:name=%s", TAG, name);
        return super.visitArray(name);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        return super.visitAnnotation(name, descriptor);
    }


    @Override
    public void visitEnd() {
        Utils.sop("%s:visitEnd", TAG);
        super.visitEnd();
    }
}
