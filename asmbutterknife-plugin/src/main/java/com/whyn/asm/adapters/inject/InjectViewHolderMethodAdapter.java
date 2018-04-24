package com.whyn.asm.adapters.inject;

import com.android.annotations.NonNull;
import com.whyn.bean.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.Log;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.List;

public class InjectViewHolderMethodAdapter extends MethodViewInjection {

    private MethodBean mInjectMethodDetail;
    private AnnotationBean mInjectAnnotationDetail;

    protected InjectViewHolderMethodAdapter(MethodVisitor mv, int access, String name, String desc, int viewInjectType,
                                            @NonNull Tuple<MethodBean, AnnotationBean> viewInjectDeatil) {
        super(mv, access, name, desc, viewInjectType);
        this.mInjectMethodDetail = viewInjectDeatil.first;
        this.mInjectAnnotationDetail = viewInjectDeatil.second;
    }

    @Override
    protected String dstInjectMethodName() {
        return this.mInjectMethodDetail.methodName;
    }

    @Override
    protected String dstInjectMethodDesc() {
        return this.mInjectMethodDetail.methodDesc;
    }

    @Override
    protected void inject() {
        injectView();
    }

    private void injectView() {
        List<Tuple<FieldBean, AnnotationBean>> bindViewFields = ViewInjectClassRecorder.getInstance().getBindViewDetail();
        Log.v("injectView:size=%d", bindViewFields.size());
        for (Tuple<FieldBean, AnnotationBean> field : bindViewFields) {
            injectFindViewById(field.first, field.second);
        }
    }

    private void injectFindViewById(FieldBean field, AnnotationBean annotation) {
        Integer id = (Integer) annotation.getValue();
        if (id == null || id.intValue() < 0)
            return;
        this.mv.visitVarInsn(ALOAD, 0);
        this.mv.visitVarInsn(ALOAD, 1);
        this.mv.visitLdcInsn(id);
//        this.mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "findViewById", "(I)Landroid/view/View;", false);
//        this.mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
//        this.mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/adapter/RecyclerAdapter$ViewHolder", "tv", "Landroid/widget/TextView;");
        Type[] args = this.mInjectMethodDetail.getArgument();
        if (args == null || args.length <= 0)
            throw new IllegalArgumentException(String.format("%s.%s() method must take a View type as it's first parameter",
                    Type.getObjectType(ViewInjectClassRecorder.getInstance().getInternalName()).getClassName(), this.mInjectMethodDetail.methodName));
        //if it is primitive type,NullPointException will occured
        String owner = args[0].getInternalName();
        this.mv.visitMethodInsn(INVOKEVIRTUAL, owner, "findViewById", "(I)Landroid/view/View;", false);
        this.mv.visitTypeInsn(CHECKCAST, Type.getType(field.desc).getInternalName());
        this.mv.visitFieldInsn(PUTFIELD,
                ViewInjectClassRecorder.getInstance().getInternalName(),
                field.name,
                field.desc);
    }
}
