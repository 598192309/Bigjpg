package com.bigjpg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bigjpg.R;
import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.ui.viewholder.BaseTaskViewHolder;
import com.bigjpg.ui.viewholder.EnlargeTaskViewHolder;
import com.bigjpg.ui.viewholder.HeaderTaskViewHolder;

import java.util.List;

/**
 * 放大adapter
 *
 * @author Momo
 * @date 2019-04-11 16:57
 */
public class EnlargeTaskListAdapter extends HeaderRecyclerViewAdapter<EnlargeConfig, BaseTaskViewHolder> {

    private LayoutInflater mInflater;
    private EnlargeTaskViewHolder.OnEnlargeTaskButtonClickListener mListener;
    private HeaderTaskViewHolder mHeaderTaskViewHolder;

    public EnlargeTaskListAdapter(Context context, List<EnlargeConfig> data, HeaderTaskViewHolder headerTaskViewHolder, EnlargeTaskViewHolder.OnEnlargeTaskButtonClickListener listener) {
        super(context, data);
        mHeaderTaskViewHolder = headerTaskViewHolder;
        mListener = listener;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public boolean useHeader() {
        return true;
    }

    @Override
    public BaseTaskViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup, int viewType) {
        return mHeaderTaskViewHolder;
    }

    @Override
    public void onBindHeaderView(BaseTaskViewHolder holder, int position) {

    }

    @Override
    public BaseTaskViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        EnlargeTaskViewHolder viewHolder = new EnlargeTaskViewHolder(mInflater.inflate(R.layout.listitem_enlarge_task, viewGroup, false));
        viewHolder.setOnEnlargeTaskButtonClick(mListener);
        return viewHolder;
    }

    @Override
    public void onBindItemViewHolder(BaseTaskViewHolder holder, int position) {
        if (useHeader()) {
            holder.bindViewData(getItem(position - 1), position);
        } else {
            holder.bindViewData(getItem(position), position);
        }
    }

}
