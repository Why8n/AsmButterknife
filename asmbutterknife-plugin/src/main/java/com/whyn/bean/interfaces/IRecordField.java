package com.whyn.bean.interfaces;

import android.support.annotation.NonNull;

import com.whyn.bean.element.FieldBean;

import java.util.Set;

public interface IRecordField {

    boolean addField(@NonNull FieldBean fieldInfo);

    Set<FieldBean> getField();
}
