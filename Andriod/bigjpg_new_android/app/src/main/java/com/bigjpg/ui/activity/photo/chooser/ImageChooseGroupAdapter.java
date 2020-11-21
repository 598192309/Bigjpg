package com.bigjpg.ui.activity.photo.chooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.ui.viewholder.BaseListViewHolder;
import com.bigjpg.util.FrescoLoader;
import com.bigjpg.util.ResourcesUtil;
import com.bigjpg.util.ViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;

/**
 * ImageChooseGroupAdapter
 *
 * @author Momo
 * @date 2018-02-24 11:34
 */
public class ImageChooseGroupAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageGroup> mDataList;
    private OnImageGroupClickListener mOnImageGroupClickListener;

    public ImageChooseGroupAdapter(Context context, List<ImageGroup> list) {
        mDataList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ImageGroup getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem_image_group, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ImageGroup item = getItem(position);
        holder.bindViewData(item, position);
        return view;
    }

    public void setOnImageGroupClickListener(OnImageGroupClickListener listener) {
        mOnImageGroupClickListener = listener;
    }

    class ViewHolder extends BaseListViewHolder<ImageGroup> {

        @BindView(R.id.group_image)
        SimpleDraweeView sdvImage;
        @BindView(R.id.group_name)
        TextView tvName;
        @BindView(R.id.group_count)
        TextView tvCount;
        @BindView(R.id.group_check)
        ImageView check;
        int position;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnImageGroupClickListener != null) {
                        mOnImageGroupClickListener.onImageGroupClick(position);
                    }
                }
            });
        }

        @Override
        public void bindViewData(ImageGroup item, int dataPosition) {
            this.position = dataPosition;
            String path = item.getFirstImgPath();
            // 标题
            tvName.setText(item.getDirName());
            // 计数
            tvCount.setText(ResourcesUtil.getString(mContext, R.string.image_count, String.valueOf(item.getImageCount())));
            sdvImage.setTag(path);
            FrescoLoader.loadImageFromFile(sdvImage, path, 200, 200);
            ViewUtil.setViewVisible(check, item.isChecked());
            if (position == 0) {
                ViewUtil.setViewVisible(tvCount, false);
            } else {
                ViewUtil.setViewVisible(tvCount, true);
            }
        }

    }

    public interface OnImageGroupClickListener {
        void onImageGroupClick(int position);
    }

}
