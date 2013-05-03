
package ca.liquidlabs.android.speedtestmapper;

import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutAppActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        // Show the Up button in the action bar.
        setupActionBar();
        
        /* setup the Attribution Text for Google Maps - as per license agreement */
      //  TextView attrText = (TextView) super.findViewById(R.id.txt_about_info);        
      //  attrText.setText();
        
        initListeners();
    }
    
    
    /**
     * Initializes / Attaches Event Listeners to Buttons
     */
    private void initListeners() 
    {
    	Button btn = (Button) super.findViewById(R.id.btn_license_info);    	
    	final AboutAppActivity $this = AboutAppActivity.this;
    	
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new AlertDialog.Builder($this)
			    	.setTitle($this.getString(R.string.lbl_license_info))
			    	.setMessage(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo($this))
			    	.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			    		public void onClick(DialogInterface dialog, int whichButton) {
			    			// Do nothing.
			    		}
			    	}).show();				
			}
		});
    	
    	btn = (Button) super.findViewById(R.id.btn_google_play_apps);
    	btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				String googlePlayLink = $this.getString(R.string.google_play_query);
				$this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(googlePlayLink)));
			}
		});
    	
    	/* On Tap of the TextView, simply redirect user to the Github Repo */
    	TextView txtView = (TextView) super.findViewById(R.id.txt_about_info);
    	txtView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse($this.getString(R.string.github_url)));
				$this.startActivity(browserIntent);				
			}
		});
    	
    	txtView = (TextView) super.findViewById(R.id.txt_author_info);
    	txtView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse($this.getString(R.string.author_url)));
				$this.startActivity(browserIntent);				
			}
		});    	
    }

    @Override
    protected void onPause() {
        // Override the activity transition animation
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onPause();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
