package com.whyn.bean.interfaces;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableSet;
import com.whyn.bean.element.FieldBean;

public interface IRecordField {

    boolean addField(@NonNull FieldBean fieldInfo);
    ImmutableSet<FieldBean> getField();
}
