package pt.isel.adeetc.meic.pdm.common;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 06-06-2012
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class GenericEventReceiver<T> implements IEventReceiver<T>
{
    
    private LinkedList<IEventHandler<T>> _handlers;
    
    
    @Override
    public void invoke(Object sender, IEventHandlerArgs<T> arg) 
    {       
        for (IEventHandler<T> handler : _handlers)
        {
            handler.invoke(sender,arg);
        }
    }
}
