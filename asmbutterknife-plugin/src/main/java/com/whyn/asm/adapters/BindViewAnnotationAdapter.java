package com.whyn.asm.adapters;

import com.whyn.bean.BindViewBean;
import com.whyn.define.Const;
import com.whyn.utils.Log;

import org.objectweb.asm.AnnotationVisitor;

import java.util.Arrays;

public class BindViewAnnotationAdapter extends AnnotationVisitor {
    private BindViewBean mBindViewBean;

    public BindViewAnnotationAdapter(AnnotationVisitor av, BindViewBean bindViewBean) {
        super(Const.ASM_API, av);
        this.mBindViewBean = bindViewBean;
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
        this.mBindViewBean.setId((int) value);
        Log.v("BindViewAnnotationAdapter:visit:name=%s,value=%d", name, (int) value);
    }
}
