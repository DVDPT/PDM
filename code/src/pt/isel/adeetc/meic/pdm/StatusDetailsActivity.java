package pt.isel.adeetc.meic.pdm;

import android.os.Bundle;
import pt.isel.adeetc.meic.pdm.common.UiHelper;
import winterwell.jtwitter.Twitter;

public class StatusDetailsActivity extends YambaBaseActivity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int objectInstance = getIntent()
                .getExtras()
                .getInt(getApplicationInstance().timelineToStatusDetailsParamName);
        Twitter.Status selectedStatus = (Twitter.Status) getNavigationMessenger().getElement(objectInstance);
        UiHelper.showToast(selectedStatus.getText());

    }
}
