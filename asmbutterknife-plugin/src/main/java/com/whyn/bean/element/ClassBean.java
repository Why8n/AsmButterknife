package com.whyn.bean.element;

import com.google.common.collect.ImmutableSet;
import com.whyn.bean.interfaces.IRecordClass;

import java.util.HashSet;
import java.util.Set;

public class ClassBean implements IRecordClass {
    private String interanlName;
    private Set<FieldBean> fieldsInfo = new HashSet<>();
    private Set<AnnotationBean> annotationsInfo = new HashSet<>();
    private Set<MethodBean> methodsInfo = new HashSet<>();

    @Override
    public void recordClass(String clsInternalName) {
        this.interanlName = clsInternalName;
    }

    @Override
    public String getInternalName() {
        return this.interanlName;
    }

    @Override
    public boolean addMethod(MethodBean methodBean) {
        return this.methodsInfo.add(methodBean);

    }

    @Override
    public boolean addAnnotation(AnnotationBean annotationBean) {
        return this.annotationsInfo.add(annotationBean);
    }

    @Override
    public boolean addField(FieldBean fieldInfo) {
        return this.fieldsInfo.add(fieldInfo);
    }

    @Override
    public ImmutableSet<FieldBean> getField() {
        return ImmutableSet.copyOf(this.fieldsInfo);
    }

    @Override
    public ImmutableSet<AnnotationBean> getAnnotation() {
        return ImmutableSet.copyOf(this.annotationsInfo);
    }

    @Override
    public ImmutableSet<MethodBean> getMethod() {
        return ImmutableSet.copyOf(this.methodsInfo);
    }

}
