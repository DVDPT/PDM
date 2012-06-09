package pt.isel.adeetc.meic.pdm.services;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.google.common.base.Predicate;
import pt.isel.adeetc.meic.pdm.common.Arrays;
import pt.isel.adeetc.meic.pdm.db.StatusDatabaseHelper;
import pt.isel.adeetc.meic.pdm.db.StatusTable;

import java.security.InvalidParameterException;

public class TimelineContentProvider extends ContentProvider
{
    private static final int STATUSES = 1;
    private static final int STATUS_ID = 2;

    private static final String AUTHORITY = "pt.isel.adeetc.meic.pdm.timeline.contentprovider";

    private static final String BASE_PATH = "statuses";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/statuses";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/status";


    private static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        URIMatcher.addURI(AUTHORITY, BASE_PATH, STATUSES);
        URIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", STATUS_ID);
    }

    private SQLiteDatabase _database;
    private StatusDatabaseHelper _dbHelper;
    private Predicate<String> _statusTableProjectionPredicate = new Predicate<String>()
    {
        @Override
        public boolean apply(String s)
        {
            return Arrays.contains(StatusTable.COLUMN_ALL, s);
        }
    };

    private SQLiteDatabase getDatabaseInstance()
    {

        if (_database == null)
            _database = _dbHelper.getWritableDatabase();

        return _database;
    }

    @Override
    public boolean onCreate()
    {
        _dbHelper = new StatusDatabaseHelper(getContext());
        return true;
    }

    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        if (!Arrays.all(projection, _statusTableProjectionPredicate))
            throw new InvalidParameterException("Projection have unknown collumns.");


        // Set the table
        queryBuilder.setTables(StatusTable.TABLE_STATUS);

        int uriType = URIMatcher.match(uri);
        switch (uriType)
        {
            case STATUSES:
                break;
            case STATUS_ID:

                queryBuilder.appendWhere(StatusTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = getDatabaseInstance();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public synchronized Uri insert(Uri uri, ContentValues contentValues)
    {
        int uriType = URIMatcher.match(uri);
        SQLiteDatabase database = getDatabaseInstance();
        long id;
        switch (uriType)
        {
            case STATUSES:
                id = database.insertWithOnConflict(StatusTable.TABLE_STATUS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public synchronized int delete(Uri uri, String selection, String[] args)
    {
        int uriType = URIMatcher.match(uri);
        SQLiteDatabase database = getDatabaseInstance();
        int rowsDeleted;
        switch (uriType)
        {
            case STATUSES:
                rowsDeleted = database.delete(StatusTable.TABLE_STATUS, selection, args);
                break;
            case STATUS_ID:
                String id = uri.getLastPathSegment();
                rowsDeleted = database.delete(StatusTable.TABLE_STATUS, StatusTable.COLUMN_ID + "=" + id, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        throw new UnsupportedOperationException();
    }
}
