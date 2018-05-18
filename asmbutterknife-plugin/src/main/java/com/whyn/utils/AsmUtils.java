package com.whyn.utils;

import com.android.annotations.NonNull;
import com.whyn.exceptions.NoViewInjectException;
import com.whyn.asm.recorders.interfaces.impl.element.ViewInjectAnalyse;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.OnClick;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AsmUtils implements Opcodes {
    private AsmUtils() {
        throw new AssertionError("No instances.");
    }

    //    for inner class visit outer class field

    /**
     * @param cv
     * @param order the exactly order of this @OnClick
     *              order 0--->0
     *              order 1--->100
     *              order 2--->200
     *              ```
     *              order 10--->1000
     *              order 11--->1100
     */
    public static void injectMethodAccess(@NonNull ClassVisitor cv, int order,
                                          String outerClsInternalName, MethodBean methodDetail) {
        int index = methodDetail.methodDesc.indexOf(")");
        String methodReturnDesc = methodDetail.methodDesc.substring(index + 1);
        Log.v("injectMethodAccess---------------%s",
                String.format("(%s)%s", Type.getObjectType(outerClsInternalName).getDescriptor(), methodReturnDesc));
        MethodVisitor mv = cv.visitMethod(ACC_STATIC + ACC_SYNTHETIC,
                String.format("access$%03d", order * 100),
                String.format("(%s)%s", Type.getObjectType(outerClsInternalName).getDescriptor(),
                        methodReturnDesc), null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, outerClsInternalName, methodDetail.methodName,
                methodDetail.methodDesc, false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public static boolean isStatic(int access) {
        return (access & Opcodes.ACC_STATIC) != 0;
    }

    public static void injectFindViewById(MethodVisitor mv, String clsInternalName, String findViewMethodOwner) {
//        String clsInternalName = ViewInjectClassRecorder.getInstance().getInternalName();
        int viewInjectType = ViewInjectAnalyse.getViewInjectType();
        if (viewInjectType == ViewInject.NONE) {
            throw new NoViewInjectException(String.format("%s must annotated with @%s on one specific method",
                    AsmUtils.internal2Class(clsInternalName), ViewInject.class.getSimpleName()));
        }
        List<Tuple<FieldBean, AnnotationBean>> bindViewDetail = ViewInjectAnalyse.getBindViewDetail();
        Utils.checkNotNull(bindViewDetail, "no @%s detected!", BindView.class.getSimpleName());
        for (Tuple<FieldBean, AnnotationBean> detail : bindViewDetail) {
            FieldBean field = detail.first;
            AnnotationBean annotation = detail.second;
            Integer id = (Integer) annotation.getValue();
            if (id == null || id.intValue() < 0)
                continue;
            mv.visitVarInsn(ALOAD, 0);
            if (viewInjectType == ViewInject.ACTIVITY)
                mv.visitVarInsn(ALOAD, 0);
            else //for viewholder
                mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn(id);
//                mv.visitMethodInsn(INVOKEVIRTUAL, "com/yn/asmbutterknife/TestActivity", "findViewById", "(I)Landroid/view/View;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL,
                    findViewMethodOwner,
                    "findViewById",
                    "(I)Landroid/view/View;",
                    false);
//                mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
            mv.visitTypeInsn(CHECKCAST, Type.getType(field.desc).getInternalName());
//                mv.visitFieldInsn(PUTFIELD, "com/yn/asmbutterknife/TestActivity", "tv", "Landroid/widget/TextView;");
            mv.visitFieldInsn(PUTFIELD, clsInternalName, field.name, field.desc);
        }
    }

    private static FieldBean ifInBindView(int id) {
        List<Tuple<FieldBean, AnnotationBean>> bindViewDetail = ViewInjectAnalyse.getBindViewDetail();
        Utils.checkNotNull(bindViewDetail, "no @%s detected!", BindView.class.getSimpleName());
        for (Tuple<FieldBean, AnnotationBean> bindView : bindViewDetail) {
            AnnotationBean annotation = bindView.second;
            if (annotation != null && id == (int) annotation.getValue())
                return bindView.first;
        }
        return null;
    }

    private static void injectOnClickAfterFindView(MethodVisitor mv, int id, long anonymousClsCount) {
//        mv.visitVarInsn(ALOAD, 0);
//        mv.visitLdcInsn(new Integer(2131165300));
//        mv.visitMethodInsn(INVOKEVIRTUAL, "com/yn/asmbutterknife/TestActivity", "findViewById", "(I)Landroid/view/View;", false);
        String owner = ViewInjectAnalyse.getInternalName();
        boolean isActivity = (ViewInjectAnalyse.getViewInjectType() == ViewInject.ACTIVITY);
        mv.visitVarInsn(ALOAD, isActivity ? 0 : 1);
        mv.visitLdcInsn(new Integer(id));
        String findViewOwnerCls = owner;
        if (!isActivity) {
            MethodBean method = ViewInjectAnalyse.getViewInjectMethod();
            //todo::check args size
            findViewOwnerCls = method.getArgument()[0].getInternalName();
        }
        mv.visitMethodInsn(INVOKEVIRTUAL, findViewOwnerCls, "findViewById", "(I)Landroid/view/View;", false);
//        mv.visitTypeInsn(NEW, "com/yn/asmbutterknife/TestActivity$1");
        AsmUtils.injectNewOnClickListener(mv, owner, anonymousClsCount);
    }

    private static void injectOnClickWithFiledExists(MethodVisitor mv, FieldBean field, long anonymousInnerClsCount) {
        String owner = ViewInjectAnalyse.getInternalName();
        mv.visitVarInsn(ALOAD, 0);
//        mv.visitFieldInsn(GETFIELD, "com/yn/asmbutterknife/TestActivity", "tv", "Landroid/widget/TextView;");
        mv.visitFieldInsn(GETFIELD, owner, field.name, field.desc);
//        mv.visitTypeInsn(NEW, "com/yn/asmbutterknife/TestActivity$1");
//        mv.visitInsn(DUP);
//        mv.visitVarInsn(ALOAD, 0);
//        mv.visitMethodInsn(INVOKESPECIAL, "com/yn/asmbutterknife/TestActivity$1", "<init>", "(Lcom/yn/asmbutterknife/TestActivity;)V", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "android/widget/TextView", "setOnClickListener", "(Landroid/view/View$OnClickListener;)V", false);
        AsmUtils.injectNewOnClickListener(mv, owner, anonymousInnerClsCount);
    }

    public static void injectOnClick(MethodVisitor mv) {
        Map<MethodBean, String> methodAccessMap = new HashMap<>();
        long order = ViewInjectAnalyse.howManyAccessMethod();
        List<Tuple<MethodBean, AnnotationBean>> onClickDetail = ViewInjectAnalyse.getOnClickDetail();
        if (onClickDetail == null)
            return;
        for (Tuple<MethodBean, AnnotationBean> onClick : onClickDetail) {
            MethodBean method = onClick.first;
            if (method != null && (method.access & Opcodes.ACC_PRIVATE) != 0) {
                methodAccessMap.put(method, String.format("access$%03d", order * 100));
                ++order;
            }
        }

        String owner = ViewInjectAnalyse.getInternalName();
        long innerAnonymousClsCount = ViewInjectAnalyse.howManyAnonymousInnerClass();
        for (Tuple<MethodBean, AnnotationBean> onClick : onClickDetail) {
            MethodBean method = onClick.first;
            AnnotationBean annotation = onClick.second;
            if (method == null || annotation == null)
                continue;
            int[] ids = (int[]) annotation.getValue();
            for (int id : ids) {
                String accessMethodName = methodAccessMap.get(method);
                createAnonymousInnerClassFile4OnClickListener(++innerAnonymousClsCount,
                        owner,
                        accessMethodName == null ? method.methodName : accessMethodName,
                        accessMethodName == null ? method.methodDesc :
                                String.format("(%s)V", Type.getObjectType(owner).getDescriptor()),
                        accessMethodName != null);
                FieldBean field = ifInBindView(id);
                if (field != null) {
                    Log.v("injectOnClickWithFiledExists");
                    injectOnClickWithFiledExists(mv, field, innerAnonymousClsCount);
                } else {
                    Log.v("injectOnClickAfterFindView");
                    injectOnClickAfterFindView(mv, id, innerAnonymousClsCount);
                }
            }
        }
    }

    private static void injectNewOnClickListener(@NonNull MethodVisitor mv, String owner, long anonymousInnerClassCount) {
        String clsInternalName = String.format("%s$%d", owner, anonymousInnerClassCount);
        mv.visitTypeInsn(NEW, clsInternalName);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 0);
//        mv.visitMethodInsn(INVOKESPECIAL, "com/yn/asmbutterknife/TestActivity$1", "<init>", "(Lcom/yn/asmbutterknife/TestActivity;)V", false);
        mv.visitMethodInsn(INVOKESPECIAL, clsInternalName, "<init>",
                String.format("(%s)V", Type.getObjectType(owner).getDescriptor()), false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "setOnClickListener", "(Landroid/view/View$OnClickListener;)V", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "setOnClickListener", "(Landroid/view/View$OnClickListener;)V", false);
    }

    public static void createAnonymousInnerClassFile4OnClickListener(long innerClassNum,
                                                                     String owner,
                                                                     String methodName,
                                                                     String methodDesc,
                                                                     boolean isAccessMethod) {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        String innerClsInternalName = String.format("%s$%d", owner, innerClassNum);
        String ownerDesc = Type.getObjectType(owner).getDescriptor();

        cw.visit(ViewInjectAnalyse.version(), ACC_SUPER, innerClsInternalName, null, "java/lang/Object", new String[]{"android/view/View$OnClickListener"});

//        cw.visitOuterClass(owner, "onCreate", "(Landroid/os/Bundle;)V");
//        cw.visitInnerClass(innerClsInternalName, null, null, 0);
        MethodBean viewInectMethod = ViewInjectAnalyse.getViewInjectMethod();
        cw.visitOuterClass(owner, viewInectMethod.methodName, viewInectMethod.methodDesc);
        List<InnerClassBean> innerClassBeans = ViewInjectAnalyse.getInnerClass();
        if (innerClassBeans != null) {
            for (InnerClassBean bean : innerClassBeans) {
                //it means that owner is an innerclass
                if (owner.equals(bean.clsInternalName)) {
                    cw.visitInnerClass(bean.clsInternalName, bean.outerClsInternalName, bean.innerName, bean.access);
                    break;
                }
            }
        }
        cw.visitInnerClass(innerClsInternalName, null, null, 0);

        cw.visitInnerClass("android/view/View$OnClickListener", "android/view/View", "OnClickListener", ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT + ACC_INTERFACE);

        {
            fv = cw.visitField(ACC_FINAL + ACC_SYNTHETIC, "this$0", ownerDesc, null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(0, "<init>", String.format("(%s)V", ownerDesc), null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, innerClsInternalName, "this$0", ownerDesc);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "onClick", "(Landroid/view/View;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, innerClsInternalName, "this$0", ownerDesc);
            mv.visitMethodInsn(isAccessMethod ? INVOKESTATIC : INVOKEVIRTUAL, owner, methodName, methodDesc, false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 2);
            mv.visitEnd();
        }
        cw.visitEnd();
        byte[] bytes = cw.toByteArray();
        File originClassFile = ViewInjectAnalyse.getClassFile();
        String innerClassFileName = innerClsInternalName.substring(innerClsInternalName.lastIndexOf("/") + 1);
        File innerClassFile = new File(originClassFile.getParent(), innerClassFileName + ".class");
        if (Utils.write2file(innerClassFile, bytes)) {
            Log.v("generate class file successfully: %s", innerClassFile.toString());
        } else {
            Log.w("generate class file failed: %s", innerClassFile.toString());
        }
    }


    public static String desc2Class(@NonNull String descriptor) {
        return Type.getType(descriptor).getClassName();
    }

    public static String internal2Class(@NonNull String internalName) {
        return Type.getObjectType(internalName).getClassName();
    }

    public static String internal2Desc(@NonNull String internalName) {
        return Type.getObjectType(internalName).getDescriptor();
    }

    public static String javaVersion(int version) {
        String javaVersion = "unknown";
        switch (version) {
            case Opcodes.V1_1:
                javaVersion = "Java_1";
                break;
            case Opcodes.V1_2:
                javaVersion = "Java_2";
                break;
            case Opcodes.V1_3:
                javaVersion = "Java_3";
                break;
            case Opcodes.V1_4:
                javaVersion = "Java_4";
                break;
            case Opcodes.V1_5:
                javaVersion = "Java_5";
                break;
            case Opcodes.V1_6:
                javaVersion = "Java_6";
                break;
            case Opcodes.V1_7:
                javaVersion = "Java_7";
                break;
            case Opcodes.V1_8:
                javaVersion = "Java_8";
                break;
            default:
                javaVersion = "over Java_8";
                break;
        }
        return javaVersion;
    }
}
