package com.whyn.asm.adapters.inject;

import com.yn.asmbutterknife.annotations.OnClick;

import org.objectweb.asm.Type;

public final class InjectAnnotationDelegate {
    private InjectAnnotationDelegate() {
    }

    public static void dispatch(String annotationDesc) {
        if (Type.getDescriptor(OnClick.class).equals(annotationDesc)) {
        }
    }
}
