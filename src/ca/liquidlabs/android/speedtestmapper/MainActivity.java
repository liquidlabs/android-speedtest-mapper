
package ca.liquidlabs.android.speedtestmapper;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ca.liquidlabs.android.speedtestmapper.InputDialogFragment.InputDialogListener;
import ca.liquidlabs.android.speedtestmapper.util.CsvDataParser;
import ca.liquidlabs.android.speedtestmapper.util.Tracer;

/**
 * Main entry point launcher activity. Data is loaded here and verified before
 * loading maps view.
 */
public class MainActivity extends Activity implements InputDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tracer.println("onCreate");
        
    }

    @Override
    protected void onStart() {
        super.onStart();

        Tracer.println("onStart");

        Tracer.println("" + CsvDataParser.parseCsvData(""));
    }

    private void showInputDialog() {
        FragmentManager fm = getFragmentManager();
        InputDialogFragment editNameDialog = InputDialogFragment.newInstance();
        editNameDialog.show(fm, "fragment_edit_name");
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

    }

}
