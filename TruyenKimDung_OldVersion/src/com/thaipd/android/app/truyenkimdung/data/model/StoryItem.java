package com.thaipd.android.app.truyenkimdung.data.model;

import com.thaipd.android.app.truyenkimdung.data.AppUtils;

public class StoryItem {

	private int storyID;
	private String story_name;
	private int imageId;
	private String rootPath;
	private String subPath;
	
	public StoryItem() {
		
	}
	
	public StoryItem(String name, int storyId, int imageId, String desc) {
		this.story_name = name;
		this.storyID = storyId;
		this.imageId = imageId;
		this.subPath = desc;
		this.rootPath = AppUtils.ASSET_PATH + "/" + desc + "/"; // example : file:///android_asset/anhhungxadieu
	}

	public int getStoryId() {
		return storyID;
	}

	public String getStory_name() {
		return story_name;
	}
	
	public String getSubpath() {
		return subPath;
	}

	public int getImageId() {
		return imageId;
	}
	
	public String getRootPath() {
		return rootPath;
	}

	
}
