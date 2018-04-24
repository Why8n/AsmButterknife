package com.whyn.bean.interfaces;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableSet;
import com.whyn.bean.element.MethodBean;

public interface IRecordMethod {

    boolean addMethod(@NonNull MethodBean methodInfo);
    ImmutableSet<MethodBean> getMethod();
}
