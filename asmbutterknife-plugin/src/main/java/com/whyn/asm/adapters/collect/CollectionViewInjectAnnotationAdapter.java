package com.whyn.asm.adapters.collect;

import com.android.annotations.NonNull;
import com.whyn.asm.adapters.base.BaseAnnotationVisitor;
import com.whyn.bean.ViewInjectBean;
import com.whyn.utils.Log;
import com.yn.annotations.ViewInject;

import org.objectweb.asm.AnnotationVisitor;

/*
Notice: if annotation using default value,visit() method will not be triggered
 */
public class CollectionViewInjectAnnotationAdapter extends BaseAnnotationVisitor {
    private ViewInjectBean mViewInjectBean;
    private boolean mIsAnnotatedWithDefaultValue = true;

    public CollectionViewInjectAnnotationAdapter(AnnotationVisitor av, @NonNull ViewInjectBean viewInjectBean) {
        super(av);
        this.mViewInjectBean = viewInjectBean;
    }

    @Override
    public void visit(String name, Object value) {
        Log.v("collecationViewInjectAnnotationAdapter:found @%s: %s=%d", ViewInject.class.getSimpleName(), name, (int) value);
        this.mIsAnnotatedWithDefaultValue = false;
        this.mViewInjectBean.setWhichView((int) value);
    }

    @Override
    public void visitEnd() {
        Log.v("visitEnd");
        if (this.mIsAnnotatedWithDefaultValue)
            this.mViewInjectBean.setWhichView(ViewInject.ACTIVITY);
    }
}
