package com.whyn.bean;

import com.yn.annotations.ViewInject;

import java.util.HashSet;
import java.util.Set;

public class ViewInjectBean {
    //record @ViewInject info
    private int mWhichView = ViewInject.NONE;
    private Set<BindViewBean> bindViewBeans;
    private byte[] mOriginByteCode;
    private String mInternalClassName; //full class name(eg: "java/lang/String")

    public ViewInjectBean(byte[] bytes) {
        this.mOriginByteCode = bytes;
        this.bindViewBeans = new HashSet<>();
    }

    public final byte[] getByteCode() {
        return this.mOriginByteCode;
    }

    public void setInternalClassNameClassName(String className) {
        this.mInternalClassName = className;
    }

    public String getInternalClassName() {
        return this.mInternalClassName;
    }

    public void addBindViewBean(BindViewBean bean) {
        this.bindViewBeans.add(bean);
    }

    public Set<BindViewBean> getBindViewBeans() {
        return this.bindViewBeans;
    }

    public void setWhichView(int whichView) {
        this.mWhichView = whichView;
    }

    public int getWhichView() {
        return this.mWhichView;
    }
}
