package pt.isel.adeetc.meic.pdm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.common.collect.Iterables;
import pt.isel.adeetc.meic.pdm.common.*;
import pt.isel.adeetc.meic.pdm.common.db.DbSetOnCompletedOperationEventData;
import pt.isel.adeetc.meic.pdm.common.db.IDataSource;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import winterwell.jtwitter.Twitter;

import java.util.*;

public class StatusDatabaseDataSource implements IDbSet<Twitter.ITweet>, IDataSource
{
    private static final String LOG = "StatusDatabaseDataSource";
    private final StatusDatabaseHelper _statusHelper;
    private SQLiteDatabase _database;
    private final IEvent<DbSetOnCompletedOperationEventData> _onCompletedOperationEvent;


    public StatusDatabaseDataSource(Context ctx)
    {
        _statusHelper = new StatusDatabaseHelper(ctx);
        _onCompletedOperationEvent = new GenericEvent<DbSetOnCompletedOperationEventData>();
    }

    @Override
    public void open()
    {
        if(_database != null)
            throw new ShouldNotHappenException("StatusDatabaseDataSource:open method called twice.");
        _database = _statusHelper.getWritableDatabase();
    }

    @Override
    public boolean isOpen()
    {
        return _database != null;
    }

    @Override
    public void close()
    {
        _database.close();
        _statusHelper.close();
    }

    @Override
    public Twitter.ITweet create()
    {
        throw new UnsupportedOperationException("StatusDatabaseDataSource.create");
    }

    @Override
    public IEvent<DbSetOnCompletedOperationEventData> getOnOperationCompletedEvent()
    {
        return _onCompletedOperationEvent;
    }

    @Override
    public boolean add(Twitter.ITweet status)
    {
        ContentValues values = new ContentValues();
        values.put(StatusTable.COLUMN_ID, status.getId());
        values.put(StatusTable.COLUMN_MESSAGE, status.getText());
        values.put(StatusTable.COLUMN_DATE, status.getCreatedAt().getTime());
        values.put(StatusTable.COLUMN_USERNAME, status.getUser().getScreenName());
        _database.insert(StatusTable.TABLE_STATUS, null, values);
        return true;
    }

    @Override
    public void addAsync(Twitter.ITweet iTweet)
    {
        final Twitter.ITweet tweet = iTweet;
        EventHelper.executeAndCallHandlerAsync(
                getOnOperationCompletedEvent(),
                new Func<DbSetOnCompletedOperationEventData>()
                {
                    @Override
                    public DbSetOnCompletedOperationEventData perform()
                    {
                        DbSetOnCompletedOperationEventData ret = new DbSetOnCompletedOperationEventData(DBSET_OPERATION_ADD);
                        ret.setData(add(tweet));
                        return ret;
                    }
                },
                this);
    }

    @Override
    public boolean remove(Twitter.ITweet status)
    {
        long id = status.getId();
        Log.d(LOG, "Comment deleted with id: " + id);
        _database.delete(StatusTable.TABLE_STATUS, StatusTable.COLUMN_ID + " = ?", new String[]{id + ""});
        return true;
    }

    @Override
    public void removeAsync(Twitter.ITweet iTweet)
    {
        final Twitter.ITweet tweet = iTweet;
        EventHelper.executeAndCallHandlerAsync(
                getOnOperationCompletedEvent(),
                new Func<DbSetOnCompletedOperationEventData>()
                {
                    @Override
                    public DbSetOnCompletedOperationEventData perform()
                    {
                        DbSetOnCompletedOperationEventData ret = new DbSetOnCompletedOperationEventData(DBSET_OPERATION_REMOVE);
                        ret.setData(remove(tweet));
                        return ret;
                    }
                },
                this);
    }

    @Override
    public boolean clearAll()
    {
        _database.delete(StatusTable.TABLE_STATUS, null, null);
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearAllAsync()
    {

        EventHelper.executeAndCallHandlerAsync(
                getOnOperationCompletedEvent(),
                new Func<DbSetOnCompletedOperationEventData>()
                {
                    @Override
                    public DbSetOnCompletedOperationEventData perform()
                    {
                        DbSetOnCompletedOperationEventData ret = new DbSetOnCompletedOperationEventData(DBSET_OPERATION_CLEAR_ALL);
                        clearAll();
                        return ret;
                    }
                },
                this);
    }

    @Override
    public void getAllAsync()
    {
        EventHelper.executeAndCallHandlerAsync(
                getOnOperationCompletedEvent(),
                new Func<DbSetOnCompletedOperationEventData>()
                {
                    @Override
                    public DbSetOnCompletedOperationEventData perform()
                    {
                        DbSetOnCompletedOperationEventData ret = new DbSetOnCompletedOperationEventData(DBSET_OPERATION_GET_ALL);
                        Iterator<Twitter.ITweet> tweets = iterator();
                        Collection<Twitter.ITweet> returnCollection = new LinkedList<>();
                        //
                        //  Force the read into the db;
                        //
                        Iterables.addAll(returnCollection,StatusDatabaseDataSource.this);
                        ret.setData(returnCollection);
                        return ret;
                    }
                },
                this);
    }


    @Override
    public Iterator<Twitter.ITweet> iterator()
    {
        List<Twitter.ITweet> tweets = new LinkedList<Twitter.ITweet>();

        Cursor cursor = _database.query(StatusTable.TABLE_STATUS,
                StatusTable.COLUMN_ALL, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Twitter.ITweet tweet = cursorToTweet(cursor);
            tweets.add(tweet);
            cursor.moveToNext();
        }
        cursor.close();
        return tweets.iterator();
    }

    private Twitter.ITweet cursorToTweet(Cursor cursor)
    {
        return new YambaTweet
                (
                        cursor.getLong(StatusTable.COLUMN_ID_INDEX),
                        new Date(cursor.getLong(StatusTable.COLUMN_DATE_INDEX)),
                        cursor.getString(StatusTable.COLUMN_MESSAGE_INDEX),
                        cursor.getString(StatusTable.COLUMN_USERNAME_INDEX)
                );
    }
}
