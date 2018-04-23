package com.whyn.asm.adapters.base;

import com.whyn.define.Const;

import org.objectweb.asm.AnnotationVisitor;

public class BaseAnnotationVisitor extends AnnotationVisitor {
    public BaseAnnotationVisitor(AnnotationVisitor av) {
        super(Const.ASM_API, av);
    }
}
