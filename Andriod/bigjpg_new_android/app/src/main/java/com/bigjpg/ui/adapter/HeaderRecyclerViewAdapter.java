package com.bigjpg.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.bigjpg.ui.viewholder.BaseListViewHolder;

import java.util.List;

/**
 * 描述:HeaderRecyclerViewAdapter
 *
 * @Author mfx
 * @Created at 2016年01月07日 11:06
 */
public abstract class HeaderRecyclerViewAdapter<T, VH extends BaseListViewHolder<T>> extends BaseRecyclerViewAdapter <T, VH> {

    public HeaderRecyclerViewAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == RecyclerViewType.HEADER){
            return onCreateHeaderViewHolder(parent, viewType);
        }else{
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(position == 0 && holder.getItemViewType() == RecyclerViewType.HEADER){
            onBindHeaderView(holder, position);
        }else{
            bindItemClickListener(holder);
            onBindItemViewHolder((VH)holder, position);
        }
    }

    @Override
    /**
     * when useHeader return true，use getBasicItemCount replace getItemCount to calculate itemcount
     */
    public int getItemCount() {
        int itemCount = getDataItemCount();
        if(useHeader()){
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && useHeader()){
            return RecyclerViewType.HEADER ;
        }else{
            return RecyclerViewType.ITEM;
        }
    }

    public abstract boolean useHeader();

    public abstract VH onCreateHeaderViewHolder(ViewGroup viewGroup, int viewType);

    public abstract void onBindHeaderView(VH holder, int position);

    public abstract VH onCreateItemViewHolder(ViewGroup viewGroup, int viewType);

    public abstract void onBindItemViewHolder(VH holder, int position);


}
