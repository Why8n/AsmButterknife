package com.yn.asmbutterknife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.OnClick;
import com.yn.asmbutterknife.annotations.ViewInject;

public class TestFragment extends Fragment {

    @BindView(R.id.tv)
    private TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        this.inject(view);
        this.mTextView.setText("I am Fragment");
        return view;
    }

    @OnClick(R.id.tv)
    private void click() {
        Toast.makeText(this.getActivity(), "inject view successfully", Toast.LENGTH_SHORT).show();
    }

    @ViewInject
    private void inject(View view) {
    }

}
