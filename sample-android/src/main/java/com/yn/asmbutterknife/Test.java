package com.yn.asmbutterknife;

import android.util.Log;
import android.view.View;

import com.yn.annotations.BindView;
import com.yn.annotations.ViewInject;

@ViewInject(ViewInject.VIEWHOLDER)
public class Test {
    @BindView(3333)
    private String str;

    public void onCreate(View view) {
        Log.v("Whyn", "Hello asm");
    }
}
