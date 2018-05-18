package com.yn.asmbutterknife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yn.asmbutterknife.adapter.RecyclerAdapter;
import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.OnClick;
import com.yn.asmbutterknife.annotations.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn;
//    @BindView(R.id.tv)
    private TextView mTextView;
    private Button bt2n;
    @BindView(R.id.rc)
    private RecyclerView mRecyclerView;

    @Override
    @ViewInject(ViewInject.ACTIVITY)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> datas = initData();
//        this.mRecyclerView = this.findViewById(R.id.rc);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.setAdapter(new RecyclerAdapter(datas));
        try {
            Class<?> cls = Class.forName("com.yn.asmbutterknife.TestActivity");
            while (cls != null) {
                Log.v("Whyn111", cls.getName());
                cls = cls.getSuperclass();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> initData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 30; ++i) {
            datas.add(i + "");
        }
        return datas;
    }

    @OnClick(R.id.tv)
    public void onTextViewClick() {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn)
    private void click() {
        Intent intent = new Intent(this, TestActivity.class);
        this.startActivity(intent);
    }


}
