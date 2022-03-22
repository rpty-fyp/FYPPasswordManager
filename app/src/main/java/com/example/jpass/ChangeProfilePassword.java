package com.example.jpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeProfilePassword extends AppCompatActivity {

    EditText et_passCurrent, et_passNew, et_passNewConfirm;
    Button bt_changePass;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_password);
        setTitle("Master Password Change");

        et_passCurrent = (EditText) findViewById(R.id.et_passCurrent);
        et_passNew = (EditText) findViewById(R.id.et_passNew);
        et_passNewConfirm = (EditText) findViewById(R.id.et_passNewConfirm);
        bt_changePass = (Button) findViewById(R.id.bt_confirmChangePass);

        Intent intent = getIntent();
        username = intent.getStringExtra("profileUser");

        bt_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String passCurrent = Security.md5(et_passCurrent.getText().toString());
                String passNew = Security.md5(et_passNew.getText().toString());
                String passNewConfirm = Security.md5(et_passNewConfirm.getText().toString());
                DBHelper db = new DBHelper(ChangeProfilePassword.this);

                if (!et_passCurrent.getText().toString().equals("") && !et_passNew.getText().toString().equals("") && !et_passNewConfirm.getText().toString().equals("")) {

                    try {

                        if (db.passCheck(passCurrent, username) == false) {
                            Toast.makeText(ChangeProfilePassword.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                        else if (!passNew.equals(passNewConfirm)) {
                            Toast.makeText(ChangeProfilePassword.this, "New passwords must match", Toast.LENGTH_SHORT).show();
                        } else {
                            db.updateProfilePassword(passNew, username);
                        }
                    } catch (Exception e) {
                        Toast.makeText(ChangeProfilePassword.this, "Error changing password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ChangeProfilePassword.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}