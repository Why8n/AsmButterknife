package com.whyn.bean.interfaces;

import com.android.annotations.NonNull;

public interface IRecordClass extends IRecordMethod, IRecordField, IRecordAnnotation, IRecordInnerClass {

    void recordClass(int version, @NonNull String clsInternalName);

    int getVersion();

    String getInternalName();
}
