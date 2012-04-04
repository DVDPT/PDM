package pt.isel.adeetc.meic.pdm.common;

public interface IEventHandlerArgs<T>
{
    Exception getError();
    T getData() throws Exception;
    boolean errorOccurred();


}
