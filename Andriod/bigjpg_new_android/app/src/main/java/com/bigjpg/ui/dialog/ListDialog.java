package com.bigjpg.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.bigjpg.R;
import com.bigjpg.ui.base.BaseDialog;
import com.bigjpg.util.AppUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 描述:列表对话框
 * 
 * @author mfx
 * @date 2014年6月10日 下午9:26:38
 */
public class ListDialog extends BaseDialog {

	@BindView(R.id.dialog_list)
	ListView mListView;

	private Activity mActivity;

	public ListDialog(Activity activity) {
		super(activity);
		mActivity = activity;
		setContentView(R.layout.dialog_listview);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.x = 0;
		lp.y = 0;
		int[] screen = AppUtil.getScreenSize(activity);
		lp.width = (int) (Math.min(screen[0], screen[1]) * 0.8f);
		getWindow().setAttributes(lp);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

	public void setData(String[] items) {
		setData(Arrays.asList(items));
	}

	public void setData(List<String> items) {
		if (mListView != null) {
			ListDialogAdapter adapter = new ListDialogAdapter(mActivity, items);
			mListView.setAdapter(adapter);
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		if (mListView != null) {
			mListView.setOnItemClickListener(listener);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
	}

	static public class ListDialogAdapter extends BaseAdapter {

		private Context context;
		private List<String> data;

		public ListDialogAdapter(Context context, List<String> data) {
			this.context = context;
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.listitem_dialog, null);
            }

			if (convertView.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) convertView.getTag();
			} else {
                viewHolder = new ViewHolder();
                viewHolder.textView = convertView.findViewById(R.id.listitem_dialog_item);
                convertView.setTag(viewHolder);
			}

			viewHolder.textView.setText(data.get(position));
			return convertView;
		}
	}

	static class ViewHolder {
		TextView textView;
	}

}
