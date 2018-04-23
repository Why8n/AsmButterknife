package com.whyn.asm.adapters.base;

import com.whyn.define.Const;

import org.objectweb.asm.MethodVisitor;

public class BaseMethodVisitor extends MethodVisitor {
    public BaseMethodVisitor(MethodVisitor mv) {
        super(Const.ASM_API, mv);
    }
}
