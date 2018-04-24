package com.whyn.asm.adapters.base;

import com.whyn.define.Const;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class BaseMethodVisitor extends MethodVisitor {
    protected BaseMethodVisitor(MethodVisitor mv) {
        super(Const.ASM_API, mv);
    }
}
