package com.bankingapplication.Model;

import com.bankingapplication.Model.db.ApplicationDB;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class Profile for profile users
 * extended by User Class
 */
public class Profile extends User
{
    private String firstName;
    private String lastName;
    private String country;
    //    private String username;
//    private String password;
    private ArrayList<Account> accounts;
    private ArrayList<Payee> payees;
//    private long dbId;

    /**
     * Constructor for creating a profile class user without setting the dbId field
     * dbId field will be set later on
     * @param firstName
     * @param lastName
     * @param country
     * @param username
     * @param password
     */
    public Profile (String firstName, String lastName, String country, String username, String password)
    {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        accounts = new ArrayList<>();
        payees = new ArrayList<>();
    }

    /**
     * Constructor for creating a profile user with the dbId
     * @param firstName
     * @param lastName
     * @param country
     * @param username
     * @param password
     * @param dbId - the key for Users Table
     */
    public Profile (String firstName, String lastName, String country, String username, String password, long dbId)
    {
        super(username, password,dbId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        //this.dbId = dbId;
    }

    /**
     * getters used to access the private fields of the profile
     */
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getCountry() {
        return country;
    }
//    public String getUsername() {
//        return username;
//    }
//    public String getPassword() {
//        return password;
//    }
    public int get_accounts_counter(){return accounts.size();}
    public ArrayList<Account> getAccounts() { return accounts; }
    public ArrayList<Payee> getPayees() { return payees; }
//    public long getDbId() { return dbId; }
//    public void setDbId(long dbId) { this.dbId = dbId; }
//
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public void setCountry(String country) {this.country = country;}
//
//    public void setUsername(String username) {this.username = username;}
//
//    public void setPassword(String password) {this.password = password;}

    /**
     * Method to add account to this profile user
     * @param accountName - account of the profile
     * @param accountBalance - account balance (current amount in account)
     */
    public void addAccount(String accountName, double accountBalance)
    {
        Random random = new Random();
        StringBuilder accNo= new StringBuilder();
        /*
            generate random account number
         */
        for(int i = 0 ;i<8;i++)
        {
            int n = random.nextInt(10);
            accNo.append(n);
        }
        Account account = new Account(accountName, accNo.toString(), accountBalance);
        accounts.add(account);
    }
    public void setAccountsFromDB(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Method to implement the transfer logic
     * and adds the transfer to profile's transactions
     * @param sendingAcc - account of sending profile
     * @param receivingAcc - account of receiving profile
     * @param transferAmount - amount to transfer to receiving account

     */
    public void addTransferTransaction(Account sendingAcc,Account receivingAcc, double transferAmount)
    {
        sendingAcc.setAccountBalance(sendingAcc.getAccountBalance() - transferAmount);
        receivingAcc.setAccountBalance(receivingAcc.getAccountBalance() + transferAmount);

        int sendingAccTransferCount = 0;
        int receivingAccTransferCount = 0;
        for (int i = 0; i < sendingAcc.getTransactions().size(); i ++) {
            if (sendingAcc.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
                sendingAccTransferCount++;
            }
        }
        for (int i = 0; i < receivingAcc.getTransactions().size(); i++) {
            if (receivingAcc.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
                receivingAccTransferCount++;
            }
        }

        sendingAcc.getTransactions().add(new Transaction("T" + (sendingAcc.getTransactions().size() + 1) + "-T" + (sendingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
        receivingAcc.getTransactions().add(new Transaction("T" + (receivingAcc.getTransactions().size() + 1) + "-T" + (receivingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
    }

    /**
     * Add payee to profile's list of payees
     * @param payeeName
     */
    public void addPayee(String payeeName) {
        String payeeID = "P" + (payees.size() + 1);
        Payee payee = new Payee(payeeID, payeeName);
        payees.add(payee);
    }

    public void setPayeesFromDB(ArrayList<Payee> payees) {
        this.payees = payees;
    }

    @Override
    public String toString()
    {
        return getFirstName() + " " + getLastName();
    }
}
