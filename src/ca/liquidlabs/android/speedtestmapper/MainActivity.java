
package ca.liquidlabs.android.speedtestmapper;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ca.liquidlabs.android.speedtestmapper.InputDialogFragment.InputDialogListener;
import ca.liquidlabs.android.speedtestmapper.model.SpeedTestRecord;
import ca.liquidlabs.android.speedtestmapper.util.CsvDataParser;
import ca.liquidlabs.android.speedtestmapper.util.Tracer;

import java.util.List;

/**
 * Main entry point launcher activity. Data is loaded here and verified before
 * loading maps view.
 * 
 * 
 */
public class MainActivity extends Activity implements InputDialogListener {

    private static final String testData = "";

    public static List<SpeedTestRecord> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tracer.println("onCreate");

        // Get intent, action and MIME type
        // More info/guide: http://developer.android.com/training/sharing/receive.html
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Tracer.println("onStart");

        

    }
    
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Tracer.Toast(this, "Got data, length : " + sharedText.length());
            mListData = CsvDataParser.parseCsvData(sharedText);
        } else {
            mListData = CsvDataParser.parseCsvData(testData);
        }
    }

    private void showInputDialog() {
        FragmentManager fm = getFragmentManager();
        InputDialogFragment editNameDialog = InputDialogFragment.newInstance();
        editNameDialog.show(fm, "fragment_input_data");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_paste_data:
                showInputDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onFinishEditDialog(String inputText) {
        Tracer.println(inputText);
        Tracer.Toast(this, inputText);

        // Test data ready - go to maps view
        Intent intent = new Intent(this, MapperActivity.class);
        startActivity(intent);
    }

}
