package com.bigjpg.ui.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


import com.bigjpg.ui.listener.OnRecyclerViewItemClickListener;

import butterknife.ButterKnife;

/**
 * BaseListViewHolder
 * @author Momo
 * @date 2017-11-08 15:38
 */
public abstract class BaseListViewHolder <T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public BaseListViewHolder(View view) {
        super(view);
        mView = view;
        view.setOnClickListener(this);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onClick(View view) {
        if (mOnRecyclerViewItemClickListener != null) {
            mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(getLayoutPosition());
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnRecyclerViewItemClickListener = listener;
    }


    abstract public void bindViewData(T data, int dataPosition);
}
