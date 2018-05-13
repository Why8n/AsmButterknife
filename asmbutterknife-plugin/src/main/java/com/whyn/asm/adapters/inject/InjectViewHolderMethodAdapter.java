package com.whyn.asm.adapters.inject;

import com.android.annotations.NonNull;
import com.whyn.asm.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.bean.Tuple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;


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
    protected void injectFindViewById() {
        check();
        String owner = this.mInjectMethodDetail.getArgument()[0].getInternalName();
        AsmUtils.injectFindViewById(this.mv, owner);
//        List<Tuple<FieldBean, AnnotationBean>> bindViewFields = ViewInjectClassRecorder.getInstance().getBindViewDetail();
//        for (Tuple<FieldBean, AnnotationBean> field : bindViewFields) {
//            injectFindViewById(field.first, field.second, owner);
//        }
    }

    private void check() {
        Type[] args = this.mInjectMethodDetail.getArgument();
        if (args == null || args.length <= 0 || args[0].getSort() != Type.OBJECT)
            throw new IllegalArgumentException(String.format("%s.%s() method must take a View type as it's first parameter",
                    Type.getObjectType(ViewInjectClassRecorder.getInstance().getInternalName()).getClassName(), this.mInjectMethodDetail.methodName));
    }

    @Override
    protected void injectOnClick() {
        AsmUtils.injectOnClick(this.mv);
    }

    private void injectFindViewById(FieldBean field, AnnotationBean annotation, String owner) {
        Integer id = (Integer) annotation.getValue();
        if (id == null || id.intValue() < 0)
            return;
        this.mv.visitVarInsn(ALOAD, 0);
        this.mv.visitVarInsn(ALOAD, 1);
        this.mv.visitLdcInsn(id);
//        this.mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "findViewById", "(I)Landroid/view/View;", false);
//        this.mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
//        this.mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/adapter/RecyclerAdapter$ViewHolder", "tv", "Landroid/widget/TextView;");
        this.mv.visitMethodInsn(INVOKEVIRTUAL, owner, "findViewById", "(I)Landroid/view/View;", false);
        this.mv.visitTypeInsn(CHECKCAST, Type.getType(field.desc).getInternalName());
        this.mv.visitFieldInsn(PUTFIELD,
                ViewInjectClassRecorder.getInstance().getInternalName(),
                field.name,
                field.desc);
    }
}
