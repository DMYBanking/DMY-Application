package com.bankingapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bankingapplication.Model.Clerk;
import com.bankingapplication.Model.Profile;
import com.example.mikebanks.bankscorpfinancial.R;

import java.util.ArrayList;

public class ClerkAdapter extends ArrayAdapter<Clerk>
{
	private Context context;
	private int resource;

	public ClerkAdapter(Context context, int resource, ArrayList<Clerk> clerks)
	{
		super(context,resource,clerks);
		this.context = context;
		this.resource = resource;
	}

	@Override
	@NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(resource, parent, false);
		}

		Clerk clerk = getItem(position);

		TextView txtAccountName = convertView.findViewById(R.id.txt_profile_name);
		txtAccountName.setText(clerk.getFirstName() + " " + clerk.getLastName());

		return convertView;
	}
}
