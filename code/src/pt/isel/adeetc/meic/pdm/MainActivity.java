package pt.isel.adeetc.meic.pdm;

import android.os.Bundle;
import pt.isel.adeetc.meic.pdm.common.IntentHelper;
import pt.isel.adeetc.meic.pdm.common.StringHelper;

public class MainActivity extends YambaBaseActivity
{
    private static final String LOG = "MainActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {

        super.onResume();
        if(StringHelper.isNullOrEmpty(getApplicationInstance().getUserName()))
            startActivity(IntentHelper.createIntentToReorderToFrontActivity(this, PrefsActivity.class));

        else
            startActivity(IntentHelper.createIntentToReorderToFrontActivity(this, TimelineActivity.class));
    }

}
