package pt.isel.adeetc.meic.pdm.db;

import android.content.Context;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import winterwell.jtwitter.Twitter;

import java.util.Comparator;
import java.util.Date;

public class YambaStatusDatabase extends StatusDatabaseDataSource
{
    public Date _lastSavedTweetDate;

    public YambaStatusDatabase(Context ctx)
    {
        super(ctx);
    }

    private Twitter.ITweet getLastTweetInserted()
    {
        int count = Iterables.size(this);
        if (count == 0)
            return null;
        if (count == 1)
            return Iterables.getFirst(this, null);

        return Ordering.from(new Comparator<Twitter.ITweet>()
        {
            @Override
            public int compare(Twitter.ITweet o, Twitter.ITweet o1)
            {
                return (int) (o.getCreatedAt().getTime() - o.getCreatedAt().getTime());
            }
        }).max(this);
    }

    @Override
    public void open()
    {
        super.open();
        Twitter.ITweet lastTweetInserted = getLastTweetInserted();

        if (lastTweetInserted == null)
            _lastSavedTweetDate = new Date(0);
        else
            _lastSavedTweetDate = lastTweetInserted.getCreatedAt();
    }

    @Override
    public boolean add(Twitter.ITweet t)
    {
        return t.getCreatedAt().compareTo(_lastSavedTweetDate) > 0 && super.add(t);
    }


    @Override
    public boolean remove(Twitter.ITweet t)
    {
        boolean res = super.remove(t);
        if (res && t.getCreatedAt().compareTo(_lastSavedTweetDate) == 0)
        {
            _lastSavedTweetDate = getLastTweetInserted().getCreatedAt();
        }
        return res;
    }

    @Override
    public boolean clearAll()
    {
        boolean res = super.clearAll();

        if (res)
            _lastSavedTweetDate = new Date(0);

        return res;
    }

}
