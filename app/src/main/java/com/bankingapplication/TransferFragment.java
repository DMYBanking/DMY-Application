package com.bankingapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bankingapplication.Model.Account;
import com.bankingapplication.Model.Admin;
import com.bankingapplication.Model.Clerk;
import com.bankingapplication.Model.Profile;
import com.bankingapplication.Model.db.ApplicationDB;
import com.example.mikebanks.bankscorpfinancial.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class TransferFragment extends Fragment
{
    private Spinner spnSendingAccount;
    private EditText edtTransferAmount;
    private Spinner spnReceivingAccount;
    private Button btnConfirmTransfer;
    private Spinner spnReceivingProfile;
    private long receivingProfileID;

    ArrayList<Profile> profiles;
    ArrayAdapter<Profile> profileAdapter;
    ArrayList<Account> profileAccounts;
    ArrayAdapter<Account> profileAccountsAdapter;
    ArrayList<Account> accountsToTransfer;
    ArrayAdapter<Account> accountsToTransferAdapter;

    SharedPreferences userPreferences;
    Gson gson;
    String json;
    Profile userProfile;
    Admin userAdmin;
    Clerk userClerk;

    public TransferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView =  inflater.inflate(R.layout.fragment_transfer, container, false);

        spnReceivingProfile = rootView.findViewById(R.id.spn_select_profile_acc);
        spnSendingAccount = rootView.findViewById(R.id.spn_select_sending_acc);
        edtTransferAmount = rootView.findViewById(R.id.edt_transfer_amount);
        spnReceivingAccount = rootView.findViewById(R.id.spn_select_receiving_acc);
        btnConfirmTransfer = rootView.findViewById(R.id.btn_confirm_transfer);

        setValues();

        return rootView;
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setValues()
    {
        userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        if(LoginFragment.getUserRole() == 1)
        {
            json = userPreferences.getString("adminUser", "");
            userAdmin = gson.fromJson(json,Admin.class);
        }
        else if(LoginFragment.getUserRole() == 2)
        {
            json = userPreferences.getString("clerkUser","");
            userClerk = gson.fromJson(json,Clerk.class);
        }
        else if(LoginFragment.getUserRole() == 3)
        {
            json = userPreferences.getString("profileUser","");
            userProfile = gson.fromJson(json, Profile.class);
        }

        btnConfirmTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmTransfer();
            }
        });

        setAdapters();
    }

    /**
     * method that sets up the adapters
     */
    private void setAdapters()
    {
        ApplicationDB applicationDB = new ApplicationDB(getActivity().getApplicationContext());
        profiles = applicationDB.getAllProfilesForTransfer(userProfile.getDbId());
        accountsToTransfer = applicationDB.getAllAccountsForTransfer(userProfile.getDbId());
        profileAccounts = applicationDB.getAccountsFromCurrentProfile(userProfile.getDbId());
        //receivingProfileID = userProfile.getDbId();
        profileAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, profiles);
        profileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        profileAccountsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,profileAccounts);
        profileAccountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountsToTransferAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,accountsToTransfer);
        accountsToTransferAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnReceivingProfile.setAdapter(profileAdapter);
        spnSendingAccount.setAdapter(profileAccountsAdapter);
        spnReceivingAccount.setAdapter(accountsToTransferAdapter);
        //spnReceivingAccount.setSelection(1);
    }

    /**
     * method that confirms the transfer
     */
    private void confirmTransfer()
    {
        int receivingProfIndex = spnReceivingProfile.getSelectedItemPosition();
        int receivingAccIndex = spnReceivingAccount.getSelectedItemPosition();
        boolean isNum = false;
        double transferAmount = 0;

        try
        {
            transferAmount = Double.parseDouble(edtTransferAmount.getText().toString());
            isNum = true;
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Please enter an amount to transfer", Toast.LENGTH_SHORT).show();
        }
        if (isNum)
        {
            //if (spnSendingAccount.getSelectedItemPosition() == receivingAccIndex)
            //{
            //    Toast.makeText(getActivity(), "You cannot make a transfer to the same account", Toast.LENGTH_SHORT).show();
            //}
            if(transferAmount < 0.01)
            {
                Toast.makeText(getActivity(), "The minimum amount for a transfer is $0.01", Toast.LENGTH_SHORT).show();

            }
            else if (transferAmount > userProfile.getAccounts().get(spnSendingAccount.getSelectedItemPosition()).getAccountBalance())
            {

                Account acc = (Account) spnSendingAccount.getSelectedItem();
                Toast.makeText(getActivity(), "The account," + " " + acc.toString() + " " + "does not have sufficient funds to make this transfer", Toast.LENGTH_LONG).show();
            }
            else
            {
                int sendingAccIndex = spnSendingAccount.getSelectedItemPosition();
                
                Account sendingAccount = (Account) spnSendingAccount.getItemAtPosition(sendingAccIndex);
                Account receivingAccount = (Account) spnReceivingAccount.getItemAtPosition(receivingAccIndex);
                Profile receivingProfile = (Profile) spnReceivingProfile.getItemAtPosition(receivingProfIndex);

                userProfile.addTransferTransaction(sendingAccount,receivingAccount, transferAmount);
                spnSendingAccount.setAdapter(profileAccountsAdapter);
                spnReceivingAccount.setAdapter(accountsToTransferAdapter);

                spnSendingAccount.setSelection(sendingAccIndex);
                spnReceivingAccount.setSelection(receivingAccIndex);

                ApplicationDB applicationDb = new ApplicationDB(getActivity().getApplicationContext());

//                applicationDb.transferMoney(userProfile,sendingAccount,receivingProfile,receivingAccount,transferAmount);

                applicationDb.overwriteAccount(userProfile, sendingAccount);
                applicationDb.overwriteAccount(receivingProfile, receivingAccount);

                applicationDb.saveNewTransaction(userProfile,sendingAccount.getAccountNo(),
                        sendingAccount.getTransactions().get(sendingAccount.getTransactions().size()-1));
                applicationDb.saveNewTransaction(receivingProfile, receivingAccount.getAccountNo(),
                        receivingAccount.getTransactions().get(receivingAccount.getTransactions().size()-1));

                SharedPreferences.Editor prefsEditor = userPreferences.edit();
                json = gson.toJson(userProfile);
                prefsEditor.putString("LastProfileUsed", json).apply();

                Toast.makeText(getActivity(), "Transfer of $" + String.format(Locale.getDefault(), "%.2f",transferAmount) + " successfully made", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
