package pt.isel.adeetc.meic.pdm;

import android.os.Bundle;
import android.util.Log;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import winterwell.jtwitter.Twitter;

public class MainActivity extends YambaBaseActivity implements IEventHandler<Iterable<Twitter.Status>>
{
    private static final String LOG = "MainActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getApplicationInstance().getTwitterClient().getUserTimelineCompletedEvent.setEventHandler(this);
        getApplicationInstance().getTwitterClient().getUserTimelineAsync();
    }

    public void invoke(Object sender, IEventHandlerArgs<Iterable<Twitter.Status>> status)
    {
        try
        {
            for (Twitter.Status s : status.getData())
                Log.d(LOG, s.getText());
        } catch (Exception e)
        {
            Log.d(LOG,e.getMessage());
        }
    }
}
