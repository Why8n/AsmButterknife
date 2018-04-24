package com.whyn.asm.adapters.inject;

import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.bean.Tuple;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.MethodVisitor;

public class MethodViewInjectionDelegate {

    public MethodVisitor obtainVisitor(MethodVisitor mv, int access, String name, String desc,
                                       int viewInjectType, Tuple<MethodBean, AnnotationBean> viewInjectDetail) {
        MethodVisitor visitor = mv;
        switch (viewInjectType) {
            case ViewInject.VIEWHOLDER:
                visitor = new InjectViewHolderMethodAdapter(mv, access, name, desc, viewInjectType, viewInjectDetail);
                break;
//            case ViewInject.NORMAL:
//                transMethodVisitor = new InjectNormalMethodAdapter(mv, access, methodName,
//                        methodDesc, viewInjectDetail);
//            break;
            case ViewInject.ACTIVITY:
                visitor = new InjectActivityMethodAdapter(mv, access, name, desc, viewInjectType);
                break;
            case ViewInject.DIALOG:
                break;
            case ViewInject.FRAGMENT:
                break;
            default:
                break;
        }
        return visitor;
    }


}