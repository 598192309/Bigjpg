package com.bigjpg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bigjpg.R;
import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.ui.viewholder.BaseHistoryViewHolder;
import com.bigjpg.ui.viewholder.EnlargeHistoryViewHolder;
import com.bigjpg.ui.viewholder.HeaderHistoryViewHolder;

import java.util.List;

/**
 * 记录adapter
 *
 * @author Momo
 * @date 2019-04-11 16:57
 */
public class EnlargeHistoryListAdapter extends HeaderRecyclerViewAdapter<EnlargeLog, BaseHistoryViewHolder> {

    private LayoutInflater mInflater;
    private EnlargeHistoryViewHolder.OnEnlargeLogButtonClickListener mLogClickListener;
    private HeaderHistoryViewHolder.OnBatchDownloadClickListener mListener;
    private boolean mIsCheckable;

    public EnlargeHistoryListAdapter(Context context, List<EnlargeLog> data, HeaderHistoryViewHolder.OnBatchDownloadClickListener listener, EnlargeHistoryViewHolder.OnEnlargeLogButtonClickListener enlargeLogButtonClickListener) {
        super(context, data);
        mListener = listener;
        mLogClickListener = enlargeLogButtonClickListener;
        mInflater = LayoutInflater.from(context);
    }

    public void setCheckable(boolean isCheckable) {
        mIsCheckable = isCheckable;
        notifyDataSetChanged();
    }

    @Override
    public boolean useHeader() {
        return true;
    }

    @Override
    public BaseHistoryViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup, int viewType) {
        HeaderHistoryViewHolder viewHolder = new HeaderHistoryViewHolder(mInflater.inflate(R.layout.layout_history_header, viewGroup, false));
        viewHolder.setOnBatchDownloadClickListener(mListener);
        return viewHolder;
    }

    @Override
    public void onBindHeaderView(BaseHistoryViewHolder holder, int position) {

    }

    @Override
    public BaseHistoryViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int viewType) {
        EnlargeHistoryViewHolder viewHolder = new EnlargeHistoryViewHolder(mInflater.inflate(R.layout.listitem_enlarge_history, viewGroup, false));
        viewHolder.setOnEnlargeLogButtonClickListener(mLogClickListener);
        return viewHolder;
    }

    @Override
    public void onBindItemViewHolder(BaseHistoryViewHolder holder, int position) {
        if (holder instanceof EnlargeHistoryViewHolder) {
            ((EnlargeHistoryViewHolder) holder).setCheckable(mIsCheckable);
        }
        if (useHeader()) {
            holder.bindViewData(getItem(position - 1), position);
        } else {
            holder.bindViewData(getItem(position), position);
        }
    }

}
