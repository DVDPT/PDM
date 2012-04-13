package pt.isel.adeetc.meic.pdm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import pt.isel.adeetc.meic.pdm.common.IntentHelper;

public class MainActivity extends Activity
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
    }

    @Override
    public void onResume()
    {
        super.onResume();
        startActivity(IntentHelper.createIntentToReorderToFrontActivity(this, TimelineActivity.class));
    }

}
