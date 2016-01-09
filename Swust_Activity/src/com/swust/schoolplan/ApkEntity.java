package com.swust.schoolplan;

import android.graphics.Bitmap;

public class ApkEntity {
	public String title;
	public String person;
	public String time;
	public String place;
	public String detail;
	public String imgUrl;
	public String getUrl() {
		return imgUrl;
	}
	public void setUrl(String imgUrl)
	{
		this.imgUrl=imgUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPlace()
	{
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDetail()
	{
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
