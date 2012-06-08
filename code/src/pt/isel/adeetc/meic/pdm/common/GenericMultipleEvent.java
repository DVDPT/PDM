package pt.isel.adeetc.meic.pdm.common;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 07-06-2012
 * Time: 1:17
 * To change this template use File | Settings | File Templates.
 */
public class GenericMultipleEvent<T> implements IMultipleEvent<T>{

    private LinkedList<IEventHandler<T>> _handlers;

    public GenericMultipleEvent(){
        _handlers= new LinkedList<IEventHandler<T>>();
    }

    @Override
    public void invoke(Object sender, IEventHandlerArgs<T> arg)
    {
          for(IEventHandler ev : _handlers)
          {
              ev.invoke(sender,arg);
          }
    }

    @Override
    public void removeEventHandler(IEventHandler<T> event)
    {
        if(_handlers.contains(event))
            _handlers.remove(event);
    }

    @Override
    public void addEventHandler(IEventHandler<T> event)
    {
        if(!_handlers.contains(event))
            _handlers.add(event);
    }
}
