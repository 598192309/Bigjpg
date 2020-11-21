package com.bigjpg.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.bigjpg.ui.listener.OnRecyclerViewItemClickListener;
import com.bigjpg.ui.viewholder.BaseListViewHolder;

import java.util.List;

/**
 * 描述:BaseRecyclerViewAdapter
 *
 * @Author mfx
 * @Created at 2015年12月31日 18:24
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends BaseListViewHolder<T>> extends RecyclerView.Adapter<VH>{

    private List<T> mData;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private LayoutInflater mInflater;

    public BaseRecyclerViewAdapter(Context context, List<T> data){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    public Context getContext(){
        return  mContext;
    }

    protected LayoutInflater getInflater(){
        return mInflater;
    }

    public List<T> getData(){
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public int getDataItemCount(){
        return mData.size();
    }

    public T getItem(int position){
        return mData.get(position);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        bindItemClickListener(holder);
        T data = getItem(position);
        holder.bindViewData(data, position);
    }

    public void bindItemClickListener(VH holder){
        holder.setOnRecyclerViewItemClickListener(mOnRecyclerViewItemClickListener);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnRecyclerViewItemClickListener = listener;
    }

    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener(){
        return mOnRecyclerViewItemClickListener;
    }

}
