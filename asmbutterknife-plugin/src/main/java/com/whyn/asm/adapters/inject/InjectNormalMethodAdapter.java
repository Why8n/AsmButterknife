package com.whyn.asm.adapters.inject;

import com.whyn.bean.Tuple;
import com.whyn.bean.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.define.Const;
import com.whyn.utils.Log;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.List;

public class InjectNormalMethodAdapter extends AdviceAdapter {
    private boolean mIsMethodWanted;

    protected InjectNormalMethodAdapter(MethodVisitor mv, int access, String name, String desc,
                                        Tuple<MethodBean, AnnotationBean> viewInjectDeatil) {
        super(Const.ASM_API, mv, access, name, desc);
        MethodBean methodBean = viewInjectDeatil.first;
        this.mIsMethodWanted = (methodBean.methodName.equals(name) && methodBean.methodDesc.equals(desc));
        Log.v("if injectNoraml:methodName=%s,methodDesc=%s,isInject=%b",name,desc,mIsMethodWanted);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        Log.v("onMethodEnter");
        if (this.mIsMethodWanted) {
            injectView();
        }
    }

    private void injectView() {
        List<Tuple<FieldBean, AnnotationBean>> bindViewFields = ViewInjectClassRecorder.getInstance().getBindViewDetail();
        Log.v("injectView:size=%d",bindViewFields.size());
        for (Tuple<FieldBean, AnnotationBean> field : bindViewFields) {
            injectFindViewById(field.first, field.second);
        }
    }

    private void injectFindViewById(FieldBean field, AnnotationBean annotation) {
        Integer id = (Integer) annotation.getValue();
        Log.v("normal--> injectFindViewById: id=%d",id);
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
