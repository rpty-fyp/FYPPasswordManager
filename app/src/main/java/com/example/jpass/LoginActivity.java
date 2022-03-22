package com.example.jpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText et_username, pw_login;
    Button bt_login, bt_newprof, bt_howTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        et_username = (EditText) findViewById(R.id.et_username);
        pw_login = (EditText) findViewById(R.id.pw_login);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_newprof = (Button) findViewById(R.id.bt_create);
        bt_howTo = (Button) findViewById(R.id.bt_howTo);



        bt_newprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newprof = new Intent(LoginActivity.this, CreateProfileActivity.class);
                startActivity(newprof);
            }
        });

        bt_howTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent help = new Intent(LoginActivity.this, HelpActivity.class);
                startActivity(help);
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String pw_hash = Security.md5(pw_login.getText().toString());
                Intent goodLogin = new Intent(LoginActivity.this, MainActivity.class);
                DBHelper dbHelper;

                if (!et_username.getText().toString().equals("") && !pw_login.getText().toString().equals("")) {

                    dbHelper = new DBHelper(LoginActivity.this);
                    long result = dbHelper.validateProfile(username, pw_hash);
                    if (result == 1) {
                        goodLogin.putExtra("profileUser", username);
                        et_username.setText("");
                        pw_login.setText("");
                        startActivity(goodLogin);

                    } else if (result == 0){
                        Toast.makeText(LoginActivity.this, "Profile does not exist.", Toast.LENGTH_SHORT).show();
                    } else if (result == -1){
                        Toast.makeText(LoginActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}