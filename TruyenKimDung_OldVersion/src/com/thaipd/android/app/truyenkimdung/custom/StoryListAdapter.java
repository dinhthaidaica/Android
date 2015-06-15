package com.thaipd.android.app.truyenkimdung.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.andrioid.commons.adapters.ItemsAdapter;
import com.squareup.picasso.Picasso;
import com.thaipd.android.app.truyenkimdung.MainActivity;
import com.thaipd.android.app.truyenkimdung.R;
import com.thaipd.android.app.truyenkimdung.data.AppUtils;
import com.thaipd.android.app.truyenkimdung.data.model.StoryItem;

public class StoryListAdapter extends ItemsAdapter<StoryItem> implements OnClickListener{
	
	private Context mContext;
	public StoryListAdapter(Context context) {
		
		super(context);
		// set data model objects
		mContext = context;
		setItemsList(AppUtils.getAllStory(mContext));
	}

	@Override
	protected View createView(StoryItem item, int pos, ViewGroup parent,
			LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.list_item, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.story_img = (ImageView) view.findViewById(R.id.story_item_image);
		holder.story_img.setOnClickListener(this);
		
		holder.story_name = (TextView) view.findViewById(R.id.story_item_name);
		
		view.setTag(holder);
		return view;
	}

	@Override
	protected void bindView(StoryItem item, int pos, View convertView) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		holder.story_img.setTag(item);
		Picasso.with(convertView.getContext()).load(item.getImageId()).noFade().into(holder.story_img);
		
		holder.story_name.setText(item.getStory_name());
	}
	
	private static class ViewHolder {
		ImageView story_img;
		TextView story_name;
	}

	@Override
	public void onClick(View view) {
		if (view.getContext() instanceof MainActivity) {
			MainActivity activity = (MainActivity) view.getContext();
			activity.openStoryDetails(view, (StoryItem) view.getTag());
		}
		
	}
	
}
