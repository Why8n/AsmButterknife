package com.whyn.asm.adapters.inject;

import com.whyn.bean.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.Log;
import com.whyn.utils.bean.Tuple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.List;

public class InjectActivityMethodAdapter extends MethodViewInjection {

    //    private ViewInjectBean mViewInjectBean;
//    private MethodBean mMethodBean;
//    private AnnotationBean mAnnotationBean;

    public InjectActivityMethodAdapter(MethodVisitor mv, int access, String name, String desc, int viewInjectType) {
        super(mv, access, name, desc, viewInjectType);
//        this.mViewInjectBean = viewInjectBean;
//        this.mMethodBean = viewInjectDeatil.first;
//        this.mAnnotationBean = viewInjectDeatil.second;
    }

//    @Override
//    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
//        super.visitMethodInsn(opcode, owner, name, desc, itf);
//        if ("setContentView".equals(name) && "(I)V".equals(desc)) {
//            Log.v("enter setContentView,begin to inject findViewById");
////            Set<BindViewBean> beans = this.mViewInjectBean.getBindViewBeans();
//            List<Tuple<FieldBean, AnnotationBean>> bindViewDetail = ViewInjectClassRecorder.getInstance().getBindViewDetail();
//            for (Tuple<FieldBean, AnnotationBean> detail : bindViewDetail) {
//                FieldBean field = detail.first;
//                AnnotationBean annotation = detail.second;
//                int id = (int) annotation.getValue();
//                if (id == -1)
//                    continue;
//                mv.visitVarInsn(ALOAD, 0);
//                mv.visitVarInsn(ALOAD, 0);
//                mv.visitLdcInsn(new Integer(id));
////                mv.visitMethodInsn(INVOKEVIRTUAL, "com/yn/asmbutterknife/TestActivity", "findViewById", "(I)Landroid/view/View;", false);
//                String ownerInternalName = ViewInjectClassRecorder.getInstance().getInternalName();
//                mv.visitMethodInsn(INVOKEVIRTUAL,
//                        ownerInternalName,
//                        "findViewById",
//                        "(I)Landroid/view/View;",
//                        false);
////                mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
//                mv.visitTypeInsn(CHECKCAST, Type.getType(field.desc).getInternalName());
////                mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/TestActivity", "tv", "Landroid/widget/TextView;");
//                mv.visitFieldInsn(PUTFIELD, ownerInternalName, field.name, field.desc);
//
//            }
//
//        }
//    }

    @Override
    protected String dstInjectMethodName() {
        return "onCreate";
    }

    @Override
    protected String dstInjectMethodDesc() {
        return "(Landroid/os/Bundle;)V";
    }

    @Override
    protected void inject() {
        Log.v("enter setContentView,begin to inject findViewById");
//            Set<BindViewBean> beans = this.mViewInjectBean.getBindViewBeans();
        List<Tuple<FieldBean, AnnotationBean>> bindViewDetail = ViewInjectClassRecorder.getInstance().getBindViewDetail();
        for (Tuple<FieldBean, AnnotationBean> detail : bindViewDetail) {
            FieldBean field = detail.first;
            AnnotationBean annotation = detail.second;
            int id = (int) annotation.getValue();
            if (id == -1)
                continue;
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitLdcInsn(new Integer(id));
//                mv.visitMethodInsn(INVOKEVIRTUAL, "com/yn/asmbutterknife/TestActivity", "findViewById", "(I)Landroid/view/View;", false);
            String ownerInternalName = ViewInjectClassRecorder.getInstance().getInternalName();
            mv.visitMethodInsn(INVOKEVIRTUAL,
                    ownerInternalName,
                    "findViewById",
                    "(I)Landroid/view/View;",
                    false);
//                mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
            mv.visitTypeInsn(CHECKCAST, Type.getType(field.desc).getInternalName());
//                mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/TestActivity", "tv", "Landroid/widget/TextView;");
            mv.visitFieldInsn(PUTFIELD, ownerInternalName, field.name, field.desc);

        }
    }
}
