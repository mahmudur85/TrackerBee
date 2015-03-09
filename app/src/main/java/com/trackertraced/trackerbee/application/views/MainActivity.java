package com.trackertraced.trackerbee.application.views;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.trackertraced.trackerbee.R;
import com.trackertraced.trackerbee.application.manager.ServiceMessengerManager;
import com.trackertraced.trackerbee.application.manager.managerImpl.ServiceMessengerManagerImpl;
import com.trackertraced.trackerbee.application.service.TrackerBeeService;
import com.trackertraced.trackerbee.application.utils.LogHelper;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private Intent intentTrackerBeeService;

	TrackerBeeService trackerBeeService;
	private long period = 10;
	private TimeUnit unit = TimeUnit.SECONDS;
	private float minumumDistance = 20.0f;
	private String USER_ID = "topu";
    private ServiceMessengerManager serviceMessengerManager = new ServiceMessengerManagerImpl();

	EditText period_edittxt;
	EditText minimumDistance_edittxt;
	RadioGroup radioGroup;
	RadioButton sec_RadioButton;
	RadioButton min_RadioButton;
	Button update_btn;

    LogHelper logHelper = new LogHelper(LogHelper.LogTags.KMR,MainActivity.class.getSimpleName(),true);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		period_edittxt = (EditText) findViewById(R.id.period_edittxt);
		minimumDistance_edittxt = (EditText) findViewById(R.id.minimumdistance_edittxt);
		radioGroup = (RadioGroup) findViewById(R.id.timeunit_radiogroup);
		sec_RadioButton = (RadioButton) findViewById(R.id.second_radioButton);
		min_RadioButton = (RadioButton) findViewById(R.id.minuts_radioButton);
		update_btn = (Button) findViewById(R.id.update);
		period_edittxt.setText(String.valueOf(period));
		minimumDistance_edittxt.setText(String.valueOf(minumumDistance));
		intentTrackerBeeService = new Intent(this, TrackerBeeService.class);
		trackerBeeService = new TrackerBeeService();
		TrackerBeeService.setUSER_ID(USER_ID);
		startService(intentTrackerBeeService);
        bindService(intentTrackerBeeService,serviceMessengerManager.getServiceConnection(), Context.BIND_AUTO_CREATE);
		update_btn.setOnClickListener(updateClick);
        logHelper.d("onCreate");
	}

	OnClickListener updateClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (period_edittxt.getText().toString() != ""
					&& minimumDistance_edittxt.getText().toString() != "") {
//				if (sec_RadioButton.isChecked()) {
//					trackerBeeService.updateLocationScheduledTaskExecutor(Long
//							.valueOf(period_edittxt.getText().toString()),
//							TimeUnit.SECONDS, Float
//									.valueOf(minimumDistance_edittxt.getText()
//											.toString()));
//				}
//				if (min_RadioButton.isChecked()) {
//					trackerBeeService.updateLocationScheduledTaskExecutor(Long
//							.valueOf(period_edittxt.getText().toString()),
//							TimeUnit.MINUTES, Float
//									.valueOf(minimumDistance_edittxt.getText()
//											.toString()));
//				}
				
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        unbindService(serviceMessengerManager.getServiceConnection());
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
