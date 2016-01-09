package com.swust.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class FoodRecord implements Serializable{
	private int recordid;
	private GoodFood goodFood;
	private User user;
	private int type;

	public FoodRecord() {

	}

	public FoodRecord(GoodFood goodFood,User user,int type) {
		super();
		this.goodFood=goodFood;
		this.user=user;
		this.type=type;
		
	}

	public int getRecordid() {
		return recordid;
	}

	public void setRecordid(int recordid) {
		this.recordid = recordid;
	}

	public GoodFood getGoodFood() {
		return goodFood;
	}

	public void setGoodFood(GoodFood goodFood) {
		this.goodFood = goodFood;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	

	

}
