package com.whyn.asm.adapters.collect.annotation;

import com.whyn.bean.element.AnnotationBean;
import com.whyn.utils.Utils;

import org.objectweb.asm.AnnotationVisitor;

import java.util.ArrayList;
import java.util.List;

public class CollectionAnnotationDelegateAdapter {
    private List<BaseCollectionAnnotationAdapter<?>> mAnnotationAdapters = new ArrayList<>();
//    private String mMethodName;
//    private String mMethodDescriptor;
//    private ViewInjectBean mViewInjectBean;

    //    public CollectionAnnotationDelegateAdapter(AnnotationVisitor av, String methodName, String methodDesc, ViewInjectBean viewInjectBean) {
//        this.mMethodName = methodName;
//        this.mMethodDescriptor = methodDesc;
//        this.mViewInjectBean = viewInjectBean;
//        this.mAnnotationAdapters.add(new CollectionViewInjectAnnotationAdapter(av, viewInjectBean));
//        this.mAnnotationAdapters.add(new CollectionOnClickAnnotationAdapter(av, viewInjectBean));
//    }

    private AnnotationBean mAnnotationBean;

    public CollectionAnnotationDelegateAdapter(AnnotationVisitor av,  AnnotationBean annotationBean) {
        this.mAnnotationBean = annotationBean;
        this.mAnnotationAdapters.add(new CollectionViewInjectAnnotationAdapter(av,this.mAnnotationBean));
        this.mAnnotationAdapters.add(new CollectionOnClickAnnotationAdapter(av,this.mAnnotationBean));
    }

    public AnnotationVisitor getVisitor(String desc) {
        Utils.checkNotNull(desc, "annottion descriptor must not be null");
        for (BaseCollectionAnnotationAdapter<?> adapter : this.mAnnotationAdapters) {
            if (adapter.desc().equals(desc)) {
//                MethodBean bean = new MethodBean(desc, this.mMethodName, this.mMethodDescriptor);
//                this.mViewInjectBean.addMethodInfo(bean);
                return adapter;
            }
        }
        return null;
    }
}
