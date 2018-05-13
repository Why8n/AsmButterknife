package com.whyn.bean.element;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.whyn.bean.interfaces.IRecordClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.objectweb.asm.Opcodes.V1_7;

public class ClassBean implements IRecordClass {
    private int version = V1_7;
    private String interanlName;
    private Set<FieldBean> fieldsInfo = new HashSet<>();
    private Set<AnnotationBean> annotationsInfo = new HashSet<>();
    private Set<MethodBean> methodsInfo = new HashSet<>();
    private List<InnerClassBean> innerClassInfo = new ArrayList<>();

    @Override
    public void recordClass(int version,String clsInternalName) {
        this.interanlName = clsInternalName;
    }

    @Override
    public int getVersion() {
        return this.version;
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

    @Override
    public boolean addInnerClass(InnerClassBean innerClassInfo) {
        return this.innerClassInfo.add(innerClassInfo);
    }

    @Override
    public List<InnerClassBean> getInnerClass() {
        return ImmutableList.copyOf(this.innerClassInfo);
    }
}
