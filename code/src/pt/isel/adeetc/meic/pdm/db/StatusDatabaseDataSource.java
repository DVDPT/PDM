package pt.isel.adeetc.meic.pdm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import pt.isel.adeetc.meic.pdm.common.db.IDataSource;
import pt.isel.adeetc.meic.pdm.common.db.IDbSet;
import winterwell.jtwitter.Twitter;

import java.util.LinkedList;

public class StatusDatabaseDataSource extends LinkedList<Twitter.Status> implements IDbSet<Twitter.Status>, IDataSource
{
    private final Context _ctx;
    private final StatusDatabaseHelper _statusHelper;
    private SQLiteDatabase _database;

    public StatusDatabaseDataSource(Context ctx)
    {
        _ctx = ctx;
        _statusHelper = new StatusDatabaseHelper(ctx);
    }

    @Override
    public void open()
    {
        _database = _statusHelper.getWritableDatabase();
    }

    @Override
    public void close()
    {
        _database.close();
        _statusHelper.close();
    }

    @Override
    public Twitter.Status create()
    {
        return null;
    }

    @Override
    public boolean add(Twitter.Status status)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean remote(Twitter.Status status)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean clearAll()
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
