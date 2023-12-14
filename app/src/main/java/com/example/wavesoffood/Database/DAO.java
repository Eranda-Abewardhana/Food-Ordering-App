package com.example.wavesoffood.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class DAO <T> {


    public static DatabaseHelper db;
    static DatabaseHelper ldb;
    public static SQLiteDatabase txn;


    private static boolean isInitialized = false;

    public static void init(Context context) {
        if (!isInitialized) {
            try {
                DatabaseManager<DatabaseHelper> manager = new DatabaseManager<>();
                db = manager.getHelper(context, DatabaseHelper.class);
                txn = db.getWritableDatabase();
                isInitialized = true; // Set the flag to true after initialization
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public DAO(DatabaseHelper db) {
    }


    public static <T> int create(T obj, Class<T> clazz) {

        try {

            Dao<T, String> Dao = db.getgdao(clazz);
            int id = obj.getClass().getDeclaredField("Id").getInt(obj);
            if (getById(id, clazz) == null) {

                return Dao.create(obj);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static <T> int update(T obj, Class<T> clazz) {
        try {

            Dao<T, String> Dao = db.getgdao(clazz);
            int id = obj.getClass().getDeclaredField("Id").getInt(obj);
            if (getById(id, clazz) != null) {
                return Dao.update(obj);
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static <T> int delete(T obj, Class<T> clazz) {
        try {
            Dao<T, String> Dao = db.getgdao(clazz);
            return Dao.delete(obj);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static <T> List<T> getAll(Class clazz) {
        try {

            Dao<T, String> Dao = db.getgdao(clazz);
            return Dao.queryForAll();

        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }


    public static <T> Dao<T, String> getdao(Class clazz) {
        try {

            Dao<T, String> Dao = db.getgdao(clazz);
            return Dao;

        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T getById(int id, Class<T> clazz) {
        try {

            Dao<T, String> Dao = db.getgdao(clazz);
            QueryBuilder<T, String> qb = Dao.queryBuilder();
            qb.where().eq("Id", id);
            PreparedQuery<T> pq = qb.prepare();
            return Dao.queryForFirst(pq);

        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

}
