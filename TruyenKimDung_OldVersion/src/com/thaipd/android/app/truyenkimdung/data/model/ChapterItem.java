package com.thaipd.android.app.truyenkimdung.data.model;

import com.thaipd.android.app.truyenkimdung.data.AppUtils;

public class ChapterItem {
	
	int storyId;
	int chapterId;
	String title;
	String desc;
	String path;
	public int getStoryId() {
		return storyId;
	}
	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}
	public int getChapterId() {
		return chapterId;
	}
	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return "<font color=\"red\">" + title + "</font>" + " : " + desc;
	}
}
