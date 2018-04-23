package com.whyn.asm.adapters.inject;

import android.support.annotation.NonNull;

import com.whyn.asm.adapters.base.BaseClassVisitor;
import com.whyn.asm.adapters.bindview.BindViewFieldAdapter;
import com.whyn.bean.BindViewBean;
import com.whyn.bean.ViewInjectBean;
import com.whyn.utils.Log;
import com.yn.annotations.ViewInject;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class ViewInjectClassAdapter extends BaseClassVisitor {
    private ViewInjectBean mViewInjectBean;

    public ViewInjectClassAdapter(ClassVisitor cv, ViewInjectBean viewInjectBean) {
        super(cv);
        this.mViewInjectBean = viewInjectBean;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Log.v("----------------------------------------");
        Log.v("ViewInjectClassAdapter:start to inject view: %s", name);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
//        private String str;
//        access=2,name=str,descriptor=Ljava/lang/String;,signature=null,value=null
        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
        return new BindViewFieldAdapter(fv, name, desc, this.mViewInjectBean);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Log.v("visitMethod:name=%s,desc=%s", name, desc);
//        visitMethod:access=1,name=<init>,descriptor=()V,signature=null,exception=null
//        visitMethod:access=9,name=main,descriptor=([Ljava/lang/String;)V,signature=null,exception=[Ljava.lang.String;@3764951d])
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return filterMethod(mv, name, desc);
    }

    private MethodVisitor filterMethod(MethodVisitor mv, String methodName, String methodDesc) {
        MethodVisitor transMethodVisitor = mv;
        switch (this.mViewInjectBean.getWhichView()) {
            case ViewInject.ACTIVITY:
                if (checkFitsActivity(methodName, methodDesc))
                    transMethodVisitor = new InjectActivityMethodAdapter(mv, this.mViewInjectBean);
                break;
            case ViewInject.DIALOG:
                break;
            case ViewInject.FRAGMENT:
                break;
            case ViewInject.VIEWHOLDER:
                break;
            default:
                break;
        }
        return transMethodVisitor;
    }

    private boolean checkFitsActivity(@NonNull String methodName, @NonNull String methodDesc) {
        return "onCreate".equals(methodName) && "(Landroid/os/Bundle;)V".equals(methodDesc);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        Log.v("ViewInjectClassAdapter:visitEnd");
        for (BindViewBean bean : this.mViewInjectBean.getBindViewBeans()) {
            Log.v(bean.toString());
        }
    }
}
