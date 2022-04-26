package com.bankingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bankingapplication.Model.Admin;
import com.bankingapplication.Model.Clerk;
import com.bankingapplication.Model.Profile;
import com.bankingapplication.Model.db.ApplicationDB;
import com.example.mikebanks.bankscorpfinancial.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment
{
    private Bundle bundle;
    private String username;
    private String password;
    // defines user role - 1 (admin), 2 (clerk), 3 (user)
    private static int userRole;

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private CheckBox rememberMe;
    private Button btnCreateAccount;
    private Button btnLoginAdmin;
    private TextView bforgot;
    private Profile lastProfileUsed;
    private Gson gson;
    private String json;
    private SharedPreferences userPreferences;

    private final View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            if (view.getId() == btnLogin.getId())
            {
                validateAccount();
            }
            else if (view.getId() == btnCreateAccount.getId())
            {
                createAccount();
            }
            else if(view.getId() == btnLoginAdmin.getId())
            {
                // admin user
                userRole = 1;
                validateAdminAccount();
            }
        }
    };

    public LoginFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString("Username", "");
            password = bundle.getString("Password", "");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        edtUsername = rootView.findViewById(R.id.edt_username);
        edtPassword = rootView.findViewById(R.id.edt_password);
        btnLogin = rootView.findViewById(R.id.btn_login);
        rememberMe = rootView.findViewById(R.id.chk_remember);
        bforgot = rootView.findViewById(R.id.btn_reset_password);
        btnCreateAccount = rootView.findViewById(R.id.btn_create_account);
//        btnLoginAdmin = rootView.findViewById(R.id.btn_login_admin);

//        SharedPreferences preferences = getActivity().getSharedPreferences("checkbox",MODE_PRIVATE);
//        String checkbox =preferences.getString("remember","");
//        if (checkbox.equals("true"))
//        {
//            validateAccount();
//            Intent intent = new Intent(getActivity(),DrawerActivity.class);
//            startActivity(intent);
//        }else if(checkbox.equals("false"))
//        {
//            Toast.makeText(getActivity(),"Please Login",Toast.LENGTH_SHORT).show();
//        }
        getActivity().setTitle(getResources().getString(R.string.app_name));
        ((LaunchActivity) getActivity()).removeUpButton();

//        forgot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity().getApplicationContext(),PasswordActivity.class);
//                startActivity(intent);
//            }
//        });

        setupViews();

        if (bundle != null) {
            edtUsername.setText(username);
            edtPassword.setText(password);
            rememberMe.setChecked(true);
        }

        return rootView;
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setupViews()
    {
        btnLogin.setOnClickListener(clickListener);
        btnCreateAccount.setOnClickListener(clickListener);
//        btnLoginAdmin.setOnClickListener(clickListener);
        userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
//        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(buttonView.isChecked())
//                {
//                    SharedPreferences preferences = getActivity().getSharedPreferences("checkbox",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("remember","true");
//                    editor.apply();
//                }else if (!buttonView.isChecked())
//                {
//                    SharedPreferences preferences = getActivity().getSharedPreferences("checkbox",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("remember","false");
//                    editor.apply();
//                }
//            }
//        });
        rememberMe.setChecked(userPreferences.getBoolean("rememberMe", false));
        if (rememberMe.isChecked())
        {

            gson = new Gson();
            json = userPreferences.getString("profileUser", "");
            lastProfileUsed = gson.fromJson(json, Profile.class);

            //edtUsername.setText(lastProfileUsed.getUsername());
            //edtPassword.setText(lastProfileUsed.getPassword());

            //Automatic login for user
            //login();
            //finish();
        }
        //userPreferences = getActivity().getSharedPreferences("adminUser",MODE_PRIVATE);

    }

    @Override
    public void onStop()
    {
        if (lastProfileUsed != null) {
            if (edtUsername.getText().toString().equals(lastProfileUsed.getUsername()) && edtPassword.getText().toString().equals(lastProfileUsed.getPassword())) {
                userPreferences.edit().putBoolean("rememberMe", rememberMe.isChecked()).apply();
            } else {
                userPreferences.edit().putBoolean("rememberMe", false).apply();
            }
        }

        super.onStop();
    }

    private void validateAdminAccount()
    {
        ApplicationDB applicationDB = new ApplicationDB(getActivity().getApplicationContext());
        ArrayList<Admin> admins = applicationDB.getAllAdmins();
        boolean match = false;
        if (admins.size() > 0)
        {
            for (int i = 0; i < admins.size(); i++)
            {
                if (edtUsername.getText().toString().equals(admins.get(i).getUsername()) && edtPassword.getText().toString().equals(admins.get(i).getPassword()))
                {
                    match = true;
                    SharedPreferences.Editor prefsEditor = userPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(admins.get(i));
                    prefsEditor.putString("adminUser", json).apply();
                    ((LaunchActivity) getActivity()).login();
                    break;
                }
            }
            if (!match) {
                Toast.makeText(getActivity(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
        }
    }

    private void validateClerkAccount()
    {
        ApplicationDB applicationDB = new ApplicationDB(getActivity().getApplicationContext());
        ArrayList<Clerk> clerks = applicationDB.getAllClerks();
        boolean match = false;
        if(clerks.size() > 0)
        {
            for(int i = 0;i < clerks.size();i++)
            {
                if(edtUsername.getText().toString().equals(clerks.get(i).getUsername()) && edtPassword.getText().toString().equals(clerks.get(i).getPassword()))
                {
                    match = true;
                    SharedPreferences.Editor prefsEditor = userPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(clerks.get(i));
                    prefsEditor.putString("clerkUser",json).apply();
                    ((LaunchActivity)getActivity()).login();
                }
            }
            if(!match)
            {
                Toast.makeText(getActivity(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
        }
    }

    private void validateAccount()
    {
        ApplicationDB applicationDB = new ApplicationDB(getActivity().getApplicationContext());
        ArrayList<Profile> profiles = applicationDB.getAllProfiles();
        ArrayList<Admin> admins = applicationDB.getAllAdmins();
        ArrayList<Clerk> clerks = applicationDB.getAllClerks();
        boolean match = false;
        if (profiles.size() > 0)
        {
            //Checking if profile user
            for (int i = 0; i < profiles.size(); i++)
            {
                if (edtUsername.getText().toString().equals(profiles.get(i).getUsername()) && edtPassword.getText().toString().equals(profiles.get(i).getPassword()))
                {
                    match = true;
                    userRole = 3;
                    //userPreferences.edit().putBoolean("rememberMe", rememberMe.isChecked()).apply();
                    SharedPreferences.Editor prefsEditor = userPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(profiles.get(i));
                    //prefsEditor.putString("LastProfileUsed", json).apply();
                    prefsEditor.putString("profileUser", json).apply();
                    ((LaunchActivity)getActivity()).login();
                }
            }
            //Checking if admin user
            for(int i = 0;i < admins.size();i++)
            {
                if(match)
                    break;
                else if(edtUsername.getText().toString().equals(admins.get(i).getUsername()) && edtPassword.getText().toString().equals(admins.get(i).getPassword()))
                {
                    match = true;
                    userRole = 1;
                    //userPreferences.edit().putBoolean("rememberMe", rememberMe.isChecked()).apply();
                    SharedPreferences.Editor prefsEditor = userPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(admins.get(i));
                    //prefsEditor.putString("LastProfileUsed", json).apply();
                    prefsEditor.putString("adminUser", json).apply();
                    ((LaunchActivity)getActivity()).login();
                }
            }
            //Checking if clerk user
            for(int i = 0;i < clerks.size();i++)
            {
                if(match)
                    break;
                else if(edtUsername.getText().toString().equals(clerks.get(i).getUsername()) && edtPassword.getText().toString().equals(clerks.get(i).getPassword()))
                {
                    match = true;
                    userRole = 2;
                    //userPreferences.edit().putBoolean("rememberMe", rememberMe.isChecked()).apply();
                    SharedPreferences.Editor prefsEditor = userPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(profiles.get(i));
                    //prefsEditor.putString("LastProfileUsed", json).apply();
                    prefsEditor.putString("clerkUser", json).apply();
                    ((LaunchActivity)getActivity()).login();
                }
            }
            if (!match)
            {
                Toast.makeText(getActivity(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
            }
        }
        else
            {
            Toast.makeText(getActivity(), R.string.incorrect_login, Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * method that creates an account
     */
    private void createAccount()
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_frm_content, new CreateProfileFragment())
                .addToBackStack(null)
                .commit();
    }

    public static int getUserRole()
    {
        return userRole;
    }
}
