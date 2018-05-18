package com.whyn.bean.interfaces;

import android.support.annotation.NonNull;

import com.whyn.bean.element.AnnotationBean;

import java.util.Set;

public interface IRecordAnnotation {

    boolean addAnnotation(@NonNull AnnotationBean annotationInfo);

    Set<AnnotationBean> getAnnotation();
}
