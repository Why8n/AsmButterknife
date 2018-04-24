package com.whyn.bean;

import com.google.common.collect.ImmutableSet;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.ClassBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.bean.interfaces.IRecordClass;
import com.whyn.utils.Utils;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

public class ViewInjectClassRecorder implements IRecordClass {
    private ClassBean classBean;

    private static final class SingletonHolder {
        private static final ViewInjectClassRecorder sInstance = new ViewInjectClassRecorder();
    }

    public static final ViewInjectClassRecorder getInstance() {
        return SingletonHolder.sInstance;
    }

    @Override
    public void recordClass(String clsName) {
        this.classBean = new ClassBean();
        this.classBean.recordClass(clsName);
    }

    @Override
    public String getInternalName() {
        return this.classBean.getInternalName();
    }

    @Override
    public boolean addMethod(MethodBean methodInfo) {
        this.check();
        return this.classBean.addMethod(methodInfo);
    }

    @Override
    public ImmutableSet<MethodBean> getMethod() {
        return this.classBean.getMethod();
    }

    @Override
    public boolean addAnnotation(AnnotationBean annotationInfo) {
        this.check();
        return this.classBean.addAnnotation(annotationInfo);
    }

    @Override
    public ImmutableSet<AnnotationBean> getAnnotation() {
        return this.classBean.getAnnotation();
    }

    @Override
    public boolean addField(FieldBean fieldInfo) {
        this.check();
        return this.classBean.addField(fieldInfo);
    }

    @Override
    public ImmutableSet<FieldBean> getField() {
        return this.classBean.getField();
    }

    private void check() {
        Utils.checkNotNull(this.classBean, "classRecorder is null,did you forget to call %s.recordClass",
                ViewInjectClassRecorder.class.getSimpleName());
    }

    public Tuple<MethodBean, AnnotationBean> getViewInjectDetail() {
        for (MethodBean methodBean : getMethod()) {
            for (AnnotationBean annotation : methodBean.getAnnotation()) {
                if (Type.getDescriptor(ViewInject.class).equals(annotation.typeDesc)) {
                    return new Tuple<>(methodBean, annotation);
                }
            }
        }
        return null;
    }

    public List<Tuple<FieldBean, AnnotationBean>> getBindViewDetail() {
        List<Tuple<FieldBean, AnnotationBean>> bindViewDetail = new ArrayList<>();
        for (FieldBean fieldBean : this.getField()) {
            for (AnnotationBean annotation : fieldBean.getAnnotation()) {
                if (Type.getDescriptor(BindView.class).equals(annotation.typeDesc)) {
                    bindViewDetail.add(new Tuple<>(fieldBean, annotation));
                }
            }
        }
        return bindViewDetail;
    }
}
