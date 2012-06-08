package pt.isel.adeetc.meic.pdm.common;


public interface IEventReceives<T>
{
    void invoke(Object sender, IEventHandlerArgs<T> arg);
    void removeEventHandler(IEventHandler<T> event);
    void addEventHandler(IEventHandler<T> event);
    void onEventHandlerChanged(IEventHandler<IEventHandler<T>> event);
}
