package pt.isel.adeetc.meic.pdm;

import android.app.ProgressDialog;
import android.os.Bundle;
import pt.isel.adeetc.meic.pdm.common.IntentHelper;
import pt.isel.adeetc.meic.pdm.common.StringHelper;

public class MainActivity extends YambaBaseActivity
{
    private static final String LOG = "MainActivity";
    private ProgressDialog _dialog;

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
        if (StringHelper.isNullOrEmpty(getApplicationInstance().getUserName()))
            startActivity(IntentHelper.createIntentToReorderToFrontActivity(this, PrefsActivity.class));

        else
        {

            _dialog = ProgressDialog.show
                    (
                            this,
                            getString(R.string.loading),
                            getString(/*R.string.main_activity_loading_app*/ R.string.HelloWorldString)
                    );

            getApplicationInstance().getGeneralPurposeHandler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    getApplicationInstance().initialize();
                    getApplicationInstance().getUiHandler().post(_onAppInitialized);
                }
            });
        }

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (_dialog != null)
            _dialog.dismiss();
    }

    private Runnable _onAppInitialized = new Runnable()
    {
        @Override
        public void run()
        {
            _dialog.dismiss();
            startActivity(IntentHelper.createIntentToReorderToFrontActivity(MainActivity.this, StatusActivity.class));
        }
    };

}
