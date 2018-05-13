package com.yn.asmbutterknife.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yn.asmbutterknife.R;
import com.yn.asmbutterknife.annotations.BindView;
import com.yn.asmbutterknife.annotations.OnClick;
import com.yn.asmbutterknife.annotations.ViewInject;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<String> mData;

    public RecyclerAdapter(List<String> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setText(mData.get(position));
        holder.setTag(mData.get(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item)
        private TextView tv;

//        @ViewInject
        public ViewHolder(View item) {
            super(item);
            this.viewinject(item);
            //TODO:: test
//            this.tv = item.findViewById(R.id.item);
//            this.tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ViewHolder.this.onClick();
//                }
//            });
        }

        @ViewInject
        private void viewinject(View view){

        }

        @OnClick(R.id.item)
        private void onClick() {
            Toast.makeText(this.tv.getContext(), (String) this.tv.getTag(), Toast.LENGTH_SHORT).show();
        }

        public void setText(String text) {
            this.tv.setText(text);
        }

        public void setTag(Object tag) {
            this.tv.setTag(tag);
        }
    }
}
