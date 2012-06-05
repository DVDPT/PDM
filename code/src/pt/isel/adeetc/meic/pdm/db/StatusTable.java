package pt.isel.adeetc.meic.pdm.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public final class StatusTable
{


    public static final String TABLE_STATUS = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DATE = "date";

    public static final int COLUMN_ID_INDEX = 0;
    public static final int COLUMN_USERNAME_INDEX = 1;
    public static final int COLUMN_MESSAGE_INDEX = 2;
    public static final int COLUMN_DATE_INDEX = 3;

    public static final String[] COLUMN_ALL = {COLUMN_ID, COLUMN_USERNAME, COLUMN_MESSAGE, COLUMN_DATE};


    private static final String LOG = "StatusTable";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STATUS
            + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_USERNAME + " text not null, "
            + COLUMN_MESSAGE + " text not null,"
            + COLUMN_DATE + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion)
    {
        Log.w(LOG, "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        onCreate(database);
    }
}
