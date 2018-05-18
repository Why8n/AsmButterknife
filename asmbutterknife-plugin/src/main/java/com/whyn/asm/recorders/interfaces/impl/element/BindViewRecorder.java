package com.whyn.asm.recorders.interfaces.impl.element;

import com.android.annotations.NonNull;
import com.whyn.asm.adapters.base.BaseMethodAdapter;
import com.whyn.asm.adapters.base.BaseMethodVisitor;
import com.whyn.exceptions.ViewHolderArgumentException;
import com.whyn.asm.recorders.interfaces.impl.adapter.ClassReaderAdapter;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.Log;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BindViewRecorder extends ClassReaderAdapter {
    private static final String TAG = BindViewRecorder.class.getSimpleName();
    static List<Tuple<FieldBean, AnnotationBean>> sBindViews;

    public BindViewRecorder() {
        reset();
    }

    @Override
    public void visitField(@NonNull FieldBean bean) {
        Set<AnnotationBean> annotations = bean.getAnnotation();
        for (AnnotationBean annotation : annotations) {
            if (Type.getDescriptor(BindView.class).equals(annotation.typeDesc)) {
                if (BindViewRecorder.sBindViews == null)
                    BindViewRecorder.sBindViews = new ArrayList<>();
                Log.v("%s found:\n%s", TAG, bean);
                BindViewRecorder.sBindViews.add(new Tuple<>(bean, annotation));
                return;
            }
        }
    }

    @Override
    public MethodVisitor injectMethod(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        if (BindViewRecorder.sBindViews == null)
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
                    String clsInternalName = ViewInjectAnalyse.getInternalName();
                    AsmUtils.injectFindViewById(mv, clsInternalName, clsInternalName);
                    Log.v("%s: inject Activity.findViweById() done", TAG);
                }
            }
        };
    }

    private MethodVisitor injectViewHolder(MethodVisitor mv, int access, String name, String desc) {
        MethodBean method = ViewInjectAnalyse.getViewInjectMethod();
//        if (method == null) {
//            throw new NoViewInjectException(String.format("%s must annotated with @%s on one specific method",
//                    AsmUtils.internal2Class(ViewInjectAnalyse.getInternalName()), ViewInject.class.getSimpleName()))
//        }
        if (method != null && method.methodName.equals(name) && method.methodDesc.equals(desc)) {
            return new BaseMethodAdapter(mv, access, name, desc) {
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
                    AsmUtils.injectFindViewById(mv, ViewInjectAnalyse.getInternalName(), args[0].getInternalName());
                    Log.v("%s: inject View.findViweById() done", TAG);
                }
            };
        }
        return mv;
    }

    @Override
    public void visitEnd(ClassVisitor cv) {
    }

    private void reset() {
        BindViewRecorder.sBindViews = null;
    }
}
