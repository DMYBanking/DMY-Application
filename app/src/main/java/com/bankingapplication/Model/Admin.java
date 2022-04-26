package com.bankingapplication.Model;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Class for Admin user
 */
public class Admin extends User
{
	private String firstName;
	private String lastName;
	private String country;
	//private String username;
	//private String password;
	private ArrayList<Profile> users;
	//private long dbId;

	public Admin(String firstName, String lastName,String country, String username, String password)
	{
		super(username,password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.users = new ArrayList<>();
	}
	public Admin(String firstName, String lastName,String country,String username,String password, long dbId)
	{
		super(username, password,dbId);
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
	}
	public String getCountry() {return country;}
	public void setCountry(String country) {this.country = country;}
	public String getFirstName() {return firstName;}
	public void setFirstName(String firstName) {this.firstName = firstName;}
	public String getLastName() {return lastName;}
	public void setLastName(String lastName) {this.lastName = lastName;}
//	public String getUsername() {return username;}
//	public void setUsername(String username) {this.username = username;}
//	public String getPassword() {return password;}
//	public void setPassword(String password) {this.password = password;}
	public ArrayList<Profile> getUsers() {return users;}
	public void setUsers(ArrayList<Profile> users) {this.users = users;}
//	public long getDbId() {return dbId;}
//	public void setDbId(long dbId) {this.dbId = dbId;}

	@Override
	public String toString()
	{
		return getFirstName() + " " + getLastName();
	}
}
