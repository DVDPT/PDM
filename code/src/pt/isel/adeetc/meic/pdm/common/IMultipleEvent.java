package pt.isel.adeetc.meic.pdm.common;


public interface IMultipleEvent<T>
{
    void invoke(Object sender, IEventHandlerArgs<T> arg);
    void removeEventHandler(IEventHandler<T> event);
    void addEventHandler(IEventHandler<T> event);
}
