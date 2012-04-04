package pt.isel.adeetc.meic.pdm.common;

public interface IEventHandler<T>
{
    void invoke(Object sender, IEventHandlerArgs<T> args);
}
