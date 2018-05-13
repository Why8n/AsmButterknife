package com.whyn.asm;

import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.FieldBean;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.Utils;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Set;


public final class ViewInjectAnalyse {
    private static final String ACCESS_METHOD_REG = "access\\$[01]+$";

    private ViewInjectAnalyse() {
    }

    public static long howManyAnonymousInnerClass() {
        List<InnerClassBean> innerClasses = ViewInjectClassRecorder.getInstance().getInnerClass();
        long innerAnonymousClsCount = 0;
        for (InnerClassBean innerCls : innerClasses) {
            if (innerCls.isAnonymous())
                ++innerAnonymousClsCount;
        }
        return innerAnonymousClsCount;
    }

    public static int howManyAccessMethod() {
        int count = 0;
        Set<MethodBean> methods = ViewInjectClassRecorder.getInstance().getMethod();
        for (MethodBean method : methods) {
            if (method != null && isAccessMethod(method.access, method.methodName, method.methodDesc))
                ++count;
        }
        return count;
    }

    //mv.visitMethodInsn(INVOKESTATIC, "com/yn/asmbutterknife/TestActivity", "access$000", "(Lcom/yn/asmbutterknife/TestActivity;)V", false);
    public static boolean isAccessMethod(int access, String name, String desc) {
        return (access & Opcodes.ACC_STATIC) != 0
                && name.matches(ACCESS_METHOD_REG)
                && desc.startsWith(String.format("(%s)",
                Type.getObjectType(ViewInjectClassRecorder.getInstance().getInternalName()).getDescriptor()));
    }

    public static Tuple<MethodBean, AnnotationBean> getViewInjectDetail() {
        return ViewInjectClassRecorder.getInstance().getViewInjectDetail();
    }

    public static int getViewInjectType() {
        int type = ViewInject.NONE;
        Tuple<MethodBean, AnnotationBean> viewInjectDetail = ViewInjectClassRecorder.getInstance().getViewInjectDetail();
        if (viewInjectDetail != null) {
            type = Utils.<Integer>getProperValue(viewInjectDetail.second.getValue(), ViewInject.NONE).intValue();
        }
        return type;
    }

    public static List<Tuple<FieldBean, AnnotationBean>> getBindViewDetail() {
        return ViewInjectClassRecorder.getInstance().getBindViewDetail();
    }

    public static List<Tuple<MethodBean, AnnotationBean>> getOnClickDetail() {
        return ViewInjectClassRecorder.getInstance().getOnClickDetail();
    }

    public static List<InnerClassBean> getInnerClass() {
        return ViewInjectClassRecorder.getInstance().getInnerClass();
    }

    public static int version(){
        return ViewInjectClassRecorder.getInstance().getVersion();
    }
}
