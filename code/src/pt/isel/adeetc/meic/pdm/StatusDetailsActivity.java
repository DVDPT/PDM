package pt.isel.adeetc.meic.pdm;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import winterwell.jtwitter.Twitter;

public class StatusDetailsActivity extends YambaBaseActivity implements View.OnClickListener
{
    private Button _sendEmailButton;
    private Twitter.ITweet _status;
    private int _objId;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.status_details);
        _sendEmailButton = (Button) findViewById(R.id.status_detail_button);

        _sendEmailButton.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (_status != null)
            return;

        _objId = getIntent()
                .getExtras()
                .getInt(YambaNavigation.TIMELINE_TO_STATUS_DETAILS_PARAM_NAME);

        _status = (Twitter.ITweet) getNavigationMessenger().getElementAndPreserve(_objId);

        TextView textRef = (TextView) findViewById(R.id.status_detail_username);
        textRef.setText(_status.getUser().getName());

        textRef = (TextView) findViewById(R.id.status_detail_message);
        textRef.setText(_status.getText() + "");

        textRef = (TextView) findViewById(R.id.status_detail_message_id);
        textRef.setText(_status.getId() + "");

        textRef = (TextView) findViewById(R.id.status_detail_date);
        textRef.setText(_status.getCreatedAt().toLocaleString());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return false;
    }

    @Override
    public void onClick(View view)
    {
        getApplicationInstance()
                .getEmailSender()
                .sendEmail
                        (
                                null,
                                "Timeline Message",
                                String.format("%s\n%s\n%s",
                                        _status.getUser().getScreenName(),
                                        _status.getText(),
                                        _status.getCreatedAt().toLocaleString()
                                )
                        );
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        getNavigationMessenger().remove(_objId);
    }
}
