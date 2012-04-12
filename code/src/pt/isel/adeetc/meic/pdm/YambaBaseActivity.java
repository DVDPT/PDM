package pt.isel.adeetc.meic.pdm;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import pt.isel.adeetc.meic.pdm.common.IntentHelper;
import pt.isel.adeetc.meic.pdm.extensions.BaseActivity;

import java.util.Collections;

public class YambaBaseActivity extends BaseActivity<YambaApplication>
{
    private static String LOG = "YambdaBaseActivity";

    private final boolean _isMenuVisible;

    public YambaBaseActivity(boolean showAppMenu)
    {
        _isMenuVisible = showAppMenu;
    }

    public YambaBaseActivity()
    {
        this(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        if(_isMenuVisible)
        {
            getMenuInflater().inflate(R.menu.app_menu, m);
        }
        return _isMenuVisible;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem m;
        for (Integer itemId : getActivityDisabledMenuItems())
        {

            m = menu.findItem(itemId);
            if (m == null)
            {
                Log.w(LOG, "getActivityDisabledMenuItems returning menu items that don't exist");
            } else
            {
                m.setEnabled(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_timeline:
                startActivity(IntentHelper.createIntentToReorderToFrontActivity(this, TimelineActivity.class));
                return true;
            case R.id.menu_status:
                startActivity(IntentHelper.createIntentToReorderToFrontActivity(this, StatusActivity.class));
                return true;
            case R.id.menu_prefs:
                startActivity(IntentHelper.createIntentToReorderToFrontActivity(this, PrefsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    protected Iterable<Integer> getActivityDisabledMenuItems()
    {
        return Collections.EMPTY_LIST;
    }



}
