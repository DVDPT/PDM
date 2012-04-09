package pt.isel.adeetc.meic.pdm;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 04-04-2012
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class StatusActivity extends Activity {

    private EditText _status;
    
    private Button _update;

    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _status = (EditText) findViewById(R.id.editText);
        _update = (Button) findViewById(R.id.buttonUpdate);

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

            }
        });

    }

    public final void runOnUIThread(Runnable r)
    {
         r.run();
    }







}