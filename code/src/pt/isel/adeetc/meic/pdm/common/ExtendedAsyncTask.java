package pt.isel.adeetc.meic.pdm.common;

import android.os.AsyncTask;


public abstract class ExtendedAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
    private Exception _error;

    public Exception getError()
    {
        return _error;
    }

    @Override
    protected final Result doInBackground(Params... paramses)
    {
        Result res;
        try
        {
           return doWork(paramses);
        } catch (Exception e)
        {
            _error = e;
        }
        return null;
    }

    protected abstract Result doWork(Params... params);
}
