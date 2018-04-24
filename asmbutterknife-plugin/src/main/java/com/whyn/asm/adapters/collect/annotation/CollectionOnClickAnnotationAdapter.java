package com.whyn.asm.adapters.collect.annotation;

import com.whyn.bean.element.AnnotationBean;
import com.yn.asmbutterknife.annotations.OnClick;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

public class CollectionOnClickAnnotationAdapter extends BaseCollectionAnnotationAdapter<int[]> {
    public CollectionOnClickAnnotationAdapter(AnnotationVisitor av, AnnotationBean annotationBean) {
        super(av, annotationBean);
    }

    @Override
    public String desc() {
        return Type.getDescriptor(OnClick.class);
    }

    @Override
    int[] defaultValue() {
        return new int[]{};
    }

}
