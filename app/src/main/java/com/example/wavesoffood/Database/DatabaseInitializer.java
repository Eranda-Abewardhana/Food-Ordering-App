package com.example.wavesoffood.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseInitializer extends SQLiteOpenHelper{
    private static String DB_PATH = "/data/data/com.example.wavesoffood/databases/";

    private static String DB_NAME = "dishes.sqlite";

    public DatabaseInitializer(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }
    private boolean checkDatabase() throws IOException{
        Log.d("check db", "check db method");
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);

        }catch(SQLiteException e){

        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }
    public void createDatabase() throws IOException {
        Log.d("create db", "create db method");
        boolean dbExist = checkDatabase();

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
