package com.whyn.asm.adapters.bindview;

import com.whyn.asm.adapters.BindViewAnnotationAdapter;
import com.whyn.bean.BindViewBean;
import com.whyn.bean.ViewInjectBean;
import com.whyn.define.Const;
import com.whyn.utils.Log;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;

import static com.whyn.define.Const.ASM_API;
import static com.whyn.define.Const.Annotation.BINDVIEW_DESC;

public class BindViewFieldAdapter extends FieldVisitor {
    private String mName;
    private String mDesc;
    private ViewInjectBean mViewInjectBean;
    private BindViewBean mBindViewBean;

    public BindViewFieldAdapter(FieldVisitor fv, String name, String desc, ViewInjectBean viewInjectBean) {
        super(ASM_API, fv);
        this.mName = name;
        this.mDesc = desc;
        this.mViewInjectBean = viewInjectBean;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor av = super.visitAnnotation(desc, visible);
        if (BINDVIEW_DESC.equals(desc)) {
            Log.v("fieldName=%s", this.mName);
            if (this.mBindViewBean == null)
                this.mBindViewBean = new BindViewBean();
            this.mBindViewBean.setName(this.mName);
            this.mBindViewBean.setDesc(this.mDesc);
            av = new BindViewAnnotationAdapter(av, this.mBindViewBean);
        }
        return av;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (this.mBindViewBean != null)
            this.mViewInjectBean.addBindViewBean(this.mBindViewBean);
    }
}
