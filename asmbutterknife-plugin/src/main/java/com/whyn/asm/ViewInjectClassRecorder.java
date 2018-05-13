package com.whyn.asm;

import com.android.annotations.NonNull;
import com.google.common.collect.ImmutableSet;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.ClassBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.bean.interfaces.IRecordClass;
import com.whyn.utils.Utils;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.OnClick;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewInjectClassRecorder implements IRecordClass {
    private File mClassFile;
    private ClassBean mClassBean;
    //    @ViewInject(int)
    private Tuple<MethodBean, AnnotationBean> mViewInjectDetail;
    //    @BindView(int)
    private List<Tuple<FieldBean, AnnotationBean>> mBindViewDetail;
    //    @OnClick(int[])
    private List<Tuple<MethodBean, AnnotationBean>> mOnClickDetail;

    private static final class SingletonHolder {
        private static final ViewInjectClassRecorder sInstance = new ViewInjectClassRecorder();
    }

    private void reset() {
        this.mClassFile = null;
        this.mClassBean = null;
        this.mViewInjectDetail = null;
        this.mBindViewDetail = null;
        this.mOnClickDetail = null;
    }

    public static final ViewInjectClassRecorder getInstance() {
        return SingletonHolder.sInstance;
    }

    public void recordClassFile(@NonNull File file) {
        this.reset();
        this.mClassFile = file;
    }

    public File getClassFile() {
        return this.mClassFile;
    }

    @Override
    public void recordClass(int version, String clsName) {
        this.mClassBean = new ClassBean();
        this.mClassBean.recordClass(version, clsName);
    }

    @Override
    public int getVersion() {
        return this.mClassBean.getVersion();
    }

    @Override
    public String getInternalName() {
        return this.mClassBean.getInternalName();
    }

    @Override
    public boolean addMethod(MethodBean methodInfo) {
        this.check();
        return this.mClassBean.addMethod(methodInfo);
    }

    @Override
    public ImmutableSet<MethodBean> getMethod() {
        return this.mClassBean.getMethod();
    }

    @Override
    public boolean addAnnotation(AnnotationBean annotationInfo) {
        this.check();
        return this.mClassBean.addAnnotation(annotationInfo);
    }

    @Override
    public ImmutableSet<AnnotationBean> getAnnotation() {
        return this.mClassBean.getAnnotation();
    }

    @Override
    public boolean addField(FieldBean fieldInfo) {
        this.check();
        return this.mClassBean.addField(fieldInfo);
    }

    @Override
    public ImmutableSet<FieldBean> getField() {
        return this.mClassBean.getField();
    }

    @Override
    public boolean addInnerClass(InnerClassBean innerClassInfo) {
        return this.mClassBean.addInnerClass(innerClassInfo);
    }

    @Override
    public List<InnerClassBean> getInnerClass() {
        return this.mClassBean.getInnerClass();
    }

    private void check() {
        Utils.checkNotNull(this.mClassBean, "classRecorder is null,did you forget to call %s.recordClass",
                ViewInjectClassRecorder.class.getSimpleName());
    }

    Tuple<MethodBean, AnnotationBean> getViewInjectDetail() {
        if (this.mViewInjectDetail != null)
            return this.mViewInjectDetail;
        for (MethodBean methodBean : this.getMethod()) {
            for (AnnotationBean annotation : methodBean.getAnnotation()) {
                if (Type.getDescriptor(ViewInject.class).equals(annotation.typeDesc)) {
                    return this.mViewInjectDetail = new Tuple<>(methodBean, annotation);
                }
            }
        }
        return null;
    }

    List<Tuple<FieldBean, AnnotationBean>> getBindViewDetail() {
        if (this.mBindViewDetail == null) {
            this.mBindViewDetail = new ArrayList<>();
            for (FieldBean fieldBean : this.getField()) {
                for (AnnotationBean annotation : fieldBean.getAnnotation()) {
                    if (Type.getDescriptor(BindView.class).equals(annotation.typeDesc)) {
                        this.mBindViewDetail.add(new Tuple<>(fieldBean, annotation));
                    }
                }
            }
        }
        return this.mBindViewDetail;
    }

    List<Tuple<MethodBean, AnnotationBean>> getOnClickDetail() {
        if (this.mOnClickDetail == null) {
            this.mOnClickDetail = new ArrayList<>();
            for (MethodBean method : this.getMethod()) {
                for (AnnotationBean annotation : method.getAnnotation()) {
                    if (Type.getDescriptor(OnClick.class).equals(annotation.typeDesc)) {
                        this.mOnClickDetail.add(new Tuple<>(method, annotation));
                    }
                }
            }
        }
        return this.mOnClickDetail;
    }

}
