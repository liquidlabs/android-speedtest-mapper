
package ca.liquidlabs.android.speedtestmapper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import ca.liquidlabs.android.speedtestmapper.util.CsvDataParser;
import ca.liquidlabs.android.speedtestmapper.util.Tracer;

/**
 * Main entry point launcher activity. Data is loaded here and verified before
 * loading maps view.
 */
public class MainActivity extends Activity {

    
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
