package com.whyn.bean.element;

import com.google.common.collect.ImmutableSet;
import com.whyn.bean.interfaces.IRecordAnnotation;
import com.whyn.utils.AsmUtils;

import java.util.HashSet;
import java.util.Set;

public final class FieldBean implements IRecordAnnotation {
    public final String name;
    public final String desc;
    private Object value; //default value,works only for static field
    private Set<AnnotationBean> annotationsInfo = new HashSet<>();

    public FieldBean(String name, String desc, Object defaultValue) {
        this.name = name;
        this.desc = desc;
        this.value = defaultValue;
    }

    @Override
    public boolean addAnnotation(AnnotationBean annotationInfo) {
        return this.annotationsInfo.add(annotationInfo);
    }

    @Override
    public Set<AnnotationBean> getAnnotation() {
        return ImmutableSet.copyOf(this.annotationsInfo);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (AnnotationBean annotation : this.annotationsInfo) {
            builder.append(annotation);
            builder.append("\n");
        }
        builder.append(AsmUtils.desc2Class(this.desc) + " " + this.name);
        return builder.toString();
    }
}
