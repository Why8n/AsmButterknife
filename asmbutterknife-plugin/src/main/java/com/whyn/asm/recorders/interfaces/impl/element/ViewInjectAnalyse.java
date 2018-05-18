package com.whyn.asm.recorders.interfaces.impl.element;

import com.android.annotations.NonNull;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.bean.Tuple;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.util.List;


public final class ViewInjectAnalyse {
    private static final String ACCESS_METHOD_REG = "access\\$[01]+$";
    private static File sClsFile;
    private static long sAnonymousInnerClassCount = 0;

    private ViewInjectAnalyse() {
        throw new AssertionError("No instances.");
    }

    public static long howManyAnonymousInnerClass() {
        if (ViewInjectAnalyse.sAnonymousInnerClassCount != 0)
            return ViewInjectAnalyse.sAnonymousInnerClassCount;
        List<InnerClassBean> innerClasses = InnerClassRecorder.sInnerClasses;
        long innerAnonymousClsCount = 0;
        for (InnerClassBean innerCls : innerClasses) {
            if (innerCls.isAnonymous())
                ++innerAnonymousClsCount;
        }
        return ViewInjectAnalyse.sAnonymousInnerClassCount = innerAnonymousClsCount;
    }

    public static int howManyAccessMethod() {
        return AccessMethodRecorder.sAccessMethodCount;
    }

    //mv.visitMethodInsn(INVOKESTATIC, "com/yn/asmbutterknife/TestActivity", "access$000", "(Lcom/yn/asmbutterknife/TestActivity;)V", false);
    public static boolean isAccessMethod(int access, String name, String desc) {
        return (access & Opcodes.ACC_STATIC) != 0
                && name.matches(ACCESS_METHOD_REG)
                && desc.startsWith(String.format("(%s)",
                Type.getObjectType(ViewInjectAnalyse.getInternalName()).getDescriptor()));
    }

    public static int getViewInjectType() {
        return ViewInjectTypeRecorder.sViewInjectType;
    }

    public static List<Tuple<FieldBean, AnnotationBean>> getBindViewDetail() {
        return BindViewRecorder.sBindViews;
    }

    public static List<Tuple<MethodBean, AnnotationBean>> getOnClickDetail() {
        return OnClickRecorder.sOnClickDetails;
    }

    public static List<InnerClassBean> getInnerClass() {
        return InnerClassRecorder.sInnerClasses;
    }

    public static int version() {
        return ClassRecorder.sVerion;
    }

    public static @NonNull
    String getInternalName() {
        return ClassRecorder.sInternalName;
    }

    public static MethodBean getViewInjectMethod() {
        return ViewInjectTypeRecorder.sViewInjectMethod;
    }

    public static void recordClassFile(File classFile) {
        ViewInjectAnalyse.sClsFile = classFile;
    }

    public static File getClassFile() {
        return ViewInjectAnalyse.sClsFile;
    }
}
