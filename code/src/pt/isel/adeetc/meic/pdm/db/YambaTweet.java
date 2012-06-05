package pt.isel.adeetc.meic.pdm.db;

import winterwell.jtwitter.Twitter;

import java.util.Date;

public class YambaTweet implements Twitter.ITweet
{
    private final long _id;
    private final Date _createdAt;
    private final String _text;
    private final Twitter.User _user;

    public YambaTweet(long id, Date createdAt, String text, String userName)
    {

        this._id = id;
        this._createdAt = createdAt;
        this._text = text;
        this._user = new Twitter.User(userName);
    }

    @Override
    public Date getCreatedAt()
    {
        return _createdAt;
    }

    @Override
    public long getId()
    {
        return _id;
    }

    @Override
    public String getText()
    {
        return _text;
    }

    @Override
    public Twitter.User getUser()
    {
        return _user;
    }
}
