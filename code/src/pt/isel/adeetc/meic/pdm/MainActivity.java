package pt.isel.adeetc.meic.pdm;

import android.content.Intent;
import android.os.Bundle;

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
        setContentView(R.layout.status);
        startActivity(new Intent(this,TimelineActivity.class));

    }

}
