package pt.isel.adeetc.meic.pdm.services;

import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import winterwell.jtwitter.Twitter;

public class TimelinePullServiceMessage implements IServiceMessage<Void,Iterable<Twitter.ITweet>>
{
    private final IEventHandler<Iterable<Twitter.ITweet>> _action;

    @Override
    public Void getData()
    {
        return null;
    }

    public TimelinePullServiceMessage(IEventHandler<Iterable<Twitter.ITweet>> action)
    {
       _action = action;
    }

    @Override
    public IEventHandler<Iterable<Twitter.ITweet>> getCallback()
    {
        return _action;
    }
}
