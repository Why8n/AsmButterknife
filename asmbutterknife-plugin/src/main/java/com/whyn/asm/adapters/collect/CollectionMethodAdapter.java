package com.whyn.asm.adapters.collect;

import com.whyn.asm.ViewInjectAnalyse;
import com.whyn.asm.adapters.base.BaseMethodVisitor;
import com.whyn.asm.adapters.collect.annotation.CollectionAnnotationDelegateAdapter;
import com.whyn.asm.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.Log;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class CollectionMethodAdapter extends BaseMethodVisitor {
    private MethodBean mMethodBean;

    public CollectionMethodAdapter(MethodVisitor mv, MethodBean methodBean) {
        super(mv);
        this.mMethodBean = methodBean;
    }

    @Override
    public void visitParameter(String name, int access) {
        Log.v("%s:visitParameter:name=%s,access=%d", name, access);
        super.visitParameter(name, access);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Log.v("CollectionMethodAdapter:visitAnnotation desc=%s", desc);
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        AnnotationBean bean = new AnnotationBean(desc);
        this.mMethodBean.addAnnotation(bean);
        CollectionAnnotationDelegateAdapter delegateAdapter = new CollectionAnnotationDelegateAdapter(av, bean);
        if ((av = delegateAdapter.getVisitor(desc)) != null) {
            if (AsmUtils.isStatic(this.mMethodBean.access)) {
                String annotation = Type.getType(desc).getClassName();
                annotation = annotation.substring(annotation.lastIndexOf(".") + 1);
                throw new IllegalStateException(String.format("%s.%s() method with @%s must not be static",
                        Type.getObjectType(ViewInjectClassRecorder.getInstance().getInternalName()).getClassName(),
                        this.mMethodBean.methodName, annotation));
            }
            Log.v("add methodBean: %s", this.mMethodBean);
            ViewInjectClassRecorder.getInstance().addMethod(this.mMethodBean);
        }
        return av;
    }
}
