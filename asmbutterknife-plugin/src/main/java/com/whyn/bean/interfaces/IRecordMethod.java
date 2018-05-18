package com.whyn.bean.interfaces;

import android.support.annotation.NonNull;

import com.whyn.bean.element.MethodBean;

import java.util.Set;

public interface IRecordMethod {

    boolean addMethod(@NonNull MethodBean methodInfo);

    Set<MethodBean> getMethod();
}
