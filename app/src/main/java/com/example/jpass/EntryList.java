package com.example.jpass;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

public class EntryList extends AppCompatActivity {

    RecyclerView rv_entryList;
    FloatingActionButton bt_addEntry;
    String username, userHash;
    SecretKeySpec genKey;
    DBHelper db;
    ArrayList<String> logID, logName, logPW, logDesc;
    CustomAdapter customAdapter;
    ImageView iv_empty;
    TextView tv_empty;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);
        setTitle("Entry List");

        tv_empty = (TextView) findViewById(R.id.tv_empty);
        iv_empty = (ImageView) findViewById(R.id.iv_empty);

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

        rv_entryList = findViewById(R.id.rv_entryList);
        bt_addEntry = findViewById(R.id.bt_addEntry);
        db = new DBHelper(EntryList.this);

        logID = new ArrayList<>();
        logName = new ArrayList<>();
        logPW = new ArrayList<>();
        logDesc = new ArrayList<>();

        bt_addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addOneEntry = new Intent(EntryList.this, EntryAddOne.class);
                addOneEntry.putExtra("profileUser", username);
                //startActivity(addOneEntry);
                summonRecreate.launch(addOneEntry);
            }
        });



        storeDataInArrays();

        customAdapter = new CustomAdapter(EntryList.this, this, logID, logName, logPW, logDesc, username);
        rv_entryList.setAdapter(customAdapter);
        rv_entryList.setLayoutManager(new LinearLayoutManager(EntryList.this));

    }

    ActivityResultLauncher<Intent> summonRecreate = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 1) {
                        recreate();
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void storeDataInArrays() {
        Cursor cursor = db.readEntryList(username);
        String plainLogName, plainLogPW, plainLogDesc;
        if (cursor.getCount() == 0) {
            iv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {

                plainLogDesc = Security.decrypt(cursor.getString(2), genKey);
                plainLogName = Security.decrypt(cursor.getString(3), genKey);
                plainLogPW = Security.decrypt(cursor.getString(4), genKey);

                logID.add(cursor.getString(0));
                logName.add(plainLogName);
                logPW.add(plainLogPW);
                logDesc.add(plainLogDesc);
            }
            iv_empty.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
        }
    }

}