package pt.isel.adeetc.meic.pdm.common;

public interface IEvent<T>
{
    void invoke(Object sender, IEventHandlerArgs<T> arg);
    void setEventHandler(IEventHandler<T> event);
    IEventHandler<T> removeEventHandler();
    void onEventHandlerChanged(IEventHandler<IEventHandler<T>> event);
}
