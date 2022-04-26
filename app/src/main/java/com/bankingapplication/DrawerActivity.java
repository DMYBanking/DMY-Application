package com.bankingapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bankingapplication.Model.Account;
import com.bankingapplication.Model.Admin;
import com.bankingapplication.Model.Clerk;
import com.bankingapplication.Model.Profile;
import com.bankingapplication.Model.db.ApplicationDB;
import com.example.mikebanks.bankscorpfinancial.R;
import com.google.gson.Gson;

import java.util.Locale;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public enum manualNavID {
        DASHBOARD_ID,
        ACCOUNTS_ID,
        LOANS_ID
    }

    private Bundle bundle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private SharedPreferences userPreferences;
    private Gson gson;
    private String json;
    private Profile userProfile;
    private Admin userAdmin;
    private Clerk userClerk;
    private Dialog loanDialog;
    private Dialog depositDialog;
    private Spinner topSpinner;
    private Spinner bottomSpinner;
    private ArrayAdapter<Account> accountAdapter;
    private ArrayAdapter<Clerk> clerkAdapter;
    private EditText edtDepositAmount;
    private EditText edtLoanAmount;
    private Button btnCancel;
    private Button btnSuccess;
    // defines user role - 1 (admin), 2 (clerk), 3 (user)
    private int userRole;

    private View.OnClickListener depositClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if (view.getId() == btnCancel.getId())
            {
                depositDialog.dismiss();
                manualNavigation(manualNavID.ACCOUNTS_ID, null);
                Toast.makeText(DrawerActivity.this, "Deposit Cancelled", Toast.LENGTH_SHORT).show();
            }
            else if (view.getId() == btnSuccess.getId())
            {
                makeDeposit();
            }
        }
    };

    private View.OnClickListener loanClickListener = new View.OnClickListener()
    {
        public void onClick(View view)
        {
            if(view.getId() == btnCancel.getId())
            {
                loanDialog.dismiss();
                manualNavigation(manualNavID.ACCOUNTS_ID,null);
                Toast.makeText(DrawerActivity.this, "Loan Cancelled", Toast.LENGTH_SHORT).show();
            }
            else if (view.getId() == btnSuccess.getId())
            {
                makeLoan();
            }
        }
    };

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)
        {
            if (i == DialogInterface.BUTTON_POSITIVE)
            {
                Bundle bundle = new Bundle();
                bundle.putBoolean("DisplayAccountDialog", true);
                manualNavigation(manualNavID.ACCOUNTS_ID, bundle);
            }
        }
    };

    public void manualNavigation(manualNavID id, Bundle bundle)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (id == manualNavID.DASHBOARD_ID)
        {
            ft.replace(R.id.flContent, new DashboardFragment()).commit();
            navView.setCheckedItem(R.id.nav_dashboard);
            setTitle("Dashboard");
        }
        else if (id == manualNavID.ACCOUNTS_ID)
        {
            AccountOverviewFragment accountOverviewFragment = new AccountOverviewFragment();
            if (bundle != null)
            {
                accountOverviewFragment.setArguments(bundle);
            }
            ft.replace(R.id.flContent, accountOverviewFragment).commit();
            navView.setCheckedItem(R.id.nav_accounts);
            setTitle("Accounts");
        }
        else if(id == manualNavID.LOANS_ID)
        {
            LoanFragment loanFragment = new LoanFragment();
            if(bundle != null)
            {
                loanFragment.setArguments(bundle);
            }
            ft.replace(R.id.flContent,loanFragment).commit();
            navView.setCheckedItem(R.id.nav_loan);
            navView.getMenu().findItem(R.id.nav_loan).setTitle("Pending Loans");
            setTitle("Pending Loans");
        }

        drawerLayout.closeDrawers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        userRole = LoginFragment.getUserRole();
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        //userPreferences = this.getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        // if admin user
        if(userRole == 1)
        {
            userPreferences = this.getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
            json = userPreferences.getString("adminUser","");
            userAdmin = gson.fromJson(json,Admin.class);
            navView.getMenu().findItem(R.id.nav_loan).setVisible(false);
            navView.getMenu().findItem(R.id.nav_transfer).setVisible(false);
            navView.getMenu().findItem(R.id.nav_payment).setVisible(false);
            navView.getMenu().findItem(R.id.nav_accounts).setVisible(false);
            navView.getMenu().findItem(R.id.nav_profile).setVisible(false);
        }
        // if clerk user
        if(userRole == 2)
        {
            userPreferences = this.getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
            json = userPreferences.getString("clerkUser","");
            userClerk = gson.fromJson(json,Clerk.class);
            navView.getMenu().findItem(R.id.nav_payment).setVisible(false);
            navView.getMenu().findItem(R.id.nav_transfer).setVisible(false);
            navView.getMenu().findItem(R.id.nav_deposit).setVisible(false);
            navView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navView.getMenu().findItem(R.id.nav_loan).setTitle("Pending Loans");
        }
        // if regular user
        else if(userRole == 3)
        {
            userPreferences = this.getSharedPreferences("LastProfileUsed",MODE_PRIVATE);
            json = userPreferences.getString("profileUser","");
            userProfile = gson.fromJson(json,Profile.class);
            navView.getMenu().findItem(R.id.nav_users).setVisible(false);
        }
        //json = userPreferences.getString("LastProfileUsed", "");
        SharedPreferences.Editor prefsEditor = userPreferences.edit();
        loadFromDB(userRole);
        // if admin user
        if(userRole == 1)
        {
            userAdmin = gson.fromJson(json, Admin.class);
            prefsEditor.putString("adminUser", json).apply();
        }
        // if clerk user
        if(userRole == 2)
        {
            userClerk = gson.fromJson(json, Clerk.class);
            prefsEditor.putString("clerkUser", json).apply();
        }
        // if regular user
        else if(userRole == 3)
        {
            userProfile = gson.fromJson(json, Profile.class);
            prefsEditor.putString("profileUser",json).apply();
        }
        prefsEditor.putString("LastProfileUsed", json).apply();
        setupDrawerListener();
        setupHeader();
        //TODO: Try calling the event listener manually for navigation, get rid of the manualNav method
        manualNavigation(manualNavID.DASHBOARD_ID, null);
    }

    //TODO: Find different way to close keyboard when opening drawer or clean this up
    private void setupDrawerListener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset)
            {

            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView)
            {

            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView)
            {

            }
            @Override
            public void onDrawerStateChanged(int newState)
            {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    private void setupHeader()
    {
        View headerView = navView.getHeaderView(0);
        String name = "";
        //ImageView imgProfilePic = findViewById(R.id.img_profile); //TODO: set the profile image
        TextView txtName = headerView.findViewById(R.id.txt_name);
        TextView txtUsername = headerView.findViewById(R.id.txt_username);
        if(userRole == 1)
        {
            name = userAdmin.getFirstName() + " " + userAdmin.getLastName();
            txtUsername.setText(userAdmin.getUsername());
        }
        else if(userRole == 2)
        {
            name = userClerk.getFirstName() + " " + userClerk.getLastName();
            txtUsername.setText(userClerk.getUsername());
        }
        else if(userRole == 3)
        {
            name = userProfile.getFirstName() + " " + userProfile.getLastName();
            txtUsername.setText(userProfile.getUsername());
        }
        txtName.setText(name);
    }

    private void loadFromDB(int userRole)
    {
        ApplicationDB applicationDb = new ApplicationDB(getApplicationContext());
        // if admin user
        if(userRole == 1)
        {
            // setting Admin's users to have all of the users profiles
            userAdmin.setUsers(applicationDb.getAllProfiles());
            json = gson.toJson(userAdmin);
        }
        // if clerk user
        if(userRole == 2)
        {
            userClerk.setUsers(applicationDb.getAllProfiles());
            json = gson.toJson(userClerk);
        }
        // if regular user
        else if(userRole == 3)
        {
            userProfile.setPayeesFromDB(applicationDb.getPayeesFromCurrentProfile(userProfile.getDbId()));
            userProfile.setAccountsFromDB(applicationDb.getAccountsFromCurrentProfile(userProfile.getDbId()));
            for (int iAccount = 0; iAccount < userProfile.getAccounts().size(); iAccount++)
            {
                userProfile.getAccounts().get(iAccount).setTransactions(applicationDb.getTransactionsFromCurrentAccount(userProfile.getDbId(), userProfile.getAccounts().get(iAccount).getAccountNo()));
            }
            json = gson.toJson(userProfile);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public void showDrawerButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.syncState();
    }

    public void showUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void displayAccountAlertADialog(String option) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(String.format("%s Error", option))
                .setMessage(String.format("You do not have enough accounts to make a %s. Add another account if you want to make a %s.", option, option.toLowerCase()))
                .setNegativeButton("Cancel", dialogClickListener)
                .setPositiveButton("Add Account", dialogClickListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayDepositDialog()
    {
        depositDialog = new Dialog(this);
        depositDialog.setContentView(R.layout.deposit_dialog);

        depositDialog.setCanceledOnTouchOutside(true);
        depositDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                manualNavigation(manualNavID.ACCOUNTS_ID, null);
                Toast.makeText(DrawerActivity.this, "Deposit Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        topSpinner = depositDialog.findViewById(R.id.dep_spn_accounts);
        accountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userProfile.getAccounts());
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topSpinner.setAdapter(accountAdapter);
        topSpinner.setSelection(0);

        edtDepositAmount = depositDialog.findViewById(R.id.edt_deposit_amount);

        btnCancel = depositDialog.findViewById(R.id.btn_cancel_deposit);
        btnSuccess = depositDialog.findViewById(R.id.btn_deposit);

        btnCancel.setOnClickListener(depositClickListener);
        btnSuccess.setOnClickListener(depositClickListener);

        depositDialog.show();

    }

    private void displayLoanDialog()
    {
        loanDialog = new Dialog(this);
        loanDialog.setContentView(R.layout.loan_dialog);
        loanDialog.setCanceledOnTouchOutside(true);
        loanDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                manualNavigation(manualNavID.ACCOUNTS_ID, null);
                Toast.makeText(DrawerActivity.this, "Deposit Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        ApplicationDB applicationDB = new ApplicationDB(getApplicationContext());
        topSpinner = loanDialog.findViewById(R.id.spn_clerk_list_Loan_dialog);
        clerkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, applicationDB.getAllClerks());
        clerkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topSpinner.setAdapter(clerkAdapter);
        topSpinner.setSelection(0);

        bottomSpinner = loanDialog.findViewById(R.id.spn_account_dialog);
        accountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,userProfile.getAccounts());
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bottomSpinner.setAdapter(accountAdapter);
        bottomSpinner.setSelection(0);
        edtLoanAmount = loanDialog.findViewById(R.id.edt_loan_amount);

        btnCancel = loanDialog.findViewById(R.id.btn_cancel_loan_dialog);
        btnSuccess = loanDialog.findViewById(R.id.btn_success_loan_dialog);

        btnCancel.setOnClickListener(loanClickListener);
        btnSuccess.setOnClickListener(loanClickListener);
        loanDialog.show();
    }

    /**
     * method used to make a deposit
     */
    private void makeDeposit() {

        int selectedAccountIndex = topSpinner.getSelectedItemPosition();

        double depositAmount = 0;
        boolean isNum = false;

        try {
            depositAmount = Double.parseDouble(edtDepositAmount.getText().toString());
            isNum = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (depositAmount < 0.01 && !isNum) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        } else {

            Account account = userProfile.getAccounts().get(selectedAccountIndex);
            account.addDepositTransaction(depositAmount);

            SharedPreferences.Editor prefsEditor = userPreferences.edit();
            gson = new Gson();
            json = gson.toJson(userProfile);
            prefsEditor.putString("profileUser", json).apply();

            ApplicationDB applicationDb = new ApplicationDB(getApplicationContext());
            applicationDb.overwriteAccount(userProfile, account);
            applicationDb.saveNewTransaction(userProfile, account.getAccountNo(),
                    account.getTransactions().get(account.getTransactions().size()-1));

            Toast.makeText(this, "Deposit of $" + String.format(Locale.getDefault(), "%.2f",depositAmount) + " " + "made successfully", Toast.LENGTH_SHORT).show();

            accountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userProfile.getAccounts());
            accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            topSpinner.setAdapter(accountAdapter);

            //TODO: Add checkbox if the user wants to make more than one deposit

            depositDialog.dismiss();
            drawerLayout.closeDrawers();
            //manualNavigation(manualNavID.ACCOUNTS_ID, null);
        }
    }

    private void makeLoan()
    {
        double loanAmount = 0;
        boolean isNum = false;
        int clerkSelectedIndex = topSpinner.getSelectedItemPosition();
        int accountSelectedIndex = bottomSpinner.getSelectedItemPosition();

        try
        {
            loanAmount = Double.parseDouble(edtLoanAmount.getText().toString());
            isNum = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (loanAmount < 0.01 && !isNum)
        {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SharedPreferences.Editor prefsEditor = userPreferences.edit();
            gson = new Gson();
            json = gson.toJson(userProfile);
            prefsEditor.putString("profileUser", json).apply();
            ApplicationDB applicationDB = new ApplicationDB(getApplicationContext());
            Clerk clerk = applicationDB.getAllClerks().get(clerkSelectedIndex);
            Account account = userProfile.getAccounts().get(accountSelectedIndex);
            clerk.addLoanTransaction(account,loanAmount);
            //ApplicationDB applicationDb = new ApplicationDB(getApplicationContext());
            //Account account = userProfile.getAccounts().get(topSpinner.getSelectedItemPosition());
            //Clerk clerk = applicationDb.getAllClerks().get(bottomSpinner.getSelectedItemPosition());
            //account.addLoanTransaction(loanAmount);
            Toast.makeText(this, "Loan of $" + String.format(Locale.getDefault(), "%.2f",loanAmount) + " " + "is pending", Toast.LENGTH_SHORT).show();
            loanDialog.dismiss();
            drawerLayout.closeDrawers();
            manualNavigation(manualNavID.ACCOUNTS_ID, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void displayHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Help")
                .setMessage("Soon, this dialog will give the user help, depending on where they are in the app");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            displayHelpDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        userPreferences = this.getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        json = userPreferences.getString("profileUser", "");
        userProfile = gson.fromJson(json, Profile.class);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        Class fragmentClass = null;
        String title = item.getTitle().toString();

        switch(item.getItemId())
        {
            case R.id.nav_dashboard:
                fragmentClass = DashboardFragment.class;
                break;
            case R.id.nav_accounts:
                fragmentClass = AccountOverviewFragment.class;
                break;
            case R.id.nav_deposit:
                if (userProfile.getAccounts().size() > 0)
                {
                    displayDepositDialog();
                }
                else
                {
                    displayAccountAlertADialog("Deposit");
                }
                break;
            case R.id.nav_transfer:
                if (userProfile.getAccounts().size() < 2)
                {
                    displayAccountAlertADialog("Transfer");
                }
                else
                {
                    title = "Transfer";
                    fragmentClass = TransferFragment.class;
                }
                break;
            case R.id.nav_payment:
                if (userProfile.getAccounts().size() < 1)
                {
                    displayAccountAlertADialog("Payment");
                }
                else
                {
                    title = "Payment";
                    fragmentClass = PaymentFragment.class;
                }
                break;
            case R.id.nav_loan:
                if(userRole == 3)
                {
                    if (userProfile.getAccounts().size() < 1)
                    {
                        displayAccountAlertADialog("Loan");
                    }
                    else
                    {
                        displayLoanDialog();
                    }
                }
                else if(userRole == 2)
                {
                    title = "Pending Loans";
                    fragmentClass = LoanFragment.class;
                }
            case R.id.nav_profile:
                fragmentClass = UserProfileFragment.class;
            case R.id.nav_settings:
                //TODO: Make Settings fragment
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                fragmentClass = DashboardFragment.class;
        }

        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            item.setChecked(true);
            setTitle(title);
            drawerLayout.closeDrawers();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
