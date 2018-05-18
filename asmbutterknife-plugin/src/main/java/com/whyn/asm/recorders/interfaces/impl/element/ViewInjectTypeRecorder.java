package com.whyn.asm.recorders.interfaces.impl.element;

import com.whyn.exceptions.MultiViewInjectTypeException;
import com.whyn.asm.recorders.interfaces.impl.adapter.ClassReaderAdapter;
import com.whyn.bean.element.AnnotationBean;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.Log;
import com.yn.asmbutterknife.annotations.ViewInject;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

import java.util.Set;

public class ViewInjectTypeRecorder extends ClassReaderAdapter {
    private static final String TAG = ViewInjectTypeRecorder.class.getSimpleName();
    private static final int DEFAULT_TYPE = ViewInject.VIEWHOLDER;
    private static final int INITIAL_TYPE = ViewInject.NONE;
    static int sViewInjectType = ViewInjectTypeRecorder.INITIAL_TYPE;
    static MethodBean sViewInjectMethod;

    public ViewInjectTypeRecorder() {
        reset();
    }

    @Override
    public void visitMethod(MethodBean bean) {
        Set<AnnotationBean> annotations = bean.getAnnotation();
        for (AnnotationBean annotation : annotations) {
            if (Type.getDescriptor(ViewInject.class).equals(annotation.typeDesc)) {
                if (ViewInjectTypeRecorder.sViewInjectType != ViewInjectTypeRecorder.INITIAL_TYPE) {
                    throw new MultiViewInjectTypeException(String.format("%s:@%s can only show once,detected multi @%s in class %s",
                            AsmUtils.internal2Class(ViewInjectAnalyse.getInternalName()),
                            ViewInject.class.getSimpleName(),
                            ViewInject.class.getSimpleName(),
                            ViewInjectAnalyse.getInternalName()));
                }
                Object value = annotation.getValue();
                ViewInjectTypeRecorder.sViewInjectType = (value == null ? ViewInjectTypeRecorder.DEFAULT_TYPE : (int) value);
                ViewInjectTypeRecorder.sViewInjectMethod = bean;
                Log.v("%s found: %s", TAG, bean);
                return;
            }
        }
    }

    @Override
    public void visitEnd(ClassVisitor cv) {
    }

    private void reset() {
        ViewInjectTypeRecorder.sViewInjectType = ViewInjectTypeRecorder.INITIAL_TYPE;
        ViewInjectTypeRecorder.sViewInjectMethod = null;
    }
}
