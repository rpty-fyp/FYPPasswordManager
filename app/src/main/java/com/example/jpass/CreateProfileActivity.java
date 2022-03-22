package com.example.jpass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateProfileActivity extends AppCompatActivity {

    EditText et_create_user, et_create_pass, et_confirm_pass;
    Button bt_newprof_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        setTitle("Create Profile");

        et_create_user = (EditText) findViewById(R.id.et_create_user);
        et_create_pass = (EditText) findViewById(R.id.et_create_pass);
        et_confirm_pass = (EditText) findViewById(R.id.et_confirm_pass);
        bt_newprof_confirm = (Button) findViewById(R.id.bt_newprof_conf);

        bt_newprof_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newprof_name = et_create_user.getText().toString();
                String newprof_hashed_pw = Security.md5(et_create_pass.getText().toString());
                String newprof_hashed_pw_conf = Security.md5(et_confirm_pass.getText().toString());

                ProfileModel profileModel;
                DBHelper dbHelper;

                if (!newprof_name.equals("") && !et_create_pass.getText().toString().equals("") && !et_confirm_pass.getText().toString().equals("")) {

                    try {
                        if (!newprof_hashed_pw.equals(newprof_hashed_pw_conf)) {
                            Toast.makeText(CreateProfileActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                        } else {
                            profileModel = new ProfileModel(-1, newprof_name, newprof_hashed_pw);

                            dbHelper = new DBHelper(CreateProfileActivity.this);
                            dbHelper.addProfile(profileModel);
                            Toast.makeText(CreateProfileActivity.this, "Profile created", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CreateProfileActivity.this, "Error creating profile", Toast.LENGTH_SHORT).show();
                        profileModel = new ProfileModel(-1, "error", "error");
                    }

                } else {
                    Toast.makeText(CreateProfileActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}