package com.whyn.asm.adapters.collect;

import com.whyn.asm.adapters.base.BaseMethodVisitor;
import com.whyn.asm.recorders.interfaces.impl.ViewInjectCollector;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.MethodBean;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;

public class CollectionMethodAdapter extends BaseMethodVisitor {
    private MethodBean mMethodBean;

    public CollectionMethodAdapter(MethodVisitor mv, MethodBean methodBean) {
        super(mv);
        this.mMethodBean = methodBean;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        AnnotationBean bean = new AnnotationBean(desc);
        this.mMethodBean.addAnnotation(bean);
        return new CollectionAnnotationAdapter(av, bean);
    }

    @Override
    public void visitEnd() {
        ViewInjectCollector.getInstance().visitMethod(this.mMethodBean);
    }
}
