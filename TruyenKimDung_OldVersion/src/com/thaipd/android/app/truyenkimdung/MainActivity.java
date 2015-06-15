package com.thaipd.android.app.truyenkimdung;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.squareup.picasso.Picasso;
import com.thaipd.android.app.truyenkimdung.custom.StoryListAdapter;
import com.thaipd.android.app.truyenkimdung.data.AppUtils;
import com.thaipd.android.app.truyenkimdung.data.Stories;
import com.thaipd.android.app.truyenkimdung.data.model.StoryItem;

public class MainActivity extends BaseActivity implements OnClickListener {
	
	private ListView mListView;
	private View mListTouchInterceptor;
	private View mDetailsLayout;
	private UnfoldableView mUnfoldableView;
	
	private ImageView storyImage;
	private WebView mWebView;
	private Button btn_start;
	private Button btn_continue;
	private StoryItem mStoryItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unfoldable_list_story);
		
		mListView = (ListView) findViewById(R.id.lv_list_story);
		mListView.setAdapter(new StoryListAdapter(this));
		
		mListTouchInterceptor = (View) findViewById(R.id.touch_interceptor_view);
		mListTouchInterceptor.setClickable(false);
		
		mDetailsLayout =  (View)findViewById(R.id.details_layout);
		mDetailsLayout.setVisibility(View.INVISIBLE);

		mUnfoldableView = (UnfoldableView) findViewById(R.id.unfoldable_view);
		
		storyImage = (ImageView) findViewById(R.id.details_image);
		
		mWebView = (WebView) findViewById(R.id.webView_content);
		
		btn_start = (Button) findViewById(R.id.btn_chapter_list);
		
		btn_continue = (Button) findViewById(R.id.btn_continue);
		
		Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
		mUnfoldableView.setFoldShading(new GlanceFoldShading(this, glance));
		
		mUnfoldableView
				.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
					@Override
					public void onUnfolding(UnfoldableView unfoldableView) {
						mWebView.loadUrl(mStoryItem.getRootPath() + AppUtils.INTRO_FILE);
						mListTouchInterceptor.setClickable(true);
						mDetailsLayout.setVisibility(View.VISIBLE);
					}

					@Override
					public void onUnfolded(UnfoldableView unfoldableView) {
						mListTouchInterceptor.setClickable(false);
					}

					@Override
					public void onFoldingBack(UnfoldableView unfoldableView) {
						mListTouchInterceptor.setClickable(true);
					}

					@Override
					public void onFoldedBack(UnfoldableView unfoldableView) {
						mListTouchInterceptor.setClickable(false);
						mDetailsLayout.setVisibility(View.INVISIBLE);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chapter_list:
			/*if (mStoryItem.isAvailable()) {*/
				
				if (mUnfoldableView != null
						&& (mUnfoldableView.isUnfolded() || mUnfoldableView
								.isUnfolding())) {
					mUnfoldableView.foldBack();
				}
				
				Intent i = new Intent(this, StoryDetailsActivity.class);
				
				i.putExtra(AppUtils.INTENT_SUB_PATH, mStoryItem.getSubpath());
				
				startActivity(i);

			break;
			
		case R.id.btn_continue:
			String current_path = getPref(mStoryItem.getSubpath());
			if (current_path != null && current_path.length() > 0) {
				Intent it = new Intent(this, ReadingPageActivity.class);
				it.putExtra(AppUtils.INTENT_SUB_PATH, current_path);
				startActivity(it);
			} else {
				Toast.makeText(this, getString(R.string.can_not_found_msg), Toast.LENGTH_SHORT).show();
			}
			
			break;
		default:
			break;
		}
		
		
	}
	
	@Override
	public void onBackPressed() {
		if (mResideMenu != null && mResideMenu.isOpened()) {
			mResideMenu.closeMenu();
		} else if (mUnfoldableView != null
				&& (mUnfoldableView.isUnfolded() || mUnfoldableView
						.isUnfolding())) {
			mUnfoldableView.foldBack();
		} else {
			super.onBackPressed();
		}
	}

	public void openStoryDetails(View coverView, StoryItem item) {
		
		mStoryItem = item;
		AppUtils.setStoryItem(mStoryItem);
		int storyId = mStoryItem.getStoryId();
			storyImage = (ImageView) findViewById(R.id.details_image);
			storyImage.setPadding(20, 20, 20, 20);
			mWebView = (WebView) findViewById(R.id.webView_content);
			btn_start = (Button) findViewById(R.id.btn_chapter_list);
			btn_continue = (Button) findViewById(R.id.btn_continue);
			
		if (storyId == Stories.ANH_HUNG_XA_DIEU.getCode()) {

			setButtonBackground(R.drawable.bg_btn_1);

		} else if (storyId == Stories.BACH_MA_KHIEU_TAY_PHONG.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_2);

		} else if (storyId == Stories.BICH_HUYET_KIEM.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_3);

		} else if (storyId == Stories.HIEP_KHACH_HANH.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_5);
			
		} else if (storyId == Stories.LIEN_THANH_QUYET.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_4);
			
		} else if (storyId == Stories.LOC_DINH_KY.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_5);
			
		} else if (storyId == Stories.PHI_HO_NGOAI_TRUYEN.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_4);
			
		} else if (storyId == Stories.THAN_DIEU_HIEP_LU.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_4);
			
		} else if (storyId == Stories.THIEN_LONG_BAT_BO.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_4);
			
		} else if (storyId == Stories.THU_KIEM_AN_CUU_LUC.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_4);
			
		} else if (storyId == Stories.TUYET_SON_PHI_HO.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_4);
			
		} else if (storyId == Stories.UYEN_UONG_DAO.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_4);
			
		} else if (storyId == Stories.VIET_NU_KIEM.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_3);
			
		} else if (storyId == Stories.Y_THIEN_DO_LONG_KY.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_5);
			
		} else if (storyId == Stories.TIEU_NGAO_GIANG_HO.getCode()) {
			
			setButtonBackground(R.drawable.bg_btn_3);
			
		}
			
//		}
		btn_start.setOnClickListener(this);
		btn_continue.setOnClickListener(this);
		
		Picasso.with(this).load(item.getImageId()).into(storyImage);

//		mWebView.loadUrl(item.getRootPath() + AppUtils.INTRO_FILE);
		
		mUnfoldableView.unfold(coverView, mDetailsLayout);
	}
	
	private void setButtonBackground(int resourceId) {
		btn_start.setBackgroundResource(resourceId);
		btn_continue.setBackgroundResource(resourceId);
	}
}
