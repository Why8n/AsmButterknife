package com.whyn.bean.interfaces;

import com.android.annotations.NonNull;

public interface IRecordClass extends IRecordMethod, IRecordField, IRecordAnnotation,IRecordInnerClass {

    void recordClass(@NonNull String clsInternalName);

    String getInternalName();
}
