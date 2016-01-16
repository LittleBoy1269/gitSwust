package com.entity;

import android.widget.ImageView;

public class MyEntity {
	private ImageView title_img;
	private ImageView like_img;
	private String title;
	private String content;
	public ImageView getTitle_img() {
		return title_img;
	}
	public void setTitle_img(ImageView title_img) {
		this.title_img = title_img;
	}
	public ImageView getLike_img() {
		return like_img;
	}
	public void setLike_img(ImageView likePressed) {
		this.like_img = likePressed;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
