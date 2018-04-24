package com.whyn.asm.adapters.collect.annotation;

import com.whyn.bean.element.AnnotationBean;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

public class CollectionViewInjectAnnotationAdapter extends BaseCollectionAnnotationAdapter<Integer> {
    public CollectionViewInjectAnnotationAdapter(AnnotationVisitor av, AnnotationBean annotationBean) {
        super(av, annotationBean);
    }

    @Override
    public String desc() {
        return Type.getDescriptor(ViewInject.class);
    }

    @Override
    Integer defaultValue() {
        return ViewInject.VIEWHOLDER;
    }
}
