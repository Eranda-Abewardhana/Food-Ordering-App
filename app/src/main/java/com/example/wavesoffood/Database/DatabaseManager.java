package com.example.wavesoffood.Database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public class DatabaseManager<H extends OrmLiteSqliteOpenHelper> {

private H helper;
	
	@SuppressWarnings("unchecked")
    public H getHelper(Context context, Class<H> helperClass) {
        if (helper == null || !helperClass.isInstance(helper)) {
            // If the helper is null or of a different type, create a new one.
            helper = OpenHelperManager.getHelper(context, helperClass);
        }
        return helper;
    }


}
