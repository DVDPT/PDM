package pt.isel.adeetc.meic.pdm;

import android.os.Bundle;
import android.widget.TextView;
import winterwell.jtwitter.Twitter;

public class StatusDetailsActivity extends YambaBaseActivity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.status_details);

        int objectInstance = getIntent()
                .getExtras()
                .getInt(getApplicationInstance().timelineToStatusDetailsParamName);

        Twitter.Status selectedStatus = (Twitter.Status) getNavigationMessenger().getElement(objectInstance);

        TextView textRef = (TextView) findViewById(R.id.status_detail_username);
        textRef.setText(selectedStatus.getUser().getName());

        textRef = (TextView) findViewById(R.id.status_detail_message);
        textRef.setText(selectedStatus.getText() + "");

        textRef = (TextView) findViewById(R.id.status_detail_message_id);
        textRef.setText(selectedStatus.getId() + "");

        textRef = (TextView) findViewById(R.id.status_detail_date);
        textRef.setText(selectedStatus.getCreatedAt().toLocaleString());
    }
}
