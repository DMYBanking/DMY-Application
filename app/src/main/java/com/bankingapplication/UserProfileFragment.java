package com.bankingapplication;

import static android.content.Context.MODE_PRIVATE;
import static android.media.CamcorderProfile.get;

import static java.sql.Types.NULL;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import  android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bankingapplication.Adapters.AccountAdapter;
import com.bankingapplication.Model.Account;
import com.bankingapplication.Model.Profile;
import com.example.mikebanks.bankscorpfinancial.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class UserProfileFragment extends Fragment {

    private  Profile userProfile;
    private SharedPreferences userPreferences;
    private TextView user_name,lstAccounts;
    private Gson gson;
    private String json;
    private ListView test;


    public UserProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_user_profile, container, false);
        String name = "";
        gson = new Gson();
        Button add_account = rootView.findViewById(R.id.add_account_b);
        user_name = rootView.findViewById(R.id.profile_user_name);
        lstAccounts = rootView.findViewById(R.id.disp_accounts_counter);
        userPreferences = getActivity().getSharedPreferences("LastProfileUsed",MODE_PRIVATE);
        json = userPreferences.getString("profileUser","");
        userProfile = gson.fromJson(json,Profile.class);
        user_name.setText(userProfile.getUsername());
        if(userProfile.get_accounts_counter() > 0)
            lstAccounts.setText(Integer.toString(userProfile.get_accounts_counter()));
        else
            lstAccounts.setText("0");

        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("DisplayAccountDialog", true);
                ((DrawerActivity) getActivity()).manualNavigation(DrawerActivity.manualNavID.ACCOUNTS_ID, bundle);
            }
        });

        return rootView;

    }
}