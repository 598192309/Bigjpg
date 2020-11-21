package com.bigjpg.ui.activity.photo.chooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bigjpg.R;
import com.bigjpg.ui.viewholder.BaseListViewHolder;
import com.bigjpg.util.FrescoLoader;
import com.bigjpg.util.ViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ImageListBundle> mDataList;
    private OnImageCheckedListener mOnImageCheckedListener;
    private boolean mEnableCheckBox;

    public ImageListAdapter(Context context, ArrayList<ImageListBundle> list, boolean enableCheckbox) {
        mContext = context;
        mDataList = list;
        mEnableCheckBox = enableCheckbox;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ImageListBundle getItem(int position) {
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
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.griditem_image_chooser_detail, null);
            holder = new ViewHolder(view, mEnableCheckBox);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.bindViewData(getItem(position), position);
        return view;
    }

   public class ViewHolder extends BaseListViewHolder<ImageListBundle> {

        @BindView(R.id.list_item_iv)
        SimpleDraweeView image;
        @BindView(R.id.list_checkbox)
        View checkBox;
        @BindView(R.id.list_overlay)
        View overlay;

        ImageListBundle data;
        boolean enableCheckBox;
        int position;

        public ViewHolder(View view, boolean enableCheckBox) {
            super(view);
            this.enableCheckBox = enableCheckBox;
            ViewUtil.setViewVisible(checkBox, enableCheckBox);
        }

        public void bindViewData(ImageListBundle data, int position) {
            this.data = data;
            this.position = position;
            FrescoLoader.loadImageFromFile(image, data.imagePath, 200, 200);
            ViewUtil.setViewVisible(overlay, data.isChecked);
            if(data.isChecked){
                checkBox.setBackgroundResource(R.drawable.ic_check);
            }else{
                checkBox.setBackgroundResource(R.drawable.ic_uncheck);
            }
        }


        @OnClick(R.id.list_checkbox)
        void onCheckClick(View view) {
            if(mOnImageCheckedListener != null){
                mOnImageCheckedListener.onImageCheckChanged(this, data, position);
            }
        }


   }

    public void setOnImageCheckedListener(OnImageCheckedListener listener){
        mOnImageCheckedListener = listener;
    }

    public interface OnImageCheckedListener{
        void onImageCheckChanged(ViewHolder viewHolder, ImageListBundle data, int position);
    }

}
