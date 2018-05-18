package com.whyn.asm.adapters.collect;

import com.whyn.asm.adapters.base.BaseFieldVisitor;
import com.whyn.asm.recorders.interfaces.impl.ViewInjectCollector;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;

public class CollectionFieldAdapter extends BaseFieldVisitor {
    private FieldBean mFieldBean;

    public CollectionFieldAdapter(FieldVisitor fv, FieldBean fieldBean) {
        super(fv);
        this.mFieldBean = fieldBean;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        AnnotationBean bean = new AnnotationBean(desc);
        this.mFieldBean.addAnnotation(bean);
        return new CollectionAnnotationAdapter(av, bean);
    }

    @Override
    public void visitEnd() {
        ViewInjectCollector.getInstance().visitField(this.mFieldBean);
    }
}
