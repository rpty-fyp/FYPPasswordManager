package com.example.jpass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class EntryUpdate extends AppCompatActivity {

    EditText et_logNameUpdate, et_logPWUpdate, et_logDescUpdate;
    Button bt_updateEntry, bt_deleteEntry, bt_passGen, bt_cancelUpdate;
    DBHelper db;
    String username, userHash;
    SecretKeySpec genKey;

    String id, name, PW, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_update);
        setTitle("Update Entry");

        et_logNameUpdate = (EditText) findViewById(R.id.et_logNameUpdate);
        et_logPWUpdate = (EditText) findViewById(R.id.et_logPWUpdate);
        et_logDescUpdate = (EditText) findViewById(R.id.et_logDescUpdate);
        bt_updateEntry = (Button) findViewById(R.id.bt_updateOne);
        bt_deleteEntry = (Button) findViewById(R.id.bt_deleteOne);
        bt_passGen = (Button) findViewById(R.id.bt_passGenUpdate);
        bt_cancelUpdate = (Button) findViewById(R.id.bt_cancelUpdate);

        db = new DBHelper(EntryUpdate.this);

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

        acquireDataFromIntent();

        bt_updateEntry.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String encLogName, encLogPW, encLogDesc;

                encLogName = Security.encrypt(et_logNameUpdate.getText().toString().trim(), genKey);
                encLogPW = Security.encrypt(et_logPWUpdate.getText().toString().trim(), genKey);
                encLogDesc = Security.encrypt(et_logDescUpdate.getText().toString().trim(), genKey);

                db.updateEntry(id, encLogName, encLogPW, encLogDesc);

                setResult(1);
                finish();
            }
        });

        bt_cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_passGen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                String passwordGenerated;

                passwordGenerated = Security.generateStrongPassword();
                et_logPWUpdate.setText(passwordGenerated);
            }
        });

        bt_deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void acquireDataFromIntent() {
        if ((getIntent().hasExtra("id")) && (getIntent().hasExtra("name"))
                && (getIntent().hasExtra("PW")) && (getIntent().hasExtra("desc"))) {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            PW = getIntent().getStringExtra("PW");
            desc = getIntent().getStringExtra("desc");

            et_logNameUpdate.setText(name);
            et_logPWUpdate.setText(PW);
            et_logDescUpdate.setText(desc);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + desc + " ?");
        builder.setMessage("Are you sure you want to delete " + desc + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBHelper db = new DBHelper(EntryUpdate.this);
                db.deleteEntry(id);
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