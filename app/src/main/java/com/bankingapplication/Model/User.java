package com.bankingapplication.Model;

import java.util.ArrayList;

/**
 * Class User for all users that extend from this class
 * e.g - Admin,Clerk,Profile
 */
public class User
{
//	private String firstName;
//	private String lastName;
//	private String country;
	private String username;
	private String password;
	private String picture;
	private long dbId;

	public User(String username, String password)
	{
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.country = country;
		this.username = username;
		this.password = password;
	}

	public User(String username, String password, long dbId)
	{
		this(username, password);
		this.dbId = dbId;
	}

//	public String getFirstName() {return firstName;}
//	public void setFirstName(String firstName) {this.firstName = firstName;}
//	public String getLastName() {return lastName;}
//	public void setLastName(String lastName) {this.lastName = lastName;}
//	public String getCountry() {return country;}
//	public void setCountry(String country) {this.country = country;}
	public String getUsername() {return username;}

	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setUsername(String username) {this.username = username;}
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	public long getDbId() {return dbId;}
	public void setDbId(long dbId) {this.dbId = dbId;}

	@Override
	public String toString()
	{
		return getUsername() + " - " + getDbId();
	}
}
