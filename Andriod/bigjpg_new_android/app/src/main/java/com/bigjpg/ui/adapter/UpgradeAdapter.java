package com.bigjpg.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.bigjpg.R;
import com.bigjpg.model.entity.UpgradeItem;
import com.bigjpg.ui.viewholder.UpgradeItemViewHolder;
import com.bigjpg.util.ResourcesUtil;

import java.util.List;

/**
 * 升级adapter
 *
 * @author Momo
 * @date 2019-04-19 18:24
 */
public class UpgradeAdapter extends BaseRecyclerViewAdapter<UpgradeItem, UpgradeItemViewHolder> {

    private UpgradeItemViewHolder.OnPayClickListener mListener;
    private Context mContext;

    public UpgradeAdapter(Context context, List<UpgradeItem> data, UpgradeItemViewHolder.OnPayClickListener listener) {
        super(context, data);
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public UpgradeItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        UpgradeItemViewHolder viewHolder = new UpgradeItemViewHolder(ResourcesUtil.inflate(mContext, R.layout.listitem_upgrade, viewGroup, false));
        viewHolder.setOnPayClickListener(mListener);
        return viewHolder;
    }
}
