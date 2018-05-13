package com.whyn.asm.adapters.inject;

import android.support.annotation.NonNull;

import com.whyn.asm.ViewInjectAnalyse;
import com.whyn.asm.adapters.base.BaseClassVisitor;
import com.whyn.asm.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.Log;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class ViewInjectClassAdapter extends BaseClassVisitor {
    public ViewInjectClassAdapter(ClassVisitor cv) {
        super(cv);
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
//        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
//        return new BindViewFieldAdapter(fv, name, desc);
        return super.visitField(access, name, desc, signature, value);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Log.v("visitMethod:name=%s,desc=%s", name, desc);
//        visitMethod:access=1,name=<init>,descriptor=()V,signature=null,exception=null
//        visitMethod:access=9,name=main,descriptor=([Ljava/lang/String;)V,signature=null,exception=[Ljava.lang.String;@3764951d])
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return filterMethod(mv, access, name, desc);
    }

    private MethodVisitor filterMethod(MethodVisitor mv, int access, String methodName, String methodDesc) {
        Tuple<MethodBean, AnnotationBean> viewInjectDetail = ViewInjectAnalyse.getViewInjectDetail();
        if (viewInjectDetail == null)
            return mv;
        Object value = viewInjectDetail.second.getValue();
        Log.v("filterMethod:ViewInjectType annotationValue=%d", (int) value);
        return MethodViewInjectionDelegate.obtainVisitor(mv, access, methodName, methodDesc,
                (value == null ? ViewInject.NONE : (int) value), viewInjectDetail);
    }

    @Override
    public void visitEnd() {
        int order = injectOnClick(ViewInjectAnalyse.howManyAccessMethod());
//        order = injectOnItemClick(order);
        super.visitEnd();
        Log.v("ViewInjectClassAdapter:visitEnd");
    }

    private int injectOnClick(int order) {
        List<Tuple<MethodBean, AnnotationBean>> onClickDetail = ViewInjectAnalyse.getOnClickDetail();
        Log.v("original access method num: %d,onClickSize; %d", order, onClickDetail.size());
        for (Tuple<MethodBean, AnnotationBean> onClick : onClickDetail) {
            MethodBean method = onClick.first;
            if (method != null && (method.access & Opcodes.ACC_PRIVATE) != 0) {
                injectOnClick4PrivateMethod(this.cv, order++,
                        ViewInjectClassRecorder.getInstance().getInternalName(),
                        method);
            }
        }
        return order;
    }

    //inject access$xx method for View.OnClickListener(){} anonymous inner class to visit outer
    private void injectOnClick4PrivateMethod(@NonNull ClassVisitor cv, int order,
                                             String outerClsInternalName, MethodBean methodDetail) {
        AsmUtils.injectMethodAccess(cv, order, outerClsInternalName, methodDetail);
    }
}
