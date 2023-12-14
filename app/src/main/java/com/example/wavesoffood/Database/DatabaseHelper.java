package com.example.wavesoffood.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private Context context;
    // Dish table columns
    public static final String DISH_NAME = "name";
    public static final String TABLE_NAME = "dishes";
    public static final String DISH_DESCRIPTION = "description";
    public static final String ID = "id";
    public static final String DISH_INSTOCK = "in_stock";
    public static final String DISH_ISVEG = "is_veg";
    public static final String DISH_PRICE = "price";
    public static final String DATABASE_NAME = "menu.db";
    public static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        DatabaseInitializer initializer = new DatabaseInitializer(context);
        try {
            initializer.createDatabase();
            initializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createDishTable(database);
    }

    private void createDishTable(SQLiteDatabase database) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DISH_NAME + " TEXT, " +
                DISH_DESCRIPTION + " TEXT, " +
                DISH_INSTOCK + " INTEGER, " +
                DISH_ISVEG + " INTEGER, " +
                DISH_PRICE + " REAL)";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
    public <T> Dao<T, String> getgdao(Class<T> clazz) throws SQLException {
        return DaoManager.createDao(getConnectionSource(), clazz);
    }
}
