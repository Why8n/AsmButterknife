package com.whyn.bean.element;

import com.whyn.utils.AsmUtils;

public class InnerClassBean {
    public final String clsInternalName;
    public final String outerClsInternalName;
    public final String innerName;
    public final int access;

    public InnerClassBean(String name, String outerName, String innerName, int access) {
        this.clsInternalName = name;
        this.outerClsInternalName = outerName;
        this.innerName = innerName;
        this.access = access;
    }

    public boolean isAnonymous() {
        return this.innerName == null;
    }

    @Override
    public String toString() {
        return String.format("%s", AsmUtils.internal2Class(this.clsInternalName));
    }
}
