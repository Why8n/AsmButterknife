package com.yn.asmbutterknife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yn.asmbutterknife.annotations.OnClick;
import com.yn.asmbutterknife.annotations.ViewInject;

public class TestActivity extends AppCompatActivity {

    @Override
    @ViewInject(ViewInject.ACTIVITY)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @OnClick(R.id.btnPublic)
    public void publicMehotd() {
        Toast.makeText(this, "public method", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnPackage)
    void packageMehotd() {
        Toast.makeText(this, "package method", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnProtected)
    protected void protectedMehotd() {
        Toast.makeText(this, "protected method", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnPrivate)
    private void privateMehotd() {
        Toast.makeText(this, "private method", Toast.LENGTH_SHORT).show();
    }

}
