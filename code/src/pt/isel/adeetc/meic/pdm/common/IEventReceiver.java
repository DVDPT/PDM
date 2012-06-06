package pt.isel.adeetc.meic.pdm.common;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 06-06-2012
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public interface IEventReceiver<T>
{
    void invoke(Object sender, IEventHandlerArgs<T> arg);
}
