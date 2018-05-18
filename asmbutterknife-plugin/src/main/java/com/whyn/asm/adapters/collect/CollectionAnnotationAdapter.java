package com.whyn.asm.adapters.collect;

import com.whyn.asm.adapters.base.BaseAnnotationVisitor;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.utils.AsmUtils;

import org.objectweb.asm.AnnotationVisitor;

class CollectionAnnotationAdapter extends BaseAnnotationVisitor {
    private AnnotationBean mAnnotationBean;

    CollectionAnnotationAdapter(AnnotationVisitor av, AnnotationBean annotationBean) {
        super(av);
        this.mAnnotationBean = annotationBean;
    }

    @Override
    public void visit(String name, Object value) {
        this.mAnnotationBean.addMethdValue(name, value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        String enumClass = AsmUtils.desc2Class(desc);
        this.mAnnotationBean.addMethdValue(name, enumClass + "." + value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        AnnotationBean bean = new AnnotationBean(desc);
        this.mAnnotationBean.addAnnotatedAnnotation(bean);
        return new CollectionAnnotationAdapter(super.visitAnnotation(name, desc), bean);
    }


    @Override
    public void visitEnd() {
    }
}
