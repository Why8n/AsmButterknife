package com.whyn.asm.adapters.collect.annotation;

import com.whyn.bean.element.AnnotationBean;
import com.whyn.utils.Utils;

import org.objectweb.asm.AnnotationVisitor;

import java.util.ArrayList;
import java.util.List;

public class CollectionAnnotationDelegateAdapter {

    private List<BaseCollectionAnnotationAdapter<?>> mAnnotationAdapters = new ArrayList<>();

    public CollectionAnnotationDelegateAdapter(AnnotationVisitor av, AnnotationBean annotationBean) {
        this.mAnnotationAdapters.add(new CollectionViewInjectAnnotationAdapter(av, annotationBean));
        this.mAnnotationAdapters.add(new CollectionOnClickAnnotationAdapter(av, annotationBean));
    }

    public AnnotationVisitor getVisitor(String desc) {
        Utils.checkNotNull(desc, "annottion descriptor must not be null");
        for (BaseCollectionAnnotationAdapter<?> adapter : this.mAnnotationAdapters) {
            if (adapter.desc().equals(desc)) {
                return adapter;
            }
        }
        return null;
    }
}
