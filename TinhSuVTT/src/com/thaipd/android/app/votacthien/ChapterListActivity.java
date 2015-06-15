package com.thaipd.android.app.votacthien;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xolem.android.app.tinhsuvtt.R;
import com.xolem.android.app.tinhsuvtt.data.ChapterItem;

public class ChapterListActivity extends BaseActivity {
	
	private ListView mListView;
	private CustomChapterAdapter mAdapter;
	private boolean isUpdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initDa();
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View content = inflater.inflate(R.layout.layout_chapter_list, null);
		
		mAdapter = new CustomChapterAdapter(this, chapterData);
		mListView = (ListView) content.findViewById(R.id.listview_chapters);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mAdapter);
		setContentView(content);
		
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
			
			if (position == getPrefInt(ReadingPageActivity.DATA_PAGE)) {
				view.setBackground(mContext.getResources().getDrawable(R.drawable.bg_read_chapter));
			} else {
				view.setBackground(null);
			}
			holder = (ChapterHolder) view.getTag();
			
			holder.title.setText(item.getTitle());
			String tile = AppUtils.getInstance(mContext).getChapterName(getChapContent(item.getmTOC()));
			holder.details.setText(Html.fromHtml("<b>" + tile + "</b>"));
			
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
			if (item != null && item.getmTOC() != null) {
				Intent i = new Intent(mContext, ReadingPageActivity.class);
				i.putExtra(AppUtils.INTENT_POSITION, position);
				mContext.startActivity(i);
			}
			
		}
		
	}
	
}
