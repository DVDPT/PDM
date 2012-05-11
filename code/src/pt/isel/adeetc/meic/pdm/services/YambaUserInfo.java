package pt.isel.adeetc.meic.pdm.services;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import winterwell.jtwitter.Twitter;

public class YambaUserInfo implements Parcelable
{
    private final String _name;
    private final int _nrOfTweets;
    private final int _followers;
    private final int _following;
    private final Uri _photoUri;

    public String getName()
    {
        return _name;
    }

    public int getNrOfTweets()
    {
        return _nrOfTweets;
    }

    public int getFollowers()
    {
        return _followers;
    }

    public int getFollowing()
    {
        return _following;
    }

    public Uri getPhotoUri()
    {
        return _photoUri;
    }

    public YambaUserInfo(Twitter.User user)
    {
        this._name = user.getName();
        this._nrOfTweets = user.getStatusesCount();
        this._followers = user.getFollowersCount();
        this._following = user.getFriendsCount();
        this._photoUri = Uri.parse(user.getProfileImageUrl().getPath());
    }

    public YambaUserInfo(Parcel parcel)
    {
        _name = parcel.readString();
        _nrOfTweets = parcel.readInt();
        _followers = parcel.readInt();
        _following = parcel.readInt();
        _photoUri = Uri.parse(parcel.readString());
    }




    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(_name);
        parcel.writeInt(_nrOfTweets);
        parcel.writeInt(_followers);
        parcel.writeInt(_following);
        parcel.writeString(_photoUri.getPath());
    }

    public static final Parcelable.Creator<YambaUserInfo> CREATOR
            = new Parcelable.Creator<YambaUserInfo>()
    {
        public YambaUserInfo createFromParcel(Parcel in)
        {
            return new YambaUserInfo(in);
        }

        public YambaUserInfo[] newArray(int size)
        {
            return new YambaUserInfo[size];
        }
    };


}