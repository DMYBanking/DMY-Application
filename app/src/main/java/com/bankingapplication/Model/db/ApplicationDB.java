package com.bankingapplication.Model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bankingapplication.Model.Admin;
import com.bankingapplication.Model.Clerk;
import com.bankingapplication.Model.Profile;
import com.bankingapplication.Model.Transaction;
import com.bankingapplication.Model.Account;
import com.bankingapplication.Model.Payee;
import com.bankingapplication.Model.User;

import java.util.ArrayList;

/**
 * Class ApplicationDB
 * will handle all of the read/write operations from database
 */
public class ApplicationDB
{
    private static SQLiteOpenHelper staticOpenHelper;
    private static SQLiteDatabase staticDatabase;
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    private static final String DB_NAME = "BankingAppDB.db";
    private static final int DB_VERSION = 2;

    /**
     * Tables column names and indexes
     * can be modified from here, just change the final strings
     */
    //-------------------------------------------------------------------USER TABLE---------------------------\\
    private static final String USERS_TABLE = "Users";

    private static final String USER_ID = "_UserID";
//    private static final String USER_FNAME = "firstName";
//    private static final String USER_LNAME = "lastName";
//    private static final String USER_COUNTRY = "country";
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_PICTURE = "picture";

    private static final int USER_ID_COLUMN = 0;
//    private static final int USER_FNAME_COLUMN = 1;
//    private static final int USER_LNAME_COLUMN = 2;
//    private static final int USER_COUNTRY_COLUMN = 3;
    private static final int USER_USERNAME_COLUMN = 1;
    private static final int USER_PASSWORD_COLUMN = 2;
    private static final int USER_PICTURE_COLUMN = 3;
    //-------------------------------------------------------------------USER TABLE---------------------------\\

    //-------------------------------------------------------------------ADMIN TABLE---------------------------\\
    private static final String ADMINS_TABLE = "Admins";

    private static final String ADMIN_ID = "_AdminID";
    private static final String ADMIN_FNAME = "firstName";
    private static final String ADMIN_LNAME = "lastName";
    private static final String ADMIN_COUNTRY = "country";
//    private static final String ADMIN_USERNAME = "username";
//    private static final String ADMIN_PASSWORD = "password";

    private static final int ADMIN_ID_COLUMN = 0;
    private static final int ADMIN_FNAME_COLUMN = 1;
    private static final int ADMIN_LNAME_COLUMN = 2;
    private static final int ADMIN_COUNTRY_COLUMN = 3;
//    private static final int ADMIN_USERNAME_COLUMN = 4;
//    private static final int ADMIN_PASSWORD_COLUMN = 5;
    //-------------------------------------------------------------------ADMIN TABLE---------------------------\\

    //-------------------------------------------------------------------CLERK TABLE---------------------------\\
    private static final String CLERKS_TABLE = "Clerks";

    private static final String CLERK_ID = "_ClerkID";
    private static final String CLERK_FNAME = "firsName";
    private static final String CLERK_LNAME = "lastName";
    private static final String CLERK_COUNTRY = "country";
//    private static final String CLERK_USERNAME = "username";
//    private static final String CLERK_PASSWORD = "password";

    private static final int CLERK_ID_COLUMN = 0;
    private static final int CLERK_FNAME_COLUMN = 1;
    private static final int CLERK_LNAME_COLUMN = 2;
    private static final int CLERK_COUNTRY_COLUMN = 3;
//    private static final int CLERK_USERNAME_COLUMN = 4;
//    private static final int CLERK_PASSWORD_COLUMN = 5;
    //-------------------------------------------------------------------CLERK TABLE---------------------------\\

    //------------------------------------------------------------------- PROFILE TABLE ----------------------- \\
    private static final String PROFILES_TABLE = "Profiles";

    private static final String PROFILE_ID = "_ProfileID";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
//    private static final String EMAIL = "email";
    private static final String COUNTRY = "country";
//    private static final String USERNAME = "username";
//    private static final String PASSWORD = "password";

    private static final int PROFILE_ID_COLUMN = 0;
    private static final int FIRST_NAME_COLUMN = 1;
    private static final int LAST_NAME_COLUMN = 2;
//    private static final int EMAIL_COLUMN = 3;
    private static final int COUNTRY_COLUMN = 3;
//    private static final int USERNAME_COLUMN = 4;
//    private static final int PASSWORD_COLUMN = 5;
    //------------------------------------------------------------------- PROFILE TABLE ----------------------- \\

    //------------------------------------------------------------------- PAYEE TABLE ----------------------- \\
    private static final String PAYEES_TABLE = "Payees";

    private static final String PAYEE_ID = "_PayeeID";
    private static final String PAYEE_NAME = "PayeeName";

    private static final int PAYEE_ID_COLUMN = 1;
    private static final int PAYEE_NAME_COLUMN = 2;
    //------------------------------------------------------------------- PAYEE TABLE ----------------------- \\

    //------------------------------------------------------------------- ACCOUNT TABLE ----------------------- \\
    private static final String ACCOUNTS_TABLE = "Accounts";

    private static final String ACCOUNT_NO = "_AccountNo";
    private static final String ACCOUNT_NAME = "AccountName";
    private static final String ACCOUNT_BALANCE = "AccountBalance";

    private static final int ACCOUNT_NO_COLUMN = 1;
    private static final int ACCOUNT_NAME_COLUMN = 2;
    private static final int ACCOUNT_BALANCE_COLUMN = 3;
    //------------------------------------------------------------------- ACCOUNT TABLE ----------------------- \\

    //------------------------------------------------------------------- TRANSACTION TABLE ----------------------- \\
    private static final String TRANSACTIONS_TABLE = "Transactions";

    private static final String TRANSACTION_ID = "_TransactionID";
    private static final String TIMESTAMP = "Timestamp";
    private static final String SENDING_ACCOUNT = "SendingAccount";
    private static final String DESTINATION_PROFILE = "DestinationProfile";
    private static final String DESTINATION_ACCOUNT = "DestinationAccount";
    private static final String TRANSACTION_PAYEE = "Payee";
    private static final String TRANSACTION_AMOUNT = "Amount";
    private static final String TRANS_TYPE = "Type";

    private static final int TRANSACTION_ID_COLUMN = 2;
    private static final int TIMESTAMP_COLUMN = 3;
    private static final int SENDING_ACCOUNT_COLUMN = 4;
    private static final int DESTINATION_PROFILE_COLUMN = 5;
    private static final int DESTINATION_ACCOUNT_COLUMN = 6;
    private static final int TRANSACTION_PAYEE_COLUMN = 7;
    private static final int TRANSACTION_AMOUNT_COLUMN = 8;
    private static final int TRANSACTION_TYPE_COLUMN = 9;
    //------------------------------------------------------------------- TRANSACTION TABLE ----------------------- \\

    /**
     * Tables SQL - tables creation
     */
    //----------------------------------------CREATE USERS TABLE--------------------------------------------------\\
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + USERS_TABLE + " (" +
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_USERNAME + " TEXT, " +
            USER_PASSWORD + " TEXT, " +
            USER_PICTURE + " TEXT )";
    //----------------------------------------CREATE USERS TABLE--------------------------------------------------\\

    //----------------------------------------CREATE ADMINS TABLE--------------------------------------------------\\
    private static final String CREATE_ADMINS_TABLE =
            "CREATE TABLE " + ADMINS_TABLE + " (" +
            ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ADMIN_FNAME + " TEXT, " +
            ADMIN_LNAME + " TEXT, " +
            ADMIN_COUNTRY + " TEXT, " +
            "FOREIGN KEY(" + ADMIN_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + "))";
    //----------------------------------------CREATE ADMINS TABLE--------------------------------------------------\\

    //----------------------------------------CREATE CLERKS TABLE--------------------------------------------------\\
    private static final String CREATE_CLERKS_TABLE =
            "CREATE TABLE " + CLERKS_TABLE + "(" +
            CLERK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CLERK_FNAME + " TEXT, " +
            CLERK_LNAME + " TEXT, " +
            CLERK_COUNTRY + " TEXT, " +
            "FOREIGN KEY(" + CLERK_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + "))";
    //----------------------------------------CREATE CLERKS TABLE--------------------------------------------------\\

    //----------------------------------------CREATE PROFILES TABLE--------------------------------------------------\\
    private static final String CREATE_PROFILES_TABLE =
            "CREATE TABLE " + PROFILES_TABLE + " (" +
                    PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIRST_NAME + " TEXT, " +
                    LAST_NAME + " TEXT, " +
//                    EMAIL + "TEXT, " +
                    COUNTRY + " TEXT, " +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + "))";
    //----------------------------------------CREATE PROFILES TABLE--------------------------------------------------\\

    //----------------------------------------CREATE PAYEES TABLE--------------------------------------------------\\
    private static final String CREATE_PAYEES_TABLE =
            "CREATE TABLE " + PAYEES_TABLE + " (" +
                    PROFILE_ID + " INTEGER NOT NULL, " +
                    PAYEE_ID + " TEXT NOT NULL, " +
                    PAYEE_NAME + " TEXT, " +
                    "PRIMARY KEY(" + PROFILE_ID + "," + PAYEE_ID + "), " +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + PROFILES_TABLE + "(" + PROFILE_ID + "))";
    //----------------------------------------CREATE PAYEES TABLE--------------------------------------------------\\

    //----------------------------------------CREATE ACCOUNTS TABLE--------------------------------------------------\\
    private static final String CREATE_ACCOUNTS_TABLE =
            "CREATE TABLE " + ACCOUNTS_TABLE + " (" +
                    PROFILE_ID + " INTEGER NOT NULL, " +
                    ACCOUNT_NO + " TEXT NOT NULL, " +
                    ACCOUNT_NAME + " TEXT, " +
                    ACCOUNT_BALANCE + " REAL, " +
                    "PRIMARY KEY(" + PROFILE_ID + "," + ACCOUNT_NO + "), " +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + PROFILES_TABLE + "(" + PROFILE_ID + "))";
    //----------------------------------------CREATE ACCOUNTS TABLE--------------------------------------------------\\

    //----------------------------------------CREATE TRANSACTIONS TABLE--------------------------------------------------\\
    private static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " + TRANSACTIONS_TABLE + " (" +
                    PROFILE_ID + " INTEGER NOT NULL, " +
                    ACCOUNT_NO + " TEXT NOT NULL, " +
                    TRANSACTION_ID + " TEXT NOT NULL, " +
                    TIMESTAMP + " TEXT, " +
                    SENDING_ACCOUNT + " TEXT, " +
                    DESTINATION_PROFILE + " TEXT, " +
                    DESTINATION_ACCOUNT + " TEXT, " +
                    TRANSACTION_PAYEE + " TEXT, " +
                    TRANSACTION_AMOUNT + " REAL, " +
                    TRANS_TYPE + " TEXT, " +
                    "PRIMARY KEY(" + PROFILE_ID + "," + ACCOUNT_NO + "," + TRANSACTION_ID + "), " +
                    "FOREIGN KEY(" + PROFILE_ID + "," + ACCOUNT_NO + ") REFERENCES " +
                    ACCOUNTS_TABLE + "(" + PROFILE_ID + "," + ACCOUNT_NO + ")," +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + PROFILES_TABLE + "(" + PROFILE_ID + "))";
    //----------------------------------------CREATE TRANSACTIONS TABLE--------------------------------------------------\\


    public ApplicationDB(Context context)
    {
        openHelper = new DBHelper(context, DB_NAME, DB_VERSION);
    }

    //TODO: Remove a profile?
    //TODO: Not needed unless I add implementation for modifying profile information such as name, password, username, etc.

    /**
     * Method to overwrite profile's data in database
     * @param profile - profile object
     */
    public void overwriteProfile(Profile profile)
    {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID,profile.getDbId());
        cv.put(FIRST_NAME,profile.getFirstName());
        cv.put(LAST_NAME,profile.getLastName());
//        cv.put(EMAIL,profile.getEmail());
        cv.put(COUNTRY, profile.getCountry());
//        cv.put(USERNAME,profile.getUsername());
//        cv.put(PASSWORD,profile.getPassword());

        database.update(PROFILES_TABLE, cv, PROFILE_ID + "=?", new String[] {String.valueOf(profile.getDbId())});
        database.close();
    }

    /**
     * Method to add a new user to database
     * @param user - User object
     */
    public void saveNewUser(User user)
    {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
//        cv.put(USER_FNAME,user.getFirstName());
//        cv.put(USER_LNAME,user.getLastName());
//        cv.put(USER_COUNTRY,user.getCountry());
        cv.put(USER_USERNAME,user.getUsername());
        cv.put(USER_PASSWORD,user.getPassword());
        if(user.getPicture() != null)
            cv.put(USER_PICTURE,user.getPicture());
        long id = database.insert(USERS_TABLE, null, cv);
        user.setDbId(id);
        database.close();
//        return id;
    }

    /**
     * Method to add a new admin to database
     * @param admin - Admin object
     */
    public void saveNewAdmin(Admin admin)
    {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
//        long id = saveNewUser(admin);
        cv.put(ADMIN_ID,admin.getDbId());
        cv.put(ADMIN_FNAME,admin.getFirstName());
        cv.put(ADMIN_LNAME,admin.getLastName());
        cv.put(ADMIN_COUNTRY,admin.getCountry());
//        cv.put(ADMIN_USERNAME,admin.getUsername());
//        cv.put(ADMIN_PASSWORD,admin.getPassword());
//        long id = database.insert(ADMINS_TABLE, null, cv);
//        admin.setDbId(id);
        database.insert(ADMINS_TABLE, null, cv);
        database.close();
    }

    /**
     * Method to add a new clerk to database
     * @param clerk - Clerk object
     */
    public void saveNewClerk(Clerk clerk)
    {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //long id = saveNewUser(clerk);
        cv.put(CLERK_ID,clerk.getDbId());
        cv.put(CLERK_FNAME,clerk.getFirstName());
        cv.put(CLERK_LNAME,clerk.getLastName());
        cv.put(CLERK_COUNTRY,clerk.getCountry());
//        cv.put(CLERK_USERNAME,clerk.getUsername());
//        cv.put(CLERK_PASSWORD,clerk.getPassword());
//        long id = database.insert(CLERKS_TABLE,null,cv);
//        clerk.setDbId(id);
        database.insert(CLERKS_TABLE,null,cv);
        database.close();
    }

    /**
     * Method to add a new profile to database
     * @param profile - Profile object
     */
    public void saveNewProfile(Profile profile)
    {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //long id = saveNewUser(profile);
        cv.put(PROFILE_ID,profile.getDbId());
        cv.put(FIRST_NAME, profile.getFirstName());
        cv.put(LAST_NAME, profile.getLastName());
//        cv.put(EMAIL,profile.getEmail());
        cv.put(COUNTRY, profile.getCountry());
//        cv.put(USERNAME, profile.getUsername());
//        cv.put(PASSWORD, profile.getPassword());
//        long id = database.insert(PROFILES_TABLE, null, cv);
//        profile.setDbId(id);
        database.insert(PROFILES_TABLE, null, cv);
        database.close();
    }

    //TODO: Overwrite or remove payee?
    /**
     * Method to add a new payee to database
     * @param payee - Payee object
     */
    public void saveNewPayee(Profile profile, Payee payee)
    {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(PROFILE_ID, profile.getDbId());
        cv.put(PAYEE_ID, payee.getPayeeID());
        cv.put(PAYEE_NAME, payee.getPayeeName());

        long id = database.insert(PAYEES_TABLE, null, cv);

        payee.setDbId(id);

        database.close();
    }

    /**
     * Method to add new transaction to data base
     * @param sendingProfile - the sending profile
     * @param accountNo - sending profile account number
     * @param transaction - transaction object to hold the type of transaction
     */
    public void saveNewTransaction(Profile sendingProfile, String accountNo, Transaction transaction)
    {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID, sendingProfile.getDbId());
        cv.put(ACCOUNT_NO, accountNo);
        cv.put(TRANSACTION_ID, transaction.getTransactionID());
        cv.put(TIMESTAMP, transaction.getTimestamp());

        if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
            cv.put(SENDING_ACCOUNT, transaction.getSendingAccount());
            cv.put(DESTINATION_ACCOUNT, transaction.getDestinationAccount());
            cv.putNull(TRANSACTION_PAYEE);
        } else if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.PAYMENT) {
            cv.putNull(SENDING_ACCOUNT);
            cv.putNull(DESTINATION_ACCOUNT);
            cv.put(TRANSACTION_PAYEE, transaction.getPayee());
        } else if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.DEPOSIT) {
            cv.putNull(SENDING_ACCOUNT);
            cv.putNull(DESTINATION_ACCOUNT);
            cv.putNull(TRANSACTION_PAYEE);
        }

        cv.put(TRANSACTION_AMOUNT, transaction.getAmount());
        cv.put(TRANS_TYPE, transaction.getTransactionType().toString());

        long id = database.insert(TRANSACTIONS_TABLE, null, cv);

        transaction.setDbId(id);

        database.close();
    }

    //TODO: Remove an account?

    /**
     * Method to overwrite profile's account
     * can update the profile's account in database after a transfer operation
     * @param profile
     * @param account
     */
    public void overwriteAccount(Profile profile, Account account)
    {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID,profile.getDbId());
        cv.put(ACCOUNT_NO,account.getAccountNo());
        cv.put(ACCOUNT_NAME,account.getAccountName());
        cv.put(ACCOUNT_BALANCE, account.getAccountBalance());

        database.update(ACCOUNTS_TABLE, cv, PROFILE_ID + "=? AND " + ACCOUNT_NO +"=?",
                new String[] {String.valueOf(profile.getDbId()), account.getAccountNo()});
        database.close();
    }

    /**
     * Method to add account to profile
     * @param profile - profile to be added an account
     * @param account - the new account for the profile
     */
    public void saveNewAccount(Profile profile, Account account)
    {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID, profile.getDbId());
        cv.put(ACCOUNT_NO, account.getAccountNo());
        cv.put(ACCOUNT_NAME, account.getAccountName());
        cv.put(ACCOUNT_BALANCE, account.getAccountBalance());

        long id = database.insert(ACCOUNTS_TABLE, null, cv);

        account.setDbID(id);

        database.close();
    }

    /**
     * Method to get all admins users from database
     * joins the ADMINS_TABLE and USERS_TABLE on their ID's
     * @return
     */
    public ArrayList<Admin> getAllAdmins()
    {
        ArrayList<Admin> admins = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + ADMINS_TABLE +  " inner join " + USERS_TABLE + " on " +
                                          ADMIN_ID + " = " + USER_ID,null);
        //Cursor cursor = database.query(ADMINS_TABLE,null,null,null,null,null,null);
        getAdminsFromCursor(admins, cursor);
        cursor.close();
        database.close();
        return admins;
    }

    /**
     * Gets the rows from database with cursor
     * cursor gives us the option to pick a specific column name/index
     * @param admins - arrayList of admins to hold all admins
     * @param cursor - cursor object to get the columns from every row
     */
    private void getAdminsFromCursor(ArrayList<Admin> admins, Cursor cursor)
    {
        // returns true if pointed to a record
        while (cursor.moveToNext())
        {
            int idIndex = cursor.getColumnIndex(ADMIN_ID);
            int firstNameIndex = cursor.getColumnIndex(ADMIN_FNAME);
            int lastNameIndex = cursor.getColumnIndex(ADMIN_LNAME);
            int countryIndex = cursor.getColumnIndex(ADMIN_COUNTRY);
            int userIndex = cursor.getColumnIndex(USER_USERNAME);
            int passwordIndex = cursor.getColumnIndex(USER_PASSWORD);
            long id = cursor.getLong(idIndex);
            String firstName = cursor.getString(firstNameIndex);
            String lastName = cursor.getString(lastNameIndex);
            String country = cursor.getString(countryIndex);
            String username = cursor.getString(userIndex);
            String password = cursor.getString(passwordIndex);

            //ArrayList<Account> accounts = new ArrayList<>();
            //ArrayList<Payee> payees = new ArrayList<>();

            admins.add(new Admin(firstName, lastName,country,username,password,id));
        }
    }

    public ArrayList<Clerk> getAllClerks()
    {
        ArrayList<Clerk> clerks = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + CLERKS_TABLE +  " inner join " + USERS_TABLE + " on " +
                                          CLERK_ID + " = " + USER_ID,null);
//        Cursor cursor = database.query(CLERKS_TABLE,null,null,null,null,
//                null,null);
        getClerksFromCursor(clerks,cursor);
        cursor.close();
        database.close();
        return clerks;
    }

    private void getClerksFromCursor(ArrayList<Clerk> clerks, Cursor cursor)
    {
        while(cursor.moveToNext())
        {
            int idIndex = cursor.getColumnIndex(CLERK_ID);
            int firstNameIndex = cursor.getColumnIndex(CLERK_FNAME);
            int lastNameIndex = cursor.getColumnIndex(CLERK_LNAME);
            int countryIndex = cursor.getColumnIndex(CLERK_COUNTRY);
            int userIndex = cursor.getColumnIndex(USER_USERNAME);
            int passwordIndex = cursor.getColumnIndex(USER_PASSWORD);
            long id = cursor.getLong(idIndex);
            String firstName = cursor.getString(firstNameIndex);
            String lastName = cursor.getString(lastNameIndex);
            String country = cursor.getString(countryIndex);
            String username = cursor.getString(userIndex);
            String password = cursor.getString(passwordIndex);
            clerks.add(new Clerk(firstName,lastName,country,username,password));
        }
    }



    public ArrayList<Profile> getAllProfiles()
    {

        ArrayList<Profile> profiles = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + PROFILES_TABLE +  " inner join " + USERS_TABLE + " on " +
                                          PROFILE_ID + " = " + USER_ID,null);
//        Cursor cursor = database.query(PROFILES_TABLE, null,null,null,null,
//                null, null);
        getProfilesFromCursor(profiles, cursor);

        cursor.close();
        database.close();

        return profiles;
    }

    /**
     * Method to get all profiles available to transfer
     * list of profiles besides the sending profile
     * @param profileID - sending profile id to be excluded by query
     * @return - list of profiles available for transfer
     */
    public ArrayList<Profile> getAllProfilesForTransfer(long profileID)
    {
        ArrayList<Profile> profiles = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + PROFILES_TABLE +  " inner join " + USERS_TABLE + " on " +
                                          PROFILE_ID + " = " + USER_ID + " where " + PROFILE_ID + " != ?",new String[]{String.valueOf(profileID)});
//        Cursor cursor = database.rawQuery("SELECT * FROM " + PROFILES_TABLE + " WHERE " +
//                                          PROFILE_ID + " != ?",new String[]{String.valueOf(profileID)});
        while(cursor.moveToNext())
        {
            int idIndex = cursor.getColumnIndex(PROFILE_ID);
            int firstNameIndex = cursor.getColumnIndex(FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(LAST_NAME);
//            int emailIndex = cursor.getColumnIndex(EMAIL);
            int countryIndex = cursor.getColumnIndex(COUNTRY);
            int userIndex = cursor.getColumnIndex(USER_USERNAME);
            int passwordIndex = cursor.getColumnIndex(USER_PASSWORD);
            long id = cursor.getLong(idIndex);
            String firstName = cursor.getString(firstNameIndex);
            String lastName = cursor.getString(lastNameIndex);
//            String email = cursor.getString(emailIndex);
            String country = cursor.getString(countryIndex);
            String username = cursor.getString(userIndex);
            String password = cursor.getString(passwordIndex);
            profiles.add(new Profile(firstName, lastName,country, username, password, id));
        }
        cursor.close();
        database.close();
        return profiles;
    }

    /**
     * Method to get all available accounts for transfer
     * list of accounts besides the sending profile's accounts
     * @param profileID - sending profile's id to be excluded by query
     * @return - list of account available for transfer
     */
    public ArrayList<Account> getAllAccountsForTransfer(long profileID)
    {
        ArrayList<Account> accounts = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + ACCOUNTS_TABLE + " WHERE " +
                                          PROFILE_ID + " != ?",new String[]{String.valueOf(profileID)});
        getAccountsForTransferFromCursor(accounts,cursor);

        //Cursor cursor = database.query(ACCOUNTS_TABLE, null, null, null, null,
        //        null ,null);
        //getAccountsForTransferFromCursor(profileID,accounts,cursor);

        cursor.close();
        database.close();

        return accounts;
    }

    /**
     * Get the specific profile from database by profileID
     * @param profileID - id of profile to get
     * @return - profile that matched the profileID
     */
    private Profile getProfileByID(long profileID)
    {
        Profile profile = null;
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + PROFILES_TABLE + " where " +
                                          PROFILE_ID + " = " + profileID,null);
        cursor.moveToFirst();
        profile.setDbId(Long.parseLong(cursor.getString(PROFILE_ID_COLUMN)));
        profile.setFirstName(cursor.getString(FIRST_NAME_COLUMN));
//        profile.setEmail(cursor.getString(EMAIL_COLUMN));
        profile.setLastName(cursor.getString(LAST_NAME_COLUMN));
        profile.setCountry(cursor.getString(COUNTRY_COLUMN));
//        profile.setUsername(cursor.getString(USERNAME_COLUMN));
//        profile.setPassword(cursor.getString(PASSWORD_COLUMN));
        cursor.close();
        return profile;
    }

    private void getProfilesFromCursor(ArrayList<Profile> profiles, Cursor cursor)
    {
        // returns true if pointed to a record
        while (cursor.moveToNext())
        {

            int idIndex = cursor.getColumnIndex(PROFILE_ID);
            int firstNameIndex = cursor.getColumnIndex(FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(LAST_NAME);
//            int emailIndex = cursor.getColumnIndex(EMAIL);
            int countryIndex = cursor.getColumnIndex(COUNTRY);
            int userIndex = cursor.getColumnIndex(USER_USERNAME);
            int passwordIndex = cursor.getColumnIndex(USER_PASSWORD);
            long id = cursor.getLong(idIndex);
            String firstName = cursor.getString(firstNameIndex);
            String lastName = cursor.getString(lastNameIndex);
//            String email = cursor.getString(emailIndex);
            String country = cursor.getString(countryIndex);
            String username = cursor.getString(userIndex);
            String password = cursor.getString(passwordIndex);

            //ArrayList<Account> accounts = new ArrayList<>();
            //ArrayList<Payee> payees = new ArrayList<>();

            profiles.add(new Profile(firstName, lastName, country, username, password, id));
        }
    }

    /**
     * Method to get payees list of this profile
     * @param profileID - profile's id to get payees list
     * @return - profile's payees list
     */
    public ArrayList<Payee> getPayeesFromCurrentProfile(long profileID)
    {
        ArrayList<Payee> payees = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(PAYEES_TABLE, null, null, null, null,
                null ,null);
        getPayeesFromCursor(profileID, payees, cursor);
        cursor.close();
        database.close();
        return payees;
    }

    private void getPayeesFromCursor(long profileID, ArrayList<Payee> payees, Cursor cursor)
    {
        while (cursor.moveToNext())
        {
            if (profileID == cursor.getLong(PROFILE_ID_COLUMN))
            {
                long id = cursor.getLong(PROFILE_ID_COLUMN);
                String payeeID = cursor.getString(PAYEE_ID_COLUMN);
                String payeeName = cursor.getString(PAYEE_NAME_COLUMN);
                payees.add(new Payee(payeeID, payeeName, id));
            }
        }
    }

    /**
     * Method to get account's transactions history
     * @param profileID - profile who owns the account
     * @param accountNo - account number
     * @return -  account's transactions
     */
    public ArrayList<Transaction> getTransactionsFromCurrentAccount(long profileID, String accountNo) {

        ArrayList<Transaction> transactions = new ArrayList<>();
        database = openHelper.getReadableDatabase();

        Cursor cursor = database.query(TRANSACTIONS_TABLE, null, null, null, null,
                null ,null);

        getTransactionsFromCursor(profileID, accountNo, transactions, cursor);

        cursor.close();
        database.close();

        return transactions;
    }

    private void getTransactionsFromCursor(long profileID, String accountNo, ArrayList<Transaction> transactions, Cursor cursor) {

        while (cursor.moveToNext())
        {

            if (profileID == cursor.getLong(PROFILE_ID_COLUMN))
            {
                long id = cursor.getLong(PROFILE_ID_COLUMN);
                if (accountNo.equals(cursor.getString(ACCOUNT_NO_COLUMN)))
                {
                    String transactionID = cursor.getString(TRANSACTION_ID_COLUMN);
                    String timestamp = cursor.getString(TIMESTAMP_COLUMN);
                    String sendingAccount = cursor.getString(SENDING_ACCOUNT_COLUMN);
                    String destinationAccount = cursor.getString(DESTINATION_ACCOUNT_COLUMN);
                    String payee = cursor.getString(TRANSACTION_PAYEE_COLUMN);
                    double amount = cursor.getDouble(TRANSACTION_AMOUNT_COLUMN);
                    Transaction.TRANSACTION_TYPE transactionType = Transaction.TRANSACTION_TYPE.valueOf(cursor.getString(TRANSACTION_TYPE_COLUMN));

                    if (transactionType == Transaction.TRANSACTION_TYPE.PAYMENT) {
                        transactions.add(new Transaction(transactionID, timestamp, payee, amount, id));
                    } else if (transactionType == Transaction.TRANSACTION_TYPE.TRANSFER) {
                        transactions.add(new Transaction(transactionID, timestamp, sendingAccount, destinationAccount, amount, id));
                    } else if (transactionType == Transaction.TRANSACTION_TYPE.DEPOSIT) {
                        transactions.add(new Transaction(transactionID, timestamp, amount, id));
                    }
                }

            }
        }
    }
    public ArrayList<Account> getAccountsFromCurrentProfile(long profileID)
    {

        ArrayList<Account> accounts = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(ACCOUNTS_TABLE, null, null, null, null,
                null,null);
        getAccountsFromCursor(profileID, accounts, cursor);

        cursor.close();
        database.close();

        return accounts;
    }

    private void getAccountsForTransferFromCursor(ArrayList<Account> accounts, Cursor cursor)
    {
        while (cursor.moveToNext())
        {
            long id = cursor.getLong(PROFILE_ID_COLUMN);
            String accountNo = cursor.getString(ACCOUNT_NO_COLUMN);
            String accountName = cursor.getString(ACCOUNT_NAME_COLUMN);
            double accountBalance = cursor.getDouble(ACCOUNT_BALANCE_COLUMN);
            accounts.add(new Account(accountName, accountNo, accountBalance, id));
        }
    }

    private void getAccountsFromCursor(long profileID, ArrayList<Account> accounts, Cursor cursor)
    {

        while (cursor.moveToNext())
        {

            if (profileID == cursor.getLong(PROFILE_ID_COLUMN))
            {
                long id = cursor.getLong(PROFILE_ID_COLUMN);
                String accountNo = cursor.getString(ACCOUNT_NO_COLUMN);
                String accountName = cursor.getString(ACCOUNT_NAME_COLUMN);
                double accountBalance = cursor.getDouble(ACCOUNT_BALANCE_COLUMN);

                accounts.add(new Account(accountName, accountNo, accountBalance, id));
            }
        }
    }

//    public void transferMoney(Profile userProfile, Account sendingAccount, Profile receivingProfile, Account receivingAccount,double transferAmount)
//    {
//        sendingAccount.setAccountBalance(sendingAccount.getAccountBalance() - transferAmount);
//        receivingAccount.setAccountBalance(receivingAccount.getAccountBalance() + transferAmount);
//        overwriteAccount(userProfile,sendingAccount);
//        overwriteAccount(receivingProfile,receivingAccount);
//    }

    //----------------------------------------ADMINS--------------------------------------------------\\
    private static final String DANIEL_FIRST_NAME = "Daniel";
    private static final String DANIEL_LAST_NAME = "Arbiv";
    private static final String DANIEL_COUNTRY = "ISR";

    private static final String MIKI_FIRST_NAME = "Miki";
    private static final String MIKI_LAST_NAME = "Vitkovsky";
    private static final String MIKI_COUNTRY = "ISR";

    private static final String YOSSI_FIRST_NAME = "Yossi";
    private static final String YOSSI_LAST_NAME = "Abu";
    private static final String YOSSI_COUNTRY = "ISR";
    //----------------------------------------ADMINS--------------------------------------------------\\

    //----------------------------------------CREATE ADMINS--------------------------------------------------\\
    private static final String ADMINS_ROW1 =
            "INSERT INTO " + ADMINS_TABLE + " (" + ADMIN_FNAME + "," + ADMIN_LNAME + "," + ADMIN_COUNTRY + ")" +
            " VALUES (" + DANIEL_FIRST_NAME + "," + DANIEL_LAST_NAME + "," + DANIEL_COUNTRY + ");";
    private static final String ADMINS_ROW2 =
            "INSERT INTO " + ADMINS_TABLE + " (" + ADMIN_FNAME + "," + ADMIN_LNAME + "," + ADMIN_COUNTRY + ")" +
            " VALUES (" + MIKI_FIRST_NAME + "," + MIKI_LAST_NAME + "," + MIKI_COUNTRY + ");";
    private static final String ADMINS_ROW3 =
            "INSERT INTO " + ADMINS_TABLE + " (" + ADMIN_FNAME + "," + ADMIN_LNAME + "," + ADMIN_COUNTRY + ")" +
            " VALUES (" + YOSSI_FIRST_NAME + "," + YOSSI_LAST_NAME + "," + YOSSI_COUNTRY + ");";
    public static void createAdmins()
    {
        staticDatabase = staticOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ADMIN_FNAME,DANIEL_FIRST_NAME);
        cv.put(ADMIN_LNAME,DANIEL_LAST_NAME);
        cv.put(ADMIN_COUNTRY,DANIEL_COUNTRY);

//        cv.put(ADMIN_FNAME,MIKI_FIRST_NAME);
//        cv.put(ADMIN_LNAME,MIKI_LAST_NAME);
//        cv.put(ADMIN_COUNTRY,MIKI_COUNTRY);
//
//        cv.put(ADMIN_FNAME,YOSSI_FIRST_NAME);
//        cv.put(ADMIN_LNAME,YOSSI_LAST_NAME);
//        cv.put(ADMIN_COUNTRY,YOSSI_COUNTRY);
    }

    public static void createClerks()
    {
        staticDatabase = staticOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CLERK_FNAME,DAN_FIRST_NAME);
        cv.put(CLERK_LNAME,DAN_LAST_NAME);
        cv.put(CLERK_COUNTRY,DAN_COUNTRY);

        cv.put(CLERK_FNAME,ADIR_FIRST_NAME);
        cv.put(CLERK_LNAME,ADIR_LAST_NAME);
        cv.put(CLERK_COUNTRY,ADIR_COUNTRY);
    }
    //----------------------------------------CREATE ADMINS--------------------------------------------------\\

    //----------------------------------------CLERKS--------------------------------------------------\\
    private static final String DAN_FIRST_NAME = "Dan";
    private static final String DAN_LAST_NAME = "Mon";
    private static final String DAN_COUNTRY = "ISR";

    private static final String ADIR_FIRST_NAME = "Adir";
    private static final String ADIR_LAST_NAME = "Shaish";
    private static final String ADIR_COUNTRY = "ISR";

    //----------------------------------------CLERKS--------------------------------------------------\\

    //----------------------------------------CREATE CLERKS--------------------------------------------------\\
    private static final String CLERKS_ROWS =
            "INSERT INTO " + CLERKS_TABLE + "(" + CLERK_FNAME + "," + CLERK_LNAME + "," + CLERK_COUNTRY + ")" +
            " VALUES" + "(" + DAN_FIRST_NAME + "," + DAN_LAST_NAME + "," + DAN_COUNTRY + ")," +
            "(" + ADIR_FIRST_NAME + "," + ADIR_LAST_NAME + "," + ADIR_COUNTRY + ");";
    //----------------------------------------CREATE CLERKS--------------------------------------------------\\
    /**
     * Class to initialize the database and drop tables on update
     */
    private static class DBHelper extends SQLiteOpenHelper
    {

        private DBHelper(Context context, String name, int version)
        {
            super(context, name, null, version);
        }

        // if the db doesn't exist , the runtime calls this fn . we don't have to check if it exists
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // create tables
            db.execSQL(CREATE_USERS_TABLE);
            db.execSQL(CREATE_ADMINS_TABLE);
//            db.execSQL(ADMINS_ROW1);
//            db.execSQL(ADMINS_ROW2);
//            db.execSQL(ADMINS_ROW3);
            db.execSQL(CREATE_CLERKS_TABLE);
//            db.execSQL(CLERKS_ROWS);
            db.execSQL(CREATE_PROFILES_TABLE);
            db.execSQL(CREATE_PAYEES_TABLE);
            db.execSQL(CREATE_ACCOUNTS_TABLE);
            db.execSQL(CREATE_TRANSACTIONS_TABLE);
//            ApplicationDB.createAdmins();
//            ApplicationDB.createClerks();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            // drop the table
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ADMINS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CLERKS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PROFILES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PAYEES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE);
            onCreate(db);
        }
    }
}
