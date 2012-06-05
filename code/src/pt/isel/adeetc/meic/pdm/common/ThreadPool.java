package pt.isel.adeetc.meic.pdm.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadPool
{
    private static ExecutorService _pool = Executors.newFixedThreadPool(10);

    private ThreadPool()
    {}

    public static void QueueUserWorkItem(Runnable work)
    {
        _pool.execute(work);
    }
}
