package com.whyn.asm.adapters.base;

import com.whyn.define.Const;

import org.objectweb.asm.ClassVisitor;

public class BaseClassVisitor extends ClassVisitor {
    public BaseClassVisitor(ClassVisitor cv) {
        super(Const.ASM_API, cv);
    }

    public BaseClassVisitor() {
        super(Const.ASM_API);
    }
}
