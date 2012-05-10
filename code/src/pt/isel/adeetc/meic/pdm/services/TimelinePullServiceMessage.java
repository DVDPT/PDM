package pt.isel.adeetc.meic.pdm.services;

import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import winterwell.jtwitter.Twitter;

public class TimelinePullServiceMessage implements IServiceMessage<Void,Iterable<Twitter.Status>>
{
    private final IEventHandler<Iterable<Twitter.Status>> _action;

    @Override
    public Void getData()
    {
        return null;
    }

    public TimelinePullServiceMessage(IEventHandler<Iterable<Twitter.Status>> action)
    {
       _action = action;
    }

    @Override
    public IEventHandler<Iterable<Twitter.Status>> getCallback()
    {
        return _action;
    }
}
