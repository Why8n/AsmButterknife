package com.whyn.asm.adapters.collect.annotation;

import com.whyn.asm.adapters.base.BaseAnnotationVisitor;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.utils.Log;

import org.objectweb.asm.AnnotationVisitor;

/*
Notice: if annotation using default value,visit() method will not be triggered
 */
public abstract class BaseCollectionAnnotationAdapter<T> extends BaseAnnotationVisitor {
    protected boolean mIsAnnotatedWithDefaultValue = true;
    private AnnotationBean mAnnotationBean;

    public BaseCollectionAnnotationAdapter(AnnotationVisitor av, AnnotationBean annotationBean) {
        super(av);
        this.mAnnotationBean = annotationBean;
    }

    @Override
    public void visit(String name, Object value) {
        Log.v("collecation AnnotationAdapter:found @%s: %s=%s", this.desc(), name, value);
        this.mIsAnnotatedWithDefaultValue = false;
        this.mAnnotationBean.addMethdValue(name, value);
    }

    @Override
    public void visitEnd() {
        Log.v("visitEnd");
        if (this.mIsAnnotatedWithDefaultValue)
            this.mAnnotationBean.addMethdValue("value", this.defaultValue());
    }

    public abstract String desc();

    abstract T defaultValue();

}
