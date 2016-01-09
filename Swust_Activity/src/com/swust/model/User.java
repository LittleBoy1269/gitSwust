package com.swust.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable{
	private int userid;
	private String number;
	private String password;
	
	private Set personCalender = new HashSet(0);
	
	
	public Set getPersonCalender() {
		return personCalender;
	}
	public void setPersonCalender(Set personCalender) {
		this.personCalender = personCalender;
	}
	public User(){
		
	}
	public User(int userid,String number,String password){
		super();
		this.userid=userid;
		this.number=number;
		this.password=password;
	}
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
