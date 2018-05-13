package com.yn.asmbutterknife;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yn.asmbutterknife.annotations.BindView;

public class Whyn$Test {
    @BindView(3333)
    private Button btn;

    //    @ViewInject(ViewInject.VIEWHOLDER)
    public void onCreate(LinearLayout view) {
        btn = (Button) view.findViewById(R.id.tv);
    }

//    @ViewInject
    public void onCreate1(View view) {
        this.btn = view.findViewById(R.id.tv);
        this.btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Whyn$Test.this.onCreate(null);
            }
        });

    }

}
