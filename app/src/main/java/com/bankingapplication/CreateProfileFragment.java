package com.bankingapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bankingapplication.Model.Admin;
import com.bankingapplication.Model.Clerk;
import com.bankingapplication.Model.Profile;
import com.bankingapplication.Model.db.ApplicationDB;
import com.example.mikebanks.bankscorpfinancial.R;

import java.util.ArrayList;

public class CreateProfileFragment extends Fragment
{
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtCountry;
    private EditText edtemail;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtPasswordConfirm;

    public CreateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Create Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_profile, container, false);

        edtFirstName = rootView.findViewById(R.id.edt_first_name);
        edtLastName = rootView.findViewById(R.id.edt_last_name);
//        edtemail = rootView.findViewById(R.id.edt_email);
        edtCountry = rootView.findViewById(R.id.edt_country);
        edtUsername = rootView.findViewById(R.id.edt_username);
        edtPassword = rootView.findViewById(R.id.edt_password);
        edtPasswordConfirm = rootView.findViewById(R.id.edt_password_confirm);
        Button btnCreateAccount = rootView.findViewById(R.id.btn_create_account);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProfile();
            }
        });
//        Button btnCreateAdmin = rootView.findViewById(R.id.createAdminBtn);
//        btnCreateAdmin.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                createAdmin();
//            }
//        });
//        Button btnCreateClerk = rootView.findViewById(R.id.createClerkBtn);
//        btnCreateClerk.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                createClerk();
//            }
//        });

        ((LaunchActivity) getActivity()).showUpButton();

        return rootView;
    }


    private void createAdmin()
    {
        ApplicationDB applicationDb = new ApplicationDB( getActivity().getApplicationContext());
        ArrayList<Admin> profiles = applicationDb.getAllAdmins();
        boolean usernameTaken = false;

        for (int iAdmin = 0; iAdmin < profiles.size(); iAdmin++) {
            if (edtUsername.getText().toString().equals(profiles.get(iAdmin).getUsername())) {
                usernameTaken = true;
            }
        }

        if (edtFirstName.getText().toString().equals("") || edtLastName.getText().toString().equals("") ||
            edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("") || edtPasswordConfirm.getText().toString().equals("")) {
            Toast.makeText(getActivity(), R.string.fields_blank, Toast.LENGTH_SHORT).show();
        }

        else if (!(edtPassword.getText().toString().equals(edtPasswordConfirm.getText().toString()))) {
            Toast.makeText(getActivity(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
        }
        else if (usernameTaken) {
            Toast.makeText(getActivity(), "A User has already taken that username", Toast.LENGTH_SHORT).show();
        }
        else {
            Admin admin = new Admin(edtFirstName.getText().toString(), edtLastName.getText().toString(),
                    edtCountry.getText().toString(),edtUsername.getText().toString(), edtPassword.getText().toString());

            applicationDb.saveNewUser(admin);
            applicationDb.saveNewAdmin(admin);

            Bundle bundle = new Bundle();
            bundle.putString("Username", admin.getUsername());
            bundle.putString("Password", admin.getPassword());

            ((LaunchActivity) getActivity()).profileCreated(bundle);

        }
    }

    /**
     * Method to create new clerk user
     */
    private void createClerk()
    {
        ApplicationDB applicationDb = new ApplicationDB( getActivity().getApplicationContext());
        ArrayList<Clerk> clerks = applicationDb.getAllClerks();
        boolean usernameTaken = false;

        for (int i = 0; i < clerks.size(); i++)
        {
            if (edtUsername.getText().toString().equals(clerks.get(i).getUsername()))
            {
                usernameTaken = true;
            }
        }

        if (edtFirstName.getText().toString().equals("") || edtLastName.getText().toString().equals("") || edtCountry.getText().toString().equals("") ||
            edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("") || edtPasswordConfirm.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(), R.string.fields_blank, Toast.LENGTH_SHORT).show();
        }

        else if (!(edtPassword.getText().toString().equals(edtPasswordConfirm.getText().toString())))
        {
            Toast.makeText(getActivity(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
        }
        else if (usernameTaken)
        {
            Toast.makeText(getActivity(), "A User has already taken that username", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Clerk userClerk = new Clerk(edtFirstName.getText().toString(), edtLastName.getText().toString(), edtCountry.getText().toString(),
                    edtUsername.getText().toString(), edtPassword.getText().toString());

            applicationDb.saveNewUser(userClerk);
            applicationDb.saveNewClerk(userClerk);

            Bundle bundle = new Bundle();
            bundle.putString("Username", userClerk.getUsername());
            bundle.putString("Password", userClerk.getPassword());

            ((LaunchActivity) getActivity()).profileCreated(bundle);

        }
    }

    /**
     * method used to create an account
     */
    private void createProfile()
    {
        ApplicationDB applicationDb = new ApplicationDB(getActivity().getApplicationContext());
        ArrayList<Profile> profiles = applicationDb.getAllProfiles();
        boolean usernameTaken = false;

        for (int iProfile = 0; iProfile < profiles.size(); iProfile++)
        {
            if (edtUsername.getText().toString().equals(profiles.get(iProfile).getUsername()))
            {
                usernameTaken = true;
            }
        }

        if (edtFirstName.getText().toString().equals("") || edtLastName.getText().toString().equals("") || edtCountry.getText().toString().equals("") ||
                edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("") || edtPasswordConfirm.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(), R.string.fields_blank, Toast.LENGTH_SHORT).show();
        }

        else if (!(edtPassword.getText().toString().equals(edtPasswordConfirm.getText().toString())))
        {
            Toast.makeText(getActivity(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
        }
        else if (usernameTaken)
        {
            Toast.makeText(getActivity(), "A User has already taken that username", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Profile userProfile = new Profile(edtFirstName.getText().toString(), edtLastName.getText().toString(), edtCountry.getText().toString(),
                    edtUsername.getText().toString(), edtPassword.getText().toString());

            applicationDb.saveNewUser(userProfile);
            applicationDb.saveNewProfile(userProfile);

            Bundle bundle = new Bundle();
            bundle.putString("Username", userProfile.getUsername());
            bundle.putString("Password", userProfile.getPassword());

            ((LaunchActivity) getActivity()).profileCreated(bundle);

        }
    }
}
