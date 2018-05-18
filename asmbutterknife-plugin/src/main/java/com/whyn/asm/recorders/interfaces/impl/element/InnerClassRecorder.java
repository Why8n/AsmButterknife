package com.whyn.asm.recorders.interfaces.impl.element;

import com.whyn.asm.recorders.interfaces.impl.adapter.ClassReaderAdapter;
import com.whyn.bean.element.InnerClassBean;
import com.whyn.utils.Log;

import org.objectweb.asm.ClassVisitor;

import java.util.ArrayList;
import java.util.List;

public class InnerClassRecorder extends ClassReaderAdapter {
    private static final String TAG = InnerClassRecorder.class.getSimpleName();
    static List<InnerClassBean> sInnerClasses;

    public InnerClassRecorder() {
        reset();
    }

    @Override
    public void visitInnerClass(InnerClassBean bean) {
        if (InnerClassRecorder.sInnerClasses == null)
            InnerClassRecorder.sInnerClasses = new ArrayList<>();
        InnerClassRecorder.sInnerClasses.add(bean);
        Log.v("%s found: %s", TAG, bean);
    }

    @Override
    public void visitEnd(ClassVisitor cv) {
    }

    private void reset() {
        InnerClassRecorder.sInnerClasses = null;
    }
}
