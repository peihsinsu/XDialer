package com.xdialer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.xdialer.R;

public class SwitchServiceActivity extends Activity {
	ImageButton widgetButton ;
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		widgetButton = (ImageButton) findViewById(R.id.WidgeImageButton01);
		if(widgetButton != null)
			widgetButton.setImageResource(R.drawable.iconred);
		else
			Toast.makeText(this.getApplicationContext(), "Cannot find Widget Buggon", Toast.LENGTH_SHORT);
	}
}
