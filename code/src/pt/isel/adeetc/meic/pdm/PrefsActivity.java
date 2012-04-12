package pt.isel.adeetc.meic.pdm;

import android.content.Intent;
import android.os.Bundle;
import android.preference.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 10-04-2012
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class PrefsActivity extends PreferenceActivity{

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onStop(){
        super.onStop();
        Intent statusActivity = new Intent(this,
                StatusActivity.class);
        startActivity(statusActivity);
    }
}
