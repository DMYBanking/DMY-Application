package com.bankingapplication;

import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.bankingapplication.Model.Clerk;
import com.bankingapplication.Model.Profile;
import com.bankingapplication.Model.Transaction;
import com.example.mikebanks.bankscorpfinancial.R;
import com.google.gson.Gson;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class LoanFragment extends Fragment
{
	private TextView txtTitle;
	private Spinner spnLoans;
	private Gson gson;
	private Profile userProfile;
	private Clerk clerk;
	private SharedPreferences userPreferences;
	private ListView lstTransactions;
	private int selectedAccountIndex;
	private ArrayList<Transaction> loans;
	private ArrayAdapter<Transaction> loansAdapter;

	public LoanFragment()
	{
		//Empty constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_payment, container, false);
		txtTitle = rootView.findViewById(R.id.txt_loan_overview_title);
		spnLoans = rootView.findViewById(R.id.spn_list_loans);
		loans = new ArrayList<>();
		setValues();
		return rootView;
	}

	private void setValues()
	{
		userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
		gson = new Gson();
		String json = userPreferences.getString("clerkUser", "");
		clerk = gson.fromJson(json,Clerk.class);
		loans = clerk.getLoansToApprove();
		loansAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,loans);
		loansAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnLoans.setAdapter(loansAdapter);
	}

}
