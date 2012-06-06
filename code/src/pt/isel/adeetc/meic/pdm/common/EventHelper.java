package pt.isel.adeetc.meic.pdm.common;

public final class EventHelper
{
    private EventHelper()
    {
    }

    public static <E> void executeAndCallHandlerAsync(final IEvent<E> event, final Func<E> func, final Object sender)
    {
        ThreadPool.QueueUserWorkItem(new Runnable()
        {
            @Override
            public void run()
            {
                executeAndCallHandler(event, func, sender);
            }

        });
    }

    public static <E> void executeAndCallHandler(IEvent<E> event, Func<E> func, Object sender)
    {
        Exception error = null;
        E obj = null;
        try
        {
            obj = func.perform();
        } catch (Exception e)
        {
            error = e;
        }

        event.invoke(sender, new GenericEventArgs<E>(obj, error));
    }
}
