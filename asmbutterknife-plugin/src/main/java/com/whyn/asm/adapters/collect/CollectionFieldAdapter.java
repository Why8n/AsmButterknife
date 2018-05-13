package com.whyn.asm.adapters.collect;

import com.whyn.asm.adapters.base.BaseAnnotationVisitor;
import com.whyn.asm.adapters.base.BaseFieldVisitor;
import com.whyn.asm.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.yn.asmbutterknife.annotations.BindView;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

public class CollectionFieldAdapter extends BaseFieldVisitor {
    private FieldBean mFieldBean;

    public CollectionFieldAdapter(FieldVisitor fv, FieldBean fieldBean) {
        super(fv);
        this.mFieldBean = fieldBean;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        if (Type.getDescriptor(BindView.class).equals(desc)) {
            AnnotationBean bean = new AnnotationBean(desc);
            this.mFieldBean.addAnnotation(bean);
            ViewInjectClassRecorder.getInstance().addField(this.mFieldBean);
            return new CollectionBindViewAnnotationAdapter(av, bean);
        }
        return null;
    }

    private class CollectionBindViewAnnotationAdapter extends BaseAnnotationVisitor {
        //        private BindViewBean mBindViewBean = new BindViewBean();
        private AnnotationBean mAnnotationBean;

        public CollectionBindViewAnnotationAdapter(AnnotationVisitor av, AnnotationBean annotationBean) {
            super(av);
            this.mAnnotationBean = annotationBean;
        }

        @Override
        public void visit(String name, Object value) {
            this.mAnnotationBean.addMethdValue(name, value);
        }
    }
}
