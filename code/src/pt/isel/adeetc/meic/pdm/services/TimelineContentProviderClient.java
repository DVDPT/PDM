package pt.isel.adeetc.meic.pdm.services;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import pt.isel.adeetc.meic.pdm.db.StatusTable;
import pt.isel.adeetc.meic.pdm.db.YambaTweet;
import winterwell.jtwitter.Twitter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TimelineContentProviderClient implements IDbSet<Twitter.ITweet>
{
    private final ContentResolver _contentResolver;

    public TimelineContentProviderClient(Context ctx)
    {
        _contentResolver = ctx.getContentResolver();
    }

    @Override
    public Twitter.ITweet create()
    {
        throw new UnsupportedOperationException("TimelineContentProviderClient.create");
    }

    @Override
    public boolean add(Twitter.ITweet status)
    {
        ContentValues values = new ContentValues();
        values.put(StatusTable.COLUMN_ID, status.getId());
        values.put(StatusTable.COLUMN_MESSAGE, status.getText());
        values.put(StatusTable.COLUMN_DATE, status.getCreatedAt().getTime());
        values.put(StatusTable.COLUMN_USERNAME, status.getUser().getScreenName());
        _contentResolver.insert(TimelineContentProvider.CONTENT_URI, values);
        return true;
    }

    @Override
    public void addAll(Iterable<Twitter.ITweet> tweets)
    {
        for (Twitter.ITweet t : tweets)
            add(t);
    }

    @Override
    public Iterable<Twitter.ITweet> getAll()
    {
        List<Twitter.ITweet> tweets = new LinkedList<Twitter.ITweet>();
        Cursor cursor = _contentResolver.query(TimelineContentProvider.CONTENT_URI, StatusTable.COLUMN_ALL, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Twitter.ITweet tweet = cursorToTweet(cursor);
            tweets.add(tweet);
            cursor.moveToNext();
        }
        cursor.close();
        return tweets;
    }

    @Override
    public boolean remove(Twitter.ITweet status)
    {

        Uri removeUri = Uri.parse(TimelineContentProvider.CONTENT_URI.getPath() + "/" + status.getId());
        _contentResolver.delete(removeUri, null, null);

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean clearAll()
    {

        _contentResolver.delete(TimelineContentProvider.CONTENT_URI, null, null);

        return true;  //To change body of implemented methods use File | Settings | File Templates.
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
