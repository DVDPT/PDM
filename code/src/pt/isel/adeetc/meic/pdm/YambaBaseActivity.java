package pt.isel.adeetc.meic.pdm;

import android.view.Menu;
import android.view.MenuItem;
import pt.isel.adeetc.meic.pdm.extensions.BaseActivity;

import java.util.LinkedList;

public class YambaBaseActivity extends BaseActivity<YambaApplication>
{

    private YambaAppMenuResolver _menuResolver = YambaAppMenuResolver.getInstance();

    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {

        getMenuInflater().inflate(R.menu.app_menu, m);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        _menuResolver.prepareOptionMenu(menu, getActivityDisabledMenuItems());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return _menuResolver.onMenuItemSelected(this, item) || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    protected Iterable<Integer> getActivityDisabledMenuItems()
    {
        return new LinkedList<Integer>();
    }


}
