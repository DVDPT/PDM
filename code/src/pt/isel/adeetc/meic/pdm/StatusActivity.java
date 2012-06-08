package pt.isel.adeetc.meic.pdm;


import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import pt.isel.adeetc.meic.pdm.common.IEventHandler;
import pt.isel.adeetc.meic.pdm.common.IEventHandlerArgs;
import pt.isel.adeetc.meic.pdm.common.UiHelper;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;
import pt.isel.adeetc.meic.pdm.services.TwitterServiceClient;
import winterwell.jtwitter.Twitter;

import java.util.LinkedList;

public class StatusActivity extends YambaBaseActivity implements IEventHandler<Integer>,TextWatcher, View.OnClickListener {

    private EditText _status;
    private Button _update;
    private TextView _count;
    private TwitterServiceClient _twitter;
    private String _maxCharacters;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

        _status = (EditText) findViewById(R.id.editText);
        _update = (Button) findViewById(R.id.buttonUpdate);
        _count = (TextView) findViewById(R.id.Count);

        _status.addTextChangedListener(this);

        _update.setOnClickListener(this);
        _twitter = getApplicationInstance().getTwitterClient();
        _twitter.updateStatusCompletedEvent.setEventHandler(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        _maxCharacters = getApplicationInstance().getMaxCharacter();
        _count.setText( _maxCharacters);
        _status.setFilters(new InputFilter[] { new InputFilter.LengthFilter(new Integer(_maxCharacters)) });

    }

    @Override
    protected Iterable<Integer> getActivityDisabledMenuItems()
    {
        LinkedList<Integer> ret =  new LinkedList<Integer>();
        ret.add(R.id.menu_status);
        return ret;
    }


    @Override
    public void invoke(Object sender, IEventHandlerArgs<Integer> statusIEventHandlerArgs)
    {
        try {
            UiHelper.showToast(statusIEventHandlerArgs.getData());
        } catch (Exception e) {
            throw new ShouldNotHappenException(e);
        }
        _status.setText("");
        _count.setText(_maxCharacters);
        _update.setEnabled(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        _twitter.updateStatusCompletedEvent.removeEventHandler();
    }

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
        Integer currentLength = new Integer(_maxCharacters) - editable.length();
        _count.setText(currentLength.toString());
    }

    @Override
    public void onClick(View view) {
        String newStatus = _status.getText().toString();
        _update.setEnabled(false);
        _twitter.updateStatusAsync(newStatus);
    }
}