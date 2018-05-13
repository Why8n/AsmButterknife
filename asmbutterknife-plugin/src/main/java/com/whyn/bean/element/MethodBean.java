package com.whyn.bean.element;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableSet;
import com.whyn.bean.interfaces.IRecordAnnotation;

import org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;

public class MethodBean implements IRecordAnnotation {
    public final int access;
    public final String methodName;
    public final String methodDesc;
    private Set<AnnotationBean> annotationsInfo = new HashSet<>();
    //    private List<ArgumentBean> argumentsInfo = new ArrayList<>();
    private Type[] argumentsInfo;

    public MethodBean(String methodName, String methodDesc, int access) {
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.access = access;
    }

    @Override
    public boolean addAnnotation(@NonNull AnnotationBean annotationInfo) {
        return this.annotationsInfo.add(annotationInfo);
    }

    @Override
    public ImmutableSet<AnnotationBean> getAnnotation() {
        return ImmutableSet.copyOf(this.annotationsInfo);
    }

    public void recordArguments(Type[] args) {
        this.argumentsInfo = args;
    }

    public Type[] getArgument() {
        return this.argumentsInfo;
    }


    @Override
    public int hashCode() {
        int result = 17;
        result *= 31 + this.methodName.hashCode();
        result *= 31 + this.methodDesc.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        boolean bRet = false;
        do {
            if (bRet = this == o)
                break;
            if (bRet = !(o instanceof MethodBean))
                break;
            MethodBean other = (MethodBean) o;
            bRet = (this.methodName == null ? other.methodName == null : this.methodName.equals(other.methodName)
                    && this.methodDesc == null ? other.methodDesc == null : this.methodDesc.equals(other.methodDesc)
            );
        } while (false);
        return bRet;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (AnnotationBean annotation : this.annotationsInfo) {
            builder.append(annotation);
            builder.append("\n");
        }
        builder.append(this.methodName);
        builder.append("(");
        if (this.argumentsInfo != null) {
            for (Type arg : this.argumentsInfo) {
                builder.append(arg.getClassName());
            }
        }
        builder.append(")");
        return builder.toString();
    }
}
