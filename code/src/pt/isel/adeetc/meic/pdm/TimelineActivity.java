package pt.isel.adeetc.meic.pdm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import pt.isel.adeetc.meic.pdm.common.*;
import pt.isel.adeetc.meic.pdm.common.holders.ViewHolder3;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;
import winterwell.jtwitter.Twitter;

import java.util.Date;
import java.util.LinkedList;

public class TimelineActivity extends YambaBaseActivity implements IEventHandler<Iterable<Twitter.Status>>, AdapterView.OnItemClickListener
{
    private TwitterServiceClient _twitter;
    private final LinkedList<Twitter.Status> _status = new LinkedList<Twitter.Status>();
    private ArrayAdapter<Twitter.Status> _listAdapter;
    private static final String LOG = "TimelineActivity";
    private ProgressDialog _loadingDialog;

    public TimelineActivity()
    {
        super(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        ListView list = (ListView) findViewById(R.id.timeline_user_status);
        _listAdapter = new TwitterStatusAdpater();
        list.setOnItemClickListener(this);
        list.setAdapter(_listAdapter);

        _twitter = getApplicationInstance().getTwitterClient();
        _twitter.getUserTimelineCompletedEvent.setEventHandler(this);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        onNavigatedTo();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        onNavigatedTo();
    }

    private void onNavigatedTo()
    {

        if (_status.size() != 0)
            return;

        getUserTimeline();
    }

    private void getUserTimeline()
    {
        if(_twitter.isTimelineBeingFetched())
            return;

        _status.clear();
        if (_loadingDialog == null)
        {
            _loadingDialog = ProgressDialog.show
                    (
                            this,
                            getString(R.string.loading),
                            getString(R.string.tineline_loading_tweets)
                    );
        } else
            _loadingDialog.show();

        _twitter.getUserTimelineAsync();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        _twitter.getUserTimelineCompletedEvent.removeEventHandler();
    }

    @Override
    protected Iterable<Integer> getActivityDisabledMenuItems()
    {
        LinkedList<Integer> ret = new LinkedList<Integer>();
        ret.add(R.id.menu_timeline);
        return ret;
    }

    public void invoke(Object sender, IEventHandlerArgs<Iterable<Twitter.Status>> userStatus)
    {
        _loadingDialog.dismiss();
        Log.d(LOG, String.format("On async task event handler."));

        if (userStatus.errorOccurred())
        {
            UiHelper.showToast(R.string.timeline_error_fetching_status);
            Log.w(LOG, userStatus.getError());
            return;
        }

        try
        {
            for (Twitter.Status status : userStatus.getData())
            {
                _listAdapter.add(status);
            }

            if (_status.size() == 0)
                UiHelper.showToast(R.string.timeline_display_no_status);

        } catch (Exception e)
        {
            throw new ShouldNotHappenException(e);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        int objId = getNavigationMessenger().putElement(_status.get(i));

        startActivity(
                new Intent(this, StatusDetailsActivity.class)
                        .putExtra(getApplicationInstance().timelineToStatusDetailsParamName, objId)
        );
    }

    private class TwitterStatusAdpater extends ArrayAdapter<Twitter.Status>
    {

        public TwitterStatusAdpater()
        {

            super(TimelineActivity.this, 0, _status);

        }

        @SuppressWarnings("unchecked")
        public View getView(int pos, View cView, ViewGroup parent)
        {
            View v = cView;
            ViewHolder3<TextView, TextView, TextView> holder;
            if (v == null)
            {
                v = getLayoutInflater().inflate(R.layout.timeline_element, null);
                holder = new ViewHolder3<TextView, TextView, TextView>
                        (
                                (TextView) v.findViewById(R.id.timeline_status_message),
                                (TextView) v.findViewById(R.id.timeline_status_username),
                                (TextView) v.findViewById(R.id.timeline_status_date)
                        );
                v.setTag(holder);
            } else
                holder = (ViewHolder3<TextView, TextView, TextView>) v.getTag();

            holder.getT1().setText(_status.get(pos).getText());
            holder.getT2().setText(_status.get(pos).getUser().getName());
            holder.getT3().setText(DateHelper.stringifyDifference(new Date(System.currentTimeMillis()), _status.get(pos).getCreatedAt()));
            return v;
        }


    }
}
