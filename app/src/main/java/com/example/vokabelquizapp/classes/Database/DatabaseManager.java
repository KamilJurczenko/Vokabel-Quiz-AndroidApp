package com.example.vokabelquizapp.classes.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import io.requery.android.database.sqlite.SQLiteDatabase;

import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseManager {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private DataBaseHelper openDbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context, String dbName) {
        this.mContext = context;
        openDbHelper = new DataBaseHelper(mContext, dbName);
    }

    public DatabaseManager createDatabase() throws SQLException {
        try {
            openDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DatabaseManager open() throws SQLException {
        try {
            openDbHelper.openDataBase();
            openDbHelper.close();
            database = openDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close(){
        if(database != null){
            this.database.close();
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> getVocabCounterPart(String word){
        //System.out.println(word);
        word = word.replace(",", "");
        ArrayList<String> resultList = new ArrayList<>();

        //String sql_Query = "SELECT written_rep,trans_list FROM simple_translation WHERE trans_list = '"+word+"'";
        String sql_Query = "SELECT * FROM simple_translation WHERE rowid IN " +
                "(SELECT rowid FROM fts_table WHERE fts_table MATCH ?)";
        word = "\"" + word + "\"";
        String[] selectionArgs = { word };
        Cursor c = database.rawQuery(sql_Query, selectionArgs);
        if(c.moveToFirst()){
            do{
                resultList.add(c.getString(c.getColumnIndex("written_rep")));
                //System.out.println(c.getString(c.getColumnIndex("written_rep")));
            } while(c.moveToNext());
        }
        c.close();
        return resultList;
    }
}
