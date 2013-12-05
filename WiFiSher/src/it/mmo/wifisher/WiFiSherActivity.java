package it.mmo.wifisher;


import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class WiFiSherActivity extends Activity   {
	/** Called when the activity is first created. */
	private AQuery a = new AQuery(this);
	private WiFiSherActivity that = this;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		a.id(R.id.buttonScan).clicked( new OnClickListener() {

            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(that, WiFiShareService.class);
            	startService(intent);
            }
		});

	}
}