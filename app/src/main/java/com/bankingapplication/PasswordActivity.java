package com.bankingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bankingapplication.Model.Admin;
import com.bankingapplication.Model.Profile;
import com.bankingapplication.Model.User;
import com.bankingapplication.Model.db.ApplicationDB;
import com.example.mikebanks.bankscorpfinancial.R;

import java.util.ArrayList;

public class PasswordActivity extends AppCompatActivity {

    private EditText username;
    private Button reset;
    private ApplicationDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        username = findViewById(R.id.user_name_rst);
        reset = findViewById(R.id.btn_reset);

        db = new ApplicationDB(this);
        ArrayList<Profile> profiles = db.getAllProfiles();


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                Boolean checkUser = false;
                for (int i = 0; i < profiles.size(); i++) {
                    if (user.equals(profiles.get(i).getUsername()))
                        checkUser = true;
                }
                if (checkUser) {
                    Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(PasswordActivity.this, "user does not exits", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}