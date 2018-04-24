package com.whyn.bean.interfaces;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableSet;
import com.whyn.bean.element.AnnotationBean;

public interface IRecordAnnotation {

    boolean addAnnotation(@NonNull AnnotationBean annotationInfo);

    ImmutableSet<AnnotationBean> getAnnotation();
}
