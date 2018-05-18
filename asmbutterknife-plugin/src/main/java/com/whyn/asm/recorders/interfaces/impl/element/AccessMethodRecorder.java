package com.whyn.asm.recorders.interfaces.impl.element;

import com.whyn.asm.recorders.interfaces.impl.adapter.ClassReaderAdapter;
import com.whyn.bean.element.MethodBean;
import com.whyn.utils.Log;

import org.objectweb.asm.ClassVisitor;

public class AccessMethodRecorder extends ClassReaderAdapter {
    private static final String TAG = AccessMethodRecorder.class.getSimpleName();
    static int sAccessMethodCount = 0;

    public AccessMethodRecorder() {
        reset();
    }

    @Override
    public void visitMethod(MethodBean bean) {
        if (ViewInjectAnalyse.isAccessMethod(bean.access, bean.methodName, bean.methodDesc)) {
            Log.v("%s:find access method: %s", TAG, bean);
            ++AccessMethodRecorder.sAccessMethodCount;
        }
    }

    @Override
    public void visitEnd(ClassVisitor cv) {
    }

    private void reset() {
        AccessMethodRecorder.sAccessMethodCount = 0;
    }
}
