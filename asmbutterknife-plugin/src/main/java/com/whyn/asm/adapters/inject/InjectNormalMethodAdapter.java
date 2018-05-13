package com.whyn.asm.adapters.inject;

import com.android.annotations.NonNull;
import com.whyn.asm.ViewInjectAnalyse;
import com.whyn.asm.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.Log;
import com.whyn.utils.bean.Tuple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.List;

public class InjectNormalMethodAdapter extends MethodViewInjection {


    private MethodBean mMethodDetail;
    private AnnotationBean mAnnotationDetail;

    protected InjectNormalMethodAdapter(MethodVisitor mv, int access, String name, String desc, int viewInjectType,
                                        @NonNull Tuple<MethodBean, AnnotationBean> viewInjectDeatil) {
        super(mv, access, name, desc, viewInjectType);
        this.mMethodDetail = viewInjectDeatil.first;
        this.mAnnotationDetail = viewInjectDeatil.second;
    }

    @Override
    protected String dstInjectMethodName() {
        return this.mMethodDetail.methodName;
    }

    @Override
    protected String dstInjectMethodDesc() {
        return this.mMethodDetail.methodDesc;
    }

    @Override
    protected void injectFindViewById() {

    }

    @Override
    protected void injectOnClick() {

    }

    protected void inject() {
        injectView();
    }

    private void injectView() {
        List<Tuple<FieldBean, AnnotationBean>> bindViewFields = ViewInjectAnalyse.getBindViewDetail();
        Log.v("injectView:size=%d", bindViewFields.size());
        for (Tuple<FieldBean, AnnotationBean> field : bindViewFields) {
            injectFindViewById(field.first, field.second);
        }
    }

    private void injectFindViewById(FieldBean field, AnnotationBean annotation) {
        Integer id = (Integer) annotation.getValue();
        Log.v("normal--> injectFindViewById: id=%d", id);
        if (id == null || id < 0)
            return;
        this.mv.visitVarInsn(ALOAD, 0);
        this.mv.visitVarInsn(ALOAD, 1);
        this.mv.visitLdcInsn(id);
//        this.mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "findViewById", "(I)Landroid/view/View;", false);
//        this.mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
//        this.mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/adapter/RecyclerAdapter$ViewHolder", "tv", "Landroid/widget/TextView;");
        this.mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "findViewById", "(I)Landroid/view/View;", false);
        this.mv.visitTypeInsn(CHECKCAST, Type.getType(field.desc).getInternalName());
        this.mv.visitFieldInsn(PUTFIELD,
                ViewInjectClassRecorder.getInstance().getInternalName(),
                field.name,
                field.desc);
    }
}
