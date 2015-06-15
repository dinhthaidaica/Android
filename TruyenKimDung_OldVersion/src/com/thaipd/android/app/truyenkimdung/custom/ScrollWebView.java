package com.thaipd.android.app.truyenkimdung.custom;

import com.thaipd.android.app.truyenkimdung.ReadingPageActivity.OnScrollChangedCallback;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ScrollWebView extends WebView {
	
	private OnScrollChangedCallback mOnScrollChangedCallback;
	
	public ScrollWebView(final Context context) {
		super(context);
	}
	
	public ScrollWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if(mOnScrollChangedCallback != null) 
			mOnScrollChangedCallback.onScroll(t, oldt);
	}
	
	public OnScrollChangedCallback getOnScrollChangedCallback() {
		return mOnScrollChangedCallback;
	}

	public void setOnScrollChangedCallback(
			final OnScrollChangedCallback onScrollChangedCallback) {
		mOnScrollChangedCallback = onScrollChangedCallback;
	}
}
