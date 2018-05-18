package com.whyn.asm.recorders.interfaces.impl.element;


import com.whyn.asm.recorders.interfaces.impl.adapter.ClassReaderAdapter;
import com.whyn.utils.AsmUtils;
import com.whyn.utils.Log;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;


public class ClassRecorder extends ClassReaderAdapter {
    private static final String TAG = ClassRecorder.class.getSimpleName();
    static int sVerion = Opcodes.V1_7;
    static String sInternalName;

    public ClassRecorder() {
        reset();
    }

    @Override
    public void visitVersion(int version) {
        ClassRecorder.sVerion = version;
        Log.v("%s: javaVersion: %s", TAG, AsmUtils.javaVersion(version));
    }

    @Override
    public void visitClass(int access, String name, String signature, String superName, String[] interfaces) {
        ClassRecorder.sInternalName = name;
        Log.v("%s: clsInternalName: %s", TAG, name);
    }

    @Override
    public void visitEnd(ClassVisitor cv) {
    }

    private void reset() {
        ClassRecorder.sVerion = Opcodes.V1_7;
        ClassRecorder.sInternalName = null;
    }
}
