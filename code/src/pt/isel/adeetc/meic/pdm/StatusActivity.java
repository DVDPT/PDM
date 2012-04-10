package pt.isel.adeetc.meic.pdm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.common.UiHelper;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;
import winterwell.jtwitter.Twitter;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 04-04-2012
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class StatusActivity extends YambaBaseActivity implements IEventHandler<Twitter.Status> {

    private final int MAXLENGTHTWEET = 142;

    private EditText _status;
    private Button _update;
    private TextView _count;
    private TwitterServiceClient _twitter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        _status = (EditText) findViewById(R.id.editText);
        _update = (Button) findViewById(R.id.buttonUpdate);
        _count = (TextView) findViewById(R.id.Count);
        _status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void afterTextChanged(Editable editable) {

                Integer currentLength = MAXLENGTHTWEET - editable.length();
                _count.setText(currentLength.toString());
            }
        });

        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newStatus = _status.getText().toString();
                _update.setEnabled(false);
                _twitter.updateStatusAsync(newStatus);
            }
        });

        _twitter = getApplicationInstance().getTwitterClient();
        _twitter.updateStatusCompletedEvent.setEventHandler(this);
    }


    @Override
    public void invoke(Object sender, IEventHandlerArgs<Twitter.Status> statusIEventHandlerArgs) {

        if(statusIEventHandlerArgs.errorOccurred())
        {
            UiHelper.showToast(R.string.status_error_insert_newStatus);
            return;
        }
        _status.setText("");
        _count.setText(R.string.MaxCount);
        _update.setEnabled(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        _twitter.updateStatusCompletedEvent.removeEventHandler();
    }
}