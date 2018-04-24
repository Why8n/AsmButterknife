package com.yn.asmbutterknife.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ViewInject {
    public static final int NONE = -1;
    public static final int NORMAL = NONE + 1;
    public static final int ACTIVITY = NORMAL + 1;
    public static final int FRAGMENT = ACTIVITY + 1;
    public static final int DIALOG = FRAGMENT + 1;
    public static final int VIEWHOLDER = DIALOG + 1;

    @WhichView int value() default NORMAL;
}

@IntDef({
        ViewInject.NONE,
        ViewInject.NORMAL,
        ViewInject.ACTIVITY,
        ViewInject.FRAGMENT,
        ViewInject.DIALOG,
        ViewInject.VIEWHOLDER,
})
@Retention(RetentionPolicy.CLASS)
@interface WhichView {

}
