package com.whyn.bean.element;

import com.android.annotations.NonNull;

public final class ArgumentBean {

    public final int access;
    public final String name;

    public ArgumentBean(@NonNull int access, @NonNull String name) {
        this.access = access;
        this.name = name;
    }
}
