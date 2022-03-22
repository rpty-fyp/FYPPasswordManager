package com.example.jpass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView profileView;
    Button viewEntryList, changeProfPass, logOut, deleteProfile;
    String username, welcomeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Password Manager");

        profileView = (TextView) findViewById(R.id.tv_profileView);
        viewEntryList = (Button) findViewById(R.id.bt_viewEntryList);
        changeProfPass = (Button) findViewById(R.id.bt_changeProfPass);
        logOut = (Button) findViewById(R.id.bt_logOut);
        deleteProfile = (Button) findViewById(R.id.bt_deleteProfile);

        Intent intent = getIntent();
        username = intent.getStringExtra("profileUser");
        welcomeMsg = "Welcome, " + username + "!";

        profileView.setText(welcomeMsg);

        viewEntryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewEntries = new Intent(MainActivity.this, EntryList.class);
                viewEntries.putExtra("profileUser", username);
                startActivity(viewEntries);
            }
        });

        changeProfPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePass = new Intent(MainActivity.this, ChangeProfilePassword.class);
                changePass.putExtra("profileUser", username);
                startActivity(changePass);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete profile?");
        builder.setMessage("Are you sure you want to delete your profile and all its entries? You CANNOT UNDO this process.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBHelper db = new DBHelper(MainActivity.this);
                db.deleteProfile(username);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

}