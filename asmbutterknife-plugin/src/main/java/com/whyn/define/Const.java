package com.whyn.define;

import org.objectweb.asm.Opcodes;

public final class Const {
    private Const() {
    }

    public static final int ASM_API = Opcodes.ASM5;


    public static final class Annotation {
        public static final String VIEWINJECT_DESC = "Lcom/yn/annotations/ViewInject;";
        public static final String BINDVIEW_DESC = "Lcom/yn/annotations/BindView;";
        public static final String ONCLICK_DESC = "Lcom/yn/annotations/OnClick;";
    }
}
