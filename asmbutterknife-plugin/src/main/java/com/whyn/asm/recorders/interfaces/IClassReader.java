package com.whyn.asm.recorders.interfaces;


import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

//visit [ visitSource ] [ visitModule ][ visitOuterClass ]
// ( visitAnnotation | visitTypeAnnotation | visitAttribute )*
// ( visitInnerClass | visitField | visitMethod )* visitEnd
public interface IClassReader {
    void visitVersion(int version);

    void visitClass(int access, String name, String signature, String superName, String[] interfaces);

    void visitInnerClass(InnerClassBean bean);

    void visitField(FieldBean bean);

    void visitMethod(MethodBean bean);

    MethodVisitor injectMethod(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions);

    void visitEnd(ClassVisitor cv);


}
