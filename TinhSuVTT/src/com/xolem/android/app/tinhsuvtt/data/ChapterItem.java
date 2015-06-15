package com.xolem.android.app.tinhsuvtt.data;

import nl.siegmann.epublib.domain.TOCReference;

public class ChapterItem {
	private int id;
	private String title;
	private TOCReference mTOC;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public TOCReference getmTOC() {
		return mTOC;
	}
	public void setmTOC(TOCReference mTOC) {
		this.mTOC = mTOC;
	}
	
}
