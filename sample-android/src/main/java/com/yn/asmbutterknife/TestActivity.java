package com.yn.asmbutterknife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.OnClick;
import com.yn.asmbutterknife.annotations.ViewInject;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    private TextView tv;
    @BindView(R.id.rc)
    private RecyclerView rc;

    public TestActivity() {
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    @ViewInject(ViewInject.VIEWHOLDER)
    private void viewInject(View view) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = this.getWindow().getDecorView();
        this.viewInject(view);

//        this.tv = findViewById(R.id.tv);
//        this.rc = findViewById(R.id.rc);
//        this.tv.setOnClickListener(new AsmButterknife_TestActivity());
//        this.rc.setOnClickListener(new AsmButterknife_TestActivity());

//        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                click();
//            }
//        });
        findViewById(R.id.rc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRC();
            }
        });
        String lookBeforeInClassFileSeeWhatHasInjected = "TestActivity.class";
    }

    @OnClick(R.id.tv)
    void click() {
        Toast.makeText(this, "package method", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn)
    private void clickRC() {
        Toast.makeText(this, "private method", Toast.LENGTH_SHORT).show();
    }

    private class RunnableFront implements Runnable {
        @Override
        public void run() {

        }
    }

    private final class AsmButterknife_TestActivity implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv:
                    click();
                case R.id.rc:
                    clickRC();
            }
        }
    }

    private final class RunnableBehind implements Runnable {
        @Override
        public void run() {

        }
    }

}
