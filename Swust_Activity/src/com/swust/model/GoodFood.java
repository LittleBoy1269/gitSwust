package com.swust.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class GoodFood implements Serializable{
	private int foodid;
	private User user;
	private String name;
	private String foodType;
	private String personPrice;
	private String place;
	private String des;
	private String url;
	private Byte type;
	private Byte status;
	private Integer commentsum;

	

	public Integer getCommentsum() {
		return commentsum;
	}

	public void setCommentsum(Integer commentsum) {
		this.commentsum = commentsum;
	}

	public GoodFood() {

	}

	public GoodFood(User user, String name,
			String foodType, String personPrice,String place,String des,Byte type,Byte status,String url) {
		super();
		this.user = user;
		this.name = name;
		this.foodType = foodType;
		this.personPrice = personPrice;
		this.place=place;
		this.des = des;
		this.type=type;
		this.status=status;
		
		
	}

	public int getFoodid() {
		return foodid;
	}

	public void setFoodid(int foodid) {
		this.foodid = foodid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public String getPersonPrice() {
		return personPrice;
	}

	public void setPersonPrice(String personPrice) {
		this.personPrice = personPrice;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	

	

	

}
