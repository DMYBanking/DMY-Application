package com.bankingapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bankingapplication.Model.Profile;
import com.example.mikebanks.bankscorpfinancial.R;

import java.util.ArrayList;

/**
 * Adapter for displaying profiles
 */
public class ProfileAdapter extends ArrayAdapter<Profile>
{
	private Context context;
	private int resource;

	public ProfileAdapter(Context context, int resource, ArrayList<Profile> profiles)
	{
		super(context,resource,profiles);
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

		Profile profile = getItem(position);

		TextView txtAccountName = convertView.findViewById(R.id.txt_profile_name);
		txtAccountName.setText(profile.getFirstName() + " " + profile.getLastName());
		TextView txtUserName = convertView.findViewById(R.id.txt_profile_username);
		txtUserName.setText(profile.getUsername());

		return convertView;
	}
}
