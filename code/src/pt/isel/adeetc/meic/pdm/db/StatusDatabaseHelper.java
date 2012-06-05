package pt.isel.adeetc.meic.pdm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatusDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "statustable.db";
    private static final int DATABASE_VERSION = 1;

    public StatusDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        StatusTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion)
    {
        StatusTable.onUpgrade(database, oldVersion, newVersion);
    }
}
