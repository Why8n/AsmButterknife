package com.yn.asmbutterknife.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yn.annotations.BindView;
import com.yn.annotations.OnClick;
import com.yn.asmbutterknife.R;

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

        public ViewHolder(View itemView) {
            super(itemView);
            //TODO:: test
            this.tv = itemView.findViewById(R.id.item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder.this.onClick();
                }
            });
        }

        @OnClick(R.id.itemView)
        public void onClick() {
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