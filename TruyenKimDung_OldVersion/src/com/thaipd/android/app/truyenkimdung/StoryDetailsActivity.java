package com.thaipd.android.app.truyenkimdung;

import java.util.List;

import com.android.thaipd.log.TLog;
import com.thaipd.android.app.truyenkimdung.data.AppUtils;
import com.thaipd.android.app.truyenkimdung.data.model.ChapterItem;
import com.thaipd.android.app.truyenkimdung.data.model.StoryItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StoryDetailsActivity extends BaseActivity {
	
	private ListView mListView;
	private CustomChapterAdapter mAdapter;
	StoryItem mStoryItem;
	private boolean isUpdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mStoryItem = AppUtils.getStoryItem();
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View content = inflater.inflate(R.layout.layout_chapter_list, null);
		
		content.setBackgroundResource(mStoryItem.getImageId());
		
		
		mListView = (ListView) content.findViewById(R.id.listview_chapters);
		setContentView(content);
		
		List<ChapterItem> data = null;
		
		
		
		if (mStoryItem != null) {
			String path = mStoryItem.getSubpath();
			if (path != null && path.length() > 0) {
				data = AppUtils.getAllChapter(this);
			}
		}
		
		if (data != null && data.size() > 0) {
			mAdapter = new CustomChapterAdapter(this, data);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(mAdapter);
		}
		
	}

	@Override
	protected void onResume() {
		if (isUpdate && mAdapter != null) {
			mAdapter.notifyDataSetChanged();
			isUpdate = !isUpdate;
		}
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		isUpdate = true;
		super.onPause();
	}
	
	@SuppressLint("NewApi")
	private class CustomChapterAdapter extends BaseAdapter implements OnItemClickListener{
		private List<ChapterItem> data;
		private Context mContext;
		
		public CustomChapterAdapter(Context context, List<ChapterItem> data) {
			this.data = data;
			this.mContext = context;
		}
		
		@Override
		public int getCount() {
			if (data != null && data.size() > 0) {
				return data.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (data != null && data.size() > 0) {
				return data.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChapterItem item = data.get(position);
			View view = convertView;
			ChapterHolder holder = null;
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			if (view == null) {
				view = inflater.inflate(R.layout.layout_chapter_item, parent, false);
				holder = new ChapterHolder();
				
				holder.title = (TextView) view.findViewById(R.id.tv_chapter_number);
				holder.details = (TextView) view.findViewById(R.id.tv_chapter_desc);
				
				view.setTag(holder);
			}
			holder = (ChapterHolder) view.getTag();
			
			if ((AppUtils.ASSET_PATH + "/" + item.getPath()).equals(getPref(mStoryItem.getSubpath()))) {
				TLog.d(AppUtils.ASSET_PATH + "/" + item.getPath() + "P == " + position);
				view.setBackground(mContext.getResources().getDrawable(R.drawable.bg_read_chapter));
				view.setAlpha(0.7f);
			} else {
				view.setBackground(null);
			}
			holder.title.setText(item.getTitle());
			String str = item.getDesc();
			if (str != null && str.length() > 0) {
				holder.details.setText(str.toUpperCase());
			}
			
			return view;
		}
	
		
		class ChapterHolder {
			TextView title;
			TextView details;
		}


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ChapterItem item = data.get(position);
			if (item != null && item.getPath() != null) {
				Intent i = new Intent(mContext, ReadingPageActivity.class);
				i.putExtra(AppUtils.INTENT_SUB_PATH, AppUtils.ASSET_PATH + "/" + item.getPath());
				mContext.startActivity(i);
			}
			
		}
		
	}
	
}
