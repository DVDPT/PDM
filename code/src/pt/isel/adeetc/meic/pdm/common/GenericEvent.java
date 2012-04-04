package pt.isel.adeetc.meic.pdm.common;

public class GenericEvent<T> implements IEvent<T>
{
    private IEventHandler<T> _handler;

    public GenericEvent(IEventHandler<T> handler)
    {
        _handler = handler;
    }

    public GenericEvent()
    {
    }

    public void invoke(Object sender, IEventHandlerArgs<T> arg)
    {
        if(_handler != null)
            _handler.invoke(sender,arg);
    }

    public void setEventHandler(IEventHandler<T> handler)
    {
        _handler = handler;
    }

    public IEventHandler<T> removeEventHandler()
    {
        IEventHandler<T> hand = _handler;
        _handler = null;
        return hand;
    }
}
