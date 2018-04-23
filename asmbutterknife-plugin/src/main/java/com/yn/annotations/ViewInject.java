package com.yn.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ViewInject {
    public static final int NONE = -1;
    public static final int ACTIVITY = 0;
    public static final int FRAGMENT = 1;
    public static final int DIALOG = 2;
    public static final int VIEWHOLDER = 3;

    @WhichView int value() default ACTIVITY;
}

@IntDef({
        ViewInject.NONE,
        ViewInject.ACTIVITY,
        ViewInject.FRAGMENT,
        ViewInject.DIALOG,
        ViewInject.VIEWHOLDER,
})
@Retention(RetentionPolicy.CLASS)
@interface WhichView {

}
