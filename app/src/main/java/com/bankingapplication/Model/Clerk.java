package com.bankingapplication.Model;

import java.util.ArrayList;

public class Clerk extends User
{
	private String firstName;
	private String lastName;
	private String country;
//	private String username;
//	private String password;
	private ArrayList<Profile> users;
	private ArrayList<Transaction> loansToApprove;
//	private long dbId;

	public Clerk(String firstName, String lastName,String country,String username, String password)
	{
		super(username,password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.users = new ArrayList<>();
		this.loansToApprove = new ArrayList<>();
	}

	public Clerk(String firstName, String lastName,String country, String username, String password, long dbId)
	{
		super(username, password,dbId);
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
//		this.dbId = dbId;
	}

	// getters and setters
	public String getFirstName() {return firstName;}
	public void setFirstName(String firstName) {this.firstName = firstName;}
	public String getLastName() {return lastName;}
	public void setLastName(String lastName) {this.lastName = lastName;}
//	public String getUsername() {return username;}
//	public void setUsername(String username) {this.username = username;}
//	public String getPassword() {return password;}
//	public void setPassword(String password) {this.password = password;}
	public ArrayList<Profile> getUsers() {return users;}
	public ArrayList<Transaction> getLoansToApprove() {return loansToApprove;}
	public void setLoansToApprove(ArrayList<Transaction> loansToApprove) {this.loansToApprove = loansToApprove;}
	public void setUsers(ArrayList<Profile> users) {this.users = users;}
	public String getCountry() {return country;}
	public void setCountry(String country) {this.country = country;}
//	public long getDbId() {return dbId;}
//	public void setDbId(long dbId) {this.dbId = dbId;}

	// methods
	//TODO: need to implement clerk methods
	public void addLoanTransaction(Account destinationAccount,double amount)
	{
//		destinationAccount.setAccountBalance(destinationAccount.getAccountBalance() + amount);
		int receivingAccTransferCount = 0;
		for (int i = 0; i < destinationAccount.getTransactions().size(); i ++)
		{
			if (destinationAccount.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.LOAN)
			{
				receivingAccTransferCount++;
			}
		}
		destinationAccount.getTransactions().add(new Transaction("T" + (destinationAccount.getTransactions().size() + 1) + "-L" + (receivingAccTransferCount+1), destinationAccount, amount));
		addLoanForPending(destinationAccount.getTransactions().get(destinationAccount.getTransactions().size()));
	}

	public void addLoanForPending(Transaction pendingLoan)
	{
		loansToApprove.add(pendingLoan);
	}

	@Override
	public String toString()
	{
		return getFirstName() + " " + getLastName();
	}
}
