package com.swust.model;

import com.swust.model.GoodFood;

public class GoodFoodList {

	private GoodFood goodFood;
	private Integer commentSum;
	
	public GoodFoodList() {

	}
	
	public GoodFoodList(GoodFood goodFood,Integer commentSum){
		super();
		this.goodFood=goodFood;
		this.commentSum=commentSum;
	}
	public GoodFood getGoodFood() {
		return goodFood;
	}
	public void setGoodFood(GoodFood goodFood) {
		this.goodFood = goodFood;
	}
	public Integer getCommentSum() {
		return commentSum;
	}
	public void setCommentSum(Integer commentSum) {
		this.commentSum = commentSum;
	}
}
