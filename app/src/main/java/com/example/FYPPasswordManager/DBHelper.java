package com.example.FYPPasswordManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "pwmgr.db";
    public static final String PROFILE_TABLE = "profile_table";
    public static final String PW_TABLE = "pw_table";
    public static final String COL_ID = "ID";
    public static final String COL_PROF_USER = "profile_username";
    public static final String COL_HASH = "HASH";
    public static final String COL_LOG_DESC = "login_site";
    public static final String COL_LOG_USER = "login_username";
    public static final String COL_LOG_PW = "login_pw";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + PROFILE_TABLE + " ("   //master login table
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_PROF_USER + " TEXT UNIQUE, "
                    + COL_HASH + " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + PW_TABLE + " ("   //login info database
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_PROF_USER + " TEXT, "
                    + COL_LOG_DESC + " TEXT, "
                    + COL_LOG_USER + " TEXT, "
                    + COL_LOG_PW + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PW_TABLE);
        onCreate(db);
    }

    public boolean addProfile(ProfileModel profileModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_PROF_USER, profileModel.getUsername());
        cv.put(COL_HASH, profileModel.getHash());

        long insert = db.insert(PROFILE_TABLE, null, cv);
        return insert != -1;
    }

    public boolean passCheck(String password, String username) {

        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + PROFILE_TABLE + " WHERE " + COL_PROF_USER + " LIKE '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            if (password.equals(cursor.getString(2))) {
                return true;
            }
        }

        return false;
    }

    void updateProfilePassword(String password, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "SELECT * FROM " + PROFILE_TABLE + " WHERE " + COL_PROF_USER + " LIKE '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);

        ContentValues cv = new ContentValues();

        cv.put(COL_HASH, password);

        if (cursor.moveToFirst()) {
            long result = db.update(PROFILE_TABLE, cv, "profile_username=?", new String[]{username});
            if (result == 1) {
                Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean addEncryptedEntry(String profileName, String entryUser, String entryPW, String entryDesc) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_PROF_USER, profileName);
        cv.put(COL_LOG_USER, entryUser);
        cv.put(COL_LOG_PW, entryPW);
        cv.put(COL_LOG_DESC, entryDesc);

        long insert = db.insert(PW_TABLE, null, cv);
        return insert != -1;

    }

    void updateEntry(String id, String name, String PW, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_LOG_USER, name);
        cv.put(COL_LOG_PW, PW);
        cv.put(COL_LOG_DESC,desc);

        long result = db.update(PW_TABLE, cv, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteEntry(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(PW_TABLE, "id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Failed to delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public List<ProfileModel> getEveryone() {

        List<ProfileModel> returnList = new ArrayList<>();

        //get data from db
        String queryString = "SELECT * FROM " + PROFILE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
        // loop through the cursor
            do {
                int id = cursor.getInt(0);
                String username = cursor.getString(1);
                String pw_hash = cursor.getString(2);

                ProfileModel newProfile = new ProfileModel(id, username, pw_hash);
                returnList.add(newProfile);
            } while (cursor.moveToNext());

        } else {
            //nothing, do nothing to the list
        }

        return returnList;
    }

    public long validateProfile(String username, String pw_hash) {

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + PROFILE_TABLE + " WHERE " + COL_PROF_USER + " LIKE '" + username + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        List<ProfileModel> everyone = getEveryone();

        if (cursor.getCount() > 0) {
            for (int i = 0; i < everyone.size(); i++) {
                if (username.equals(everyone.get(i).getUsername()) && pw_hash.equals(everyone.get(i).getHash())) {
                    return 1;
                }
            }
        } else {
            //no profile
            return 0;
        }
        //error
        return -1;
    }
    
    Cursor readEntryList(String username) {

        String query = "SELECT * FROM " + PW_TABLE + " WHERE " + COL_PROF_USER + " LIKE '" + username + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    void deleteProfile(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryEntries = "DELETE FROM " + PW_TABLE + " WHERE " + COL_PROF_USER + " LIKE '" + username + "'";
        String queryProfile = "DELETE FROM " + PROFILE_TABLE + " WHERE " + COL_PROF_USER + " LIKE '" + username + "'";

        db.execSQL(queryEntries);
        db.execSQL(queryProfile);

    }
}
