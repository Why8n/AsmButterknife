package com.whyn.bean.element;

import com.android.annotations.NonNull;
import com.whyn.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public final class AnnotationBean {
    //annotation type descriptor,etc: Lcom/yn/asmbutterknife/annotations/ViewInject;
    public final String typeDesc;
    private Map<String, Object> methodValue = new HashMap<>();

    public AnnotationBean(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public void addMethdValue(String methodName, Object value) {
        this.methodValue.put(methodName, value);
    }

    public Object getValue(@NonNull String methodName) {
        Utils.checkNotNull(methodName, "method name passed to Annotation %s must not be null", this.typeDesc);
        return this.methodValue.get(methodName);
    }

    public Object getValue() {
        return this.getValue("value");
    }

}
