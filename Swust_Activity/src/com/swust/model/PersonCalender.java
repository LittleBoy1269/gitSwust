package com.swust.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class PersonCalender implements Serializable{
	private int calenderid;
	private User user;
	private Timestamp dayTime;
	private String name;
	private String des;
	private Byte status;

	public PersonCalender() {

	}

	public PersonCalender(int calenderid, User user, Timestamp dayTime,
			String name, String des,Byte status) {
		super();
		this.calenderid = calenderid;
		this.user = user;
		this.dayTime = dayTime;
		this.name = name;
		this.des=des;
		this.status = status;
		
		
		
	}
	
	

	public int getCalenderid() {
		return calenderid;
	}

	public void setCalenderid(int calenderid) {
		this.calenderid = calenderid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	public Timestamp getDayTime() {
		return dayTime;
	}

	public void setDayTime(Timestamp dayTime) {
		this.dayTime = dayTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	

}
