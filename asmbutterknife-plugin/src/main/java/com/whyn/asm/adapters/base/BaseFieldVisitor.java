package com.whyn.asm.adapters.base;

import com.whyn.define.Const;

import org.objectweb.asm.FieldVisitor;

public class BaseFieldVisitor extends FieldVisitor {
    public BaseFieldVisitor(FieldVisitor fv) {
        super(Const.ASM_API, fv);
    }
}
