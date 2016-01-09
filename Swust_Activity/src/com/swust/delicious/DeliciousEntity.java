package com.swust.delicious;

import android.graphics.Bitmap;

public class DeliciousEntity {
	public int foodid;
	public String title;
	public String place;
	public String average;
	public String keyword;
	public String detail;
	public String imgUrl;
	public Integer commentSum;
	
	public Integer getcommentSum(){
		return commentSum;
	}
	public void setcommentSum(Integer commentSum){
		this.commentSum=commentSum;
	}
	public int getFoodid(){
		return foodid;
	}
	public void setFoodid(int foodid){
		this.foodid=foodid;
	}
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
	public String getKeyword()
	{
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getAverage() {
		return average;
	}
	public void setAverage(String average) {
		this.average = average;
	}
}
