package com.whyn.asm.recorders.interfaces.impl.element;

import com.android.annotations.NonNull;
import com.whyn.asm.adapters.base.BaseMethodVisitor;
import com.whyn.exceptions.ViewHolderArgumentException;
import com.whyn.asm.recorders.interfaces.impl.adapter.ClassReaderAdapter;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.define.Const;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.Log;
import com.whyn.utils.Utils;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.OnClick;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

public class OnClickRecorder extends ClassReaderAdapter {
    private static final String TAG = OnClickRecorder.class.getSimpleName();
    static List<Tuple<MethodBean, AnnotationBean>> sOnClickDetails;

    public OnClickRecorder() {
        reset();
    }

    @Override
    public void visitMethod(@NonNull MethodBean bean) {
        Set<AnnotationBean> annotations = bean.getAnnotation();
        for (AnnotationBean annotation : annotations) {
            if (Type.getDescriptor(OnClick.class).equals(annotation.typeDesc)) {
                if (OnClickRecorder.sOnClickDetails == null)
                    OnClickRecorder.sOnClickDetails = new ArrayList<>();
                Log.v("%s found: @%s", TAG, bean);
                OnClickRecorder.sOnClickDetails.add(new Tuple<>(bean, annotation));
                return;
            }
        }
    }

    @Override
    public MethodVisitor injectMethod(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        if (OnClickRecorder.sOnClickDetails == null)
            return mv;
        MethodVisitor methodVisitor = mv;
        int viewInjectType = ViewInjectAnalyse.getViewInjectType();
        switch (viewInjectType) {
            case ViewInject.ACTIVITY:
                methodVisitor = injectActivity(mv);
                break;
            case ViewInject.VIEWHOLDER:
                methodVisitor = injectViewHolder(mv, access, name, desc);
                break;
            default:
                break;
        }
        return methodVisitor;
    }

    private MethodVisitor injectActivity(MethodVisitor mv) {
        return new BaseMethodVisitor(mv) {
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                //super.visitMethodInsn must be call first,or inject order will be inverse
                super.visitMethodInsn(opcode, owner, name, desc, itf);
                if ("setContentView".equals(name) && "(I)V".equals(desc)) {
                    AsmUtils.injectOnClick(this.mv);
                    Log.v("%s: inject Activity.setOnClickListener() done", TAG);
                }
            }
        };
    }

    private MethodVisitor injectViewHolder(MethodVisitor mv, int access, String name, String desc) {
        MethodBean method = ViewInjectAnalyse.getViewInjectMethod();
        if (method != null && method.methodName.equals(name) && method.methodDesc.equals(desc)) {
            return new AdviceAdapter(Const.ASM_API, mv, access, name, desc) {
                @Override
                protected void onMethodEnter() {
                    super.onMethodEnter();
                    MethodBean method = ViewInjectAnalyse.getViewInjectMethod();
                    Type[] args = method.getArgument();
                    if (args == null || args.length == 0) {
                        throw new ViewHolderArgumentException(String.format("%s:method annotated with @%s(ViewHolder) should take %s type as the first argument",
                                AsmUtils.internal2Class(ViewInjectAnalyse.getInternalName()),
                                ViewInject.class.getSimpleName(), "android.view.View"));
                    }
                    AsmUtils.injectOnClick(this.mv);
                    Log.v("%s: inject View.setOnClickListener() done", TAG);
                }
            };
        }
        return mv;
    }

    @Override
    public void visitEnd(ClassVisitor cv) {
        Log.v("onClickRecorder:visitEnd");
        injectInnerClass4OnClick(cv);
        injectAccessMethod(cv);
    }

    private void reset() {
        OnClickRecorder.sOnClickDetails = null;
    }


    private void injectInnerClass4OnClick(ClassVisitor cv) {
        injectAnonymousCls4OnClick(cv);
    }

    private void injectAnonymousCls4OnClick(ClassVisitor cv) {
        Log.v("injectAnonymousClass4OnClick");
        List<Tuple<MethodBean, AnnotationBean>> onClickDetail = ViewInjectAnalyse.getOnClickDetail();
        if (onClickDetail == null)
            return;
        String owner = ViewInjectAnalyse.getInternalName();
        boolean needInjectOnClickListener = false;
        long innerAnonymousClsCount = ViewInjectAnalyse.howManyAnonymousInnerClass();
        for (Tuple<MethodBean, AnnotationBean> onClick : onClickDetail) {
            int[] ids = (int[]) onClick.second.getValue();
            for (int i = 0; i < ids.length; ++i) {
                needInjectOnClickListener = true;
                cv.visitInnerClass(String.format("%s$%d", owner, ++innerAnonymousClsCount), null, null, 0);
                Log.v("inject InnerClass: %s$%d", owner, innerAnonymousClsCount);
            }
        }
        if (needInjectOnClickListener) {
            Log.v("inject innercalss View$OnClickListener");
            cv.visitInnerClass("android/view/View$OnClickListener",
                    "android/view/View",
                    "OnClickListener",
                    ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT + ACC_INTERFACE);
        }
    }

    private void injectAccessMethod(ClassVisitor cv) {
        int order = injectAccessMethod4OnClick(cv, ViewInjectAnalyse.howManyAccessMethod());
//        order = injectOnItemClick(order);
    }

    private int injectAccessMethod4OnClick(ClassVisitor cv, int order) {
        List<Tuple<MethodBean, AnnotationBean>> onClickDetail = ViewInjectAnalyse.getOnClickDetail();
        if (onClickDetail == null)
            return order;
        for (Tuple<MethodBean, AnnotationBean> onClick : onClickDetail) {
            MethodBean method = onClick.first;
            if (method != null && (method.access & Opcodes.ACC_PRIVATE) != 0) {
                injectOnClick4PrivateMethod(cv, order++,
                        ViewInjectAnalyse.getInternalName(),
                        method);
            }
        }
        return order;
    }

    //    inject access$xx method for View.OnClickListener(){} anonymous inner class to visit outer
    private void injectOnClick4PrivateMethod(@NonNull ClassVisitor cv, int order,
                                             String outerClsInternalName, MethodBean methodDetail) {
        Log.v("inject accessmethod");
        AsmUtils.injectMethodAccess(cv, order, outerClsInternalName, methodDetail);
    }
}
