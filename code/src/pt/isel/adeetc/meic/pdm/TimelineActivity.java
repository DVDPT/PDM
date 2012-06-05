package pt.isel.adeetc.meic.pdm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class TimelineActivity extends YambaBaseActivity implements IEventHandler<Iterable<Twitter.ITweet>>, AdapterView.OnItemClickListener
{

    private final LinkedList<Twitter.ITweet> _status = new LinkedList<Twitter.ITweet>();
    private ArrayAdapter<Twitter.ITweet> _listAdapter;
    private static final String LOG = "TimelineActivity";
    private ProgressDialog _loadingDialog;


    private TwitterServiceClient getTwitter()
    {
        return getApplicationInstance().getTwitterClient();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        ListView list = (ListView) findViewById(R.id.timeline_user_status);
        _listAdapter = new TwitterStatusAdapter();
        list.setOnItemClickListener(this);
        list.setAdapter(_listAdapter);



    }

    @Override
    protected void onPause()
    {
        super.onPause();
        _loadingDialog.dismiss();
        getTwitter().getUserTimelineCompletedEvent.removeEventHandler();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
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

        getUserTimeline(true);
    }

    private void getUserTimeline(boolean isResuming)
    {
        Iterable<Twitter.ITweet> data = getTwitter().getTwitterCachedTimeline();
        getTwitter().getUserTimelineCompletedEvent.setEventHandler(this);
        if (data != null && isResuming)
        {
            setTimelineOnUi(data);
            return;
        }

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

        getTwitter().getUserTimelineAsync();
    }


    public void invoke(Object sender, IEventHandlerArgs<Iterable<Twitter.ITweet>> userStatus)
    {

        Log.d(LOG, String.format("On event task event handler."));
        _loadingDialog.dismiss();



        if (userStatus.errorOccurred())
        {
            UiHelper.showToast(R.string.timeline_error_fetching_status);
            Log.w(LOG, userStatus.getError());
            return;
        }

        try
        {
            setTimelineOnUi(userStatus.getData());
        } catch (Exception e)
        {
            throw new ShouldNotHappenException(e);
        }
    }

    private void setTimelineOnUi(Iterable<? extends Twitter.ITweet> statusCollection)
    {
        _status.clear();
        int i = 0;
        final int maxAllowed = getApplicationInstance().getMaxTweets();
        for (Twitter.ITweet status : statusCollection)
        {
            _listAdapter.add(status);

            if (++i == maxAllowed)
                break;
        }

        if (_status.size() == 0)
            UiHelper.showToast(R.string.timeline_display_no_status);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        int objId = getNavigationMessenger().putElement(_status.get(i));

        startActivity(
                new Intent(this, StatusDetailsActivity.class)
                        .putExtra(YambaNavigation.timelineToStatusDetailsParamName, objId)
        );
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_refresh).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_refresh)
        {
            getUserTimeline(false);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected Iterable<Integer> getActivityDisabledMenuItems()
    {
        LinkedList<Integer> ret = new LinkedList<Integer>();
        ret.add(R.id.menu_timeline);
        return ret;
    }

    private class TwitterStatusAdapter extends ArrayAdapter<Twitter.ITweet>
    {

        public TwitterStatusAdapter()
        {

            super(TimelineActivity.this, 0, _status);

        }

        @SuppressWarnings("unchecked")
        public View getView(int pos, View cView, ViewGroup parent)
        {
            Log.d(LOG, "On TwitterStatusAdapter drawing some element.");
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

            holder.getT1().setText(getSubStringOrFull(_status.get(pos).getText(), getApplicationInstance().getStatusMaxCharactersShowedInTimeline()));
            holder.getT2().setText(_status.get(pos).getUser().getName());
            holder.getT3().setText(DateHelper.stringifyDifference(new Date(System.currentTimeMillis()), _status.get(pos).getCreatedAt()));
            return v;
        }

        private String getSubStringOrFull(String text, int maxCharacters)
        {
            return text.length() > maxCharacters ? text.substring(0, maxCharacters) : text;
        }


    }
}
