package com.bigjpg.ui.viewholder;

import android.view.View;

import butterknife.ButterKnife;

/**
 * BaseViewHolder
 * @author Momo
 * @date 2017-11-07 16:56
 */
public abstract class BaseViewHolder<T>{

    View mView;

    public BaseViewHolder(View view){
        mView = view;
        ButterKnife.bind(this, view);
    }

    abstract public void bindViewData(T data);
}
