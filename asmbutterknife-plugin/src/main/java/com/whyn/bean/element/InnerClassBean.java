package com.whyn.bean.element;

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
}
