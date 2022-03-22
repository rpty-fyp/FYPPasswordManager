package com.example.jpass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.spec.SecretKeySpec;

public class EntryAddOne extends AppCompatActivity {

    EditText et_logName, et_logPW, et_logDesc;
    Button bt_addOne, bt_passGen, bt_cancelAdd;
    String username, userHash;
    SecretKeySpec genKey;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_addone);
        setTitle("Add Entry ");

        et_logName = (EditText) findViewById(R.id.et_logName);
        et_logPW = (EditText) findViewById(R.id.et_logPW);
        et_logDesc = (EditText) findViewById(R.id.et_logDesc);
        bt_addOne = (Button) findViewById(R.id.bt_addOne);
        bt_passGen = (Button) findViewById(R.id.bt_passGen);
        bt_cancelAdd = (Button) findViewById(R.id.bt_cancelAdd);

        db = new DBHelper(EntryAddOne.this);

        Intent intent = getIntent();
        username = intent.getStringExtra("profileUser");
        userHash = Security.md5(username);

        try {
            genKey = Security.generateKey(userHash, username);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        bt_passGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String passwordGenerated;

                passwordGenerated = Security.generateStrongPassword();
                et_logPW.setText(passwordGenerated);
            }
        });

        bt_addOne.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String encLogName, encLogPW, encLogDesc;

                encLogName = Security.encrypt(et_logName.getText().toString().trim(), genKey);
                encLogPW = Security.encrypt(et_logPW.getText().toString().trim(), genKey);
                encLogDesc = Security.encrypt(et_logDesc.getText().toString().trim(), genKey);

                boolean success = db.addEncryptedEntry(username, encLogName, encLogPW, encLogDesc);
                if (success) {
                    Toast.makeText(EntryAddOne.this, "Added entry to database.", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(EntryAddOne.this, "Failed to add entry.", Toast.LENGTH_SHORT);
                };
                setResult(1);
                finish();

            }
        });

        bt_cancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}