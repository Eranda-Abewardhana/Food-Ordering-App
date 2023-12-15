package com.example.wavesoffood.Database;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.List;

public class OrmliteHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "food_.db";
    private static final int DB_VERSION = 1;
    private Context context;
    // Public methods
    public OrmliteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
       getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DishDTO.class);
        } catch (SQLException | java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
    public <T> List getAll(Class clazz) throws SQLException, java.sql.SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryForAll();
    }
    public <T> Dao.CreateOrUpdateStatus createOrUpdate(T obj) throws SQLException, java.sql.SQLException {
        Dao<T, ?> dao = (Dao<T, ?>) getDao(obj.getClass());
        return dao.createOrUpdate(obj);
    }
}
