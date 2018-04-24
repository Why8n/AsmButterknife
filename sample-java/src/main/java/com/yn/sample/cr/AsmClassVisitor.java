package com.yn.sample.cr;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


import java.rmi.AccessException;
import java.util.Arrays;

import static com.yn.sample.cr.AsmDemo.sop;

class AsmClassVisitor extends ClassVisitor {
    public AsmClassVisitor() {
        super(Opcodes.ASM6);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        version=51,access=33,name=com/yn/sample/cr/AsmDemo,signature=null,superName=java/lang/Object,interfaces=[Ljava.lang.String;@6ff3c5b5]
        sop("vistit:version=%d,access=%d,name=%s,signature=%s,superName=%s,interfaces=%s", version, access, name, signature, superName, interfaces);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitSource(String source, String debug) {
        sop("visitSource:source=%s,debug=%s", source, debug);
        super.visitSource(source, debug);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        sop("visitAnnotation:descriptor=%s,visible=%s", descriptor, visible);
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        sop("visitAttribute:attribute=%s", attribute);
        super.visitAttribute(attribute);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//        access=2,name=before,descriptor=Ljava/lang/String;,signature=null,value=null
        sop("visitField:access=%d,name=%s,descriptor=%s,signature=%s,value=%s", access, name, descriptor, signature, value);
        sop("getType().getClass()=%s", Type.getType(descriptor).getClassName());
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//        access=1,name=<init>,descriptor=()V,signature=null,exception=null
        sop("visitMethod:access=%d,name=%s,descriptor=%s,signature=%s,exception=%s", access, name, descriptor, signature, exceptions);
        Type[] argsType = Type.getArgumentTypes(descriptor);
        Utils.sop("args:%s", Arrays.toString(argsType));
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new AsmMethodVisitor(mv);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
//        name=com/yn/sample/cr/AsmDemo$TestStaticInner,outerName=com/yn/sample/cr/AsmDemo,innerName=TestStaticInner,access=8
        sop("visitInnerClass:name=%s,outerName=%s,innerName=%s,access=%d", name, outerName, innerName, access);
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public void visitEnd() {
        sop("visitEnd");
        super.visitEnd();
    }
}
