package com.yn.asmbutterknife;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.ViewInject;

public class Test {
    @BindView(3333)
    private Button btn;

    //    @ViewInject(ViewInject.VIEWHOLDER)
    public void onCreate(LinearLayout view) {
        btn = (Button)view.findViewById(R.id.tv);
    }

    @ViewInject
    public void onCreate1(View view) {
        Log.v("Whyn", "Hello asm");
    }

}
