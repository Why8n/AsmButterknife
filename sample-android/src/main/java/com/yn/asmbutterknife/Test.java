package com.yn.asmbutterknife;

import android.util.Log;
import android.view.View;

import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.ViewInject;

public class Test {
    @BindView(3333)
    private String str;

//    @ViewInject(ViewInject.VIEWHOLDER)
    public void onCreate(View view) {
        Log.v("Whyn", "Hello asm");
    }

    @ViewInject()
    public void onCreate1(View view) {
        Log.v("Whyn", "Hello asm");
    }

}
