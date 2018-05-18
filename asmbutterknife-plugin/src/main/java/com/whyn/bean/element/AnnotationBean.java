package com.whyn.bean.element;

import com.android.annotations.NonNull;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AnnotationBean {
    public final String typeDesc;
    private Map<String, Object> methodValue = new HashMap<>();
    private List<AnnotationBean> annotatedAnnotation;

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

    public boolean addAnnotatedAnnotation(AnnotationBean bean) {
        if (this.annotatedAnnotation == null)
            this.annotatedAnnotation = new ArrayList<>();
        return this.annotatedAnnotation.add(bean);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("@" + AsmUtils.desc2Class(this.typeDesc));
        builder.append("(");
        for (Map.Entry<String, Object> entry : this.methodValue.entrySet()) {
            builder.append(entry.getKey() + "=" + entry.getValue());
            builder.append("\n");
        }
        int index = builder.lastIndexOf("\n");
        return index == -1 ? builder.append(")").toString() :
                builder.substring(0, builder.lastIndexOf("\n")) + ")";
    }
}
