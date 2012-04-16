package pt.isel.adeetc.meic.pdm;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import pt.isel.adeetc.meic.pdm.common.IntentHelper;

public final class YambaAppMenuResolver
{

    private static String LOG = "YambaAppMenuResolver";

    private YambaAppMenuResolver()
    {

    }

    private static YambaAppMenuResolver _resolver = new YambaAppMenuResolver();

    public static YambaAppMenuResolver getInstance()
    {
        return _resolver;
    }

    public void createMenu(Activity a, Menu m)
    {
        a.getMenuInflater().inflate(R.menu.app_menu, m);
    }

    public void prepareOptionMenu(Menu menu, Iterable<Integer> disabledMenuItems)
    {
        MenuItem m;
        for (Integer itemId : disabledMenuItems)
        {

            m = menu.findItem(itemId);
            if (m == null)
            {
                Log.w(LOG, "prepareOptionMenu returning menu items that don't exist");
            } else
            {
                m.setEnabled(false);
            }
        }
    }

    public boolean onMenuItemSelected(Activity a, MenuItem item)
    {

        /*
      switch (item.getItemId())
      {
          case R.id.menu_timeline:
              a.startActivity(IntentHelper.createIntentToReorderToFrontActivity(a, TimelineActivity.class));
              return true;
          case R.id.menu_status:
              a.startActivity(IntentHelper.createIntentToReorderToFrontActivity(a, StatusActivity.class));
              return true;
          case R.id.menu_prefs:
              a.startActivity(IntentHelper.createIntentToReorderToFrontActivity(a, PrefsActivity.class));
              return true;
          default:
              return false;
      }
        */
        int id = item.getItemId();
        if (id == R.id.menu_timeline)
            a.startActivity(IntentHelper.createIntentToReorderToFrontActivity(a, TimelineActivity.class));
        else if (id == R.id.menu_status)
            a.startActivity(IntentHelper.createIntentToReorderToFrontActivity(a, StatusActivity.class));
        else if (id == R.id.menu_prefs)
            a.startActivity(IntentHelper.createIntentToReorderToFrontActivity(a, PrefsActivity.class));
        else
            return false;
        return true;

    }

}
