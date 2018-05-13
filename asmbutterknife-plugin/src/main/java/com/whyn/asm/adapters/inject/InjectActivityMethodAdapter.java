package com.whyn.asm.adapters.inject;

import com.whyn.asm.ViewInjectAnalyse;
import com.whyn.asm.ViewInjectClassRecorder;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.Log;
import com.whyn.utils.bean.Tuple;

import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class InjectActivityMethodAdapter extends MethodViewInjection {

    public InjectActivityMethodAdapter(MethodVisitor mv, int access, String name, String desc, int viewInjectType) {
        super(mv, access, name, desc, viewInjectType);
    }

    @Override
    protected String dstInjectMethodName() {
        return "onCreate";
    }

    @Override
    protected String dstInjectMethodDesc() {
        return "(Landroid/os/Bundle;)V";
    }

    @Override
    protected void injectFindViewById() {
        Log.v("enter setContentView,begin to inject findViewById");
        AsmUtils.injectFindViewById(this.mv,
                ViewInjectClassRecorder.getInstance().getInternalName());

//            Set<BindViewBean> beans = this.mViewInjectBean.getBindViewBeans();
//        List<Tuple<FieldBean, AnnotationBean>> bindViewDetail = ViewInjectClassRecorder.getInstance().getBindViewDetail();
//        for (Tuple<FieldBean, AnnotationBean> detail : bindViewDetail) {
//            FieldBean field = detail.first;
//            AnnotationBean annotation = detail.second;
//            Integer id = (Integer) annotation.getValue();
//            if (id == null || id.intValue() < 0)
//                continue;
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitLdcInsn(id);
//                mv.visitMethodInsn(INVOKEVIRTUAL, "com/yn/asmbutterknife/TestActivity", "findViewById", "(I)Landroid/view/View;", false);
//            String ownerInternalName = ViewInjectClassRecorder.getInstance().getInternalName();
//            mv.visitMethodInsn(INVOKEVIRTUAL,
//                    ownerInternalName,
//                    "findViewById",
//                    "(I)Landroid/view/View;",
//                    false);
//                mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
//            mv.visitTypeInsn(CHECKCAST, Type.getType(field.desc).getInternalName());
//                mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/TestActivity", "tv", "Landroid/widget/TextView;");
//            mv.visitFieldInsn(PUTFIELD, ownerInternalName, field.name, field.desc);
//        }
    }

    @Override
    protected void injectOnClick() {
        AsmUtils.injectOnClick(this.mv);
//        Map<MethodBean, String> methodAccessMap = new HashMap<>();
//        int order = ViewInjectAnalyse.howManyAccessMethod();
//        List<Tuple<MethodBean, AnnotationBean>> onClickDetail = ViewInjectAnalyse.getOnClickDetail();
//        for (Tuple<MethodBean, AnnotationBean> onClick : onClickDetail) {
//            MethodBean method = onClick.first;
//            if (method != null && (method.access & Opcodes.ACC_PRIVATE) != 0) {
//                methodAccessMap.put(method, String.format("access$%3d", order++));
//            }
//        }
//
//        for (Tuple<MethodBean, AnnotationBean> onClick : onClickDetail) {
//            MethodBean method = onClick.first;
//            AnnotationBean annotation = onClick.second;
//            if (method == null || annotation == null)
//                continue;
//            int[] ids = (int[]) annotation.getValue();
//            long innerAnonymousClsCount = ViewInjectAnalyse.howManyAnonymousInnerClass();
//            for (int id : ids) {
//                String accessMethodName = methodAccessMap.get(method);
//                AsmUtils.createAnonymousInnerClassFile4OnClickListener(++innerAnonymousClsCount,
//                        ViewInjectClassRecorder.getInstance().getInternalName(),
//                        accessMethodName == null ? method.methodName : accessMethodName,
//                        method.methodDesc);
//                FieldBean field = ifInBindView(id);
//                if (field != null) {
//                    Log.v("injectOnClickDirect");
//                    injectOnClickDirect(field, innerAnonymousClsCount);
//                } else {
//                    Log.v("injectOnClickAfterFindView");
//                    injectOnClickAfterFindView(id, innerAnonymousClsCount);
//                }
//            }
//        }
    }


    private void injectOnClickAfterFindView(int id, long anonymousClsCount) {
//        mv.visitVarInsn(ALOAD, 0);
//        mv.visitLdcInsn(new Integer(2131165300));
//        mv.visitMethodInsn(INVOKEVIRTUAL, "com/yn/asmbutterknife/TestActivity", "findViewById", "(I)Landroid/view/View;", false);
        String owner = ViewInjectClassRecorder.getInstance().getInternalName();
//        AsmUtils.injectFindViewById4Activity(this.mv, owner, id);
//        mv.visitTypeInsn(NEW, "com/yn/asmbutterknife/TestActivity$1");
        AsmUtils.injectNewOnClickListener(this.mv, owner, anonymousClsCount);
    }

    private void injectOnClickDirect(FieldBean field, long anonymousInnerClsCount) {
        String owner = ViewInjectClassRecorder.getInstance().getInternalName();
        mv.visitVarInsn(ALOAD, 0);
//        mv.visitFieldInsn(GETFIELD, "com/yn/asmbutterknife/TestActivity", "tv", "Landroid/widget/TextView;");
        mv.visitFieldInsn(GETFIELD, owner, field.name, field.desc);
//        mv.visitTypeInsn(NEW, "com/yn/asmbutterknife/TestActivity$1");
//        mv.visitInsn(DUP);
//        mv.visitVarInsn(ALOAD, 0);
//        mv.visitMethodInsn(INVOKESPECIAL, "com/yn/asmbutterknife/TestActivity$1", "<init>", "(Lcom/yn/asmbutterknife/TestActivity;)V", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "android/widget/TextView", "setOnClickListener", "(Landroid/view/View$OnClickListener;)V", false);
        AsmUtils.injectNewOnClickListener(this.mv, owner, anonymousInnerClsCount);
    }

    private FieldBean ifInBindView(int id) {
        List<Tuple<FieldBean, AnnotationBean>> bindViewDetail = ViewInjectAnalyse.getBindViewDetail();
        for (Tuple<FieldBean, AnnotationBean> bindView : bindViewDetail) {
            AnnotationBean annotation = bindView.second;
            if (annotation != null && id == (int) annotation.getValue())
                return bindView.first;
        }
        return null;
    }
}
