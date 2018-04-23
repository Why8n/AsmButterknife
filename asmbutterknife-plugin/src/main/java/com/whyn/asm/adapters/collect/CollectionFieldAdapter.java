package com.whyn.asm.adapters.collect;

import com.android.annotations.NonNull;
import com.whyn.asm.adapters.base.BaseAnnotationVisitor;
import com.whyn.asm.adapters.base.BaseFieldVisitor;
import com.whyn.bean.BindViewBean;
import com.whyn.bean.ViewInjectBean;
import com.whyn.utils.Log;
import com.yn.annotations.BindView;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

public class CollectionFieldAdapter extends BaseFieldVisitor {
    private ViewInjectBean mViewInjectBean;
    private String mFieldName; //tv
    private String mFieldDesc; //Landroid.widget.TextView;

    public CollectionFieldAdapter(FieldVisitor fv, String name, String desc,
                                  @NonNull ViewInjectBean viewInjectBean) {
        super(fv);
        this.mFieldName = name;
        this.mFieldDesc = desc;
        this.mViewInjectBean = viewInjectBean;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        if (Type.getDescriptor(BindView.class).equals(desc))
            return new CollectionBindViewAnnotationAdapter(av);
        return null;
    }

    private class CollectionBindViewAnnotationAdapter extends BaseAnnotationVisitor {
        private BindViewBean mBindViewBean = new BindViewBean();

        public CollectionBindViewAnnotationAdapter(AnnotationVisitor av) {
            super(av);
        }

        @Override
        public void visit(String name, Object value) {
            this.mBindViewBean.setName(CollectionFieldAdapter.this.mFieldName);
            this.mBindViewBean.setDesc(CollectionFieldAdapter.this.mFieldDesc);
            this.mBindViewBean.setId((int) value);
            CollectionFieldAdapter.this.mViewInjectBean.addBindViewBean(this.mBindViewBean);
            Log.v("found %s", this.mBindViewBean);
        }
    }
}
