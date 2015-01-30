package com.xdialer.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

//import com.xdialer.AdSenseUtil;
import com.xdialer.ConfigurateManager;
import com.xdialer.R;

public class MainTabActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		Resources res = getResources(); // Resource object to get Drawables
		ConfigurateManager.setContext(this);
		ConfigurateManager.restorePrefs();
	    
		/** TabHost will have Tabs */
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setBackgroundDrawable(res.getDrawable(R.drawable.bg));
		/**
		 * TabSpec used to create a new tab. By using TabSpec only we can able
		 * to setContent to the tab. By using TabSpec setIndicator() we can set
		 * name to tab.
		 */

		/** tid1 is firstTabSpec Id. Its used to access outside. */
		TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
		TabSpec secondTabSpec = tabHost.newTabSpec("tid1");
		TabSpec thirdTabSpec = tabHost.newTabSpec("tid1");
		TabSpec forthTabSpec = tabHost.newTabSpec("tid1");

		/** TabSpec setIndicator() is used to set name for the tab. */
		/** TabSpec setContent() is used to set content for a particular tab. */
		firstTabSpec.setIndicator(res.getString(R.string.tab_dial), res.getDrawable(R.drawable.tabdialer)).setContent(
				new Intent(this, DialerActivity.class));
		secondTabSpec.setIndicator(res.getString(R.string.tab_phonebook), res.getDrawable(R.drawable.tabcontact)).setContent(
				new Intent(this, PhoneBookActivity.class));
		thirdTabSpec.setIndicator(res.getString(R.string.tab_config), res.getDrawable(R.drawable.tabconfig)).setContent(
				new Intent(this, ConfigureActivity.class));
		forthTabSpec.setIndicator(res.getString(R.string.tab_help), res.getDrawable(R.drawable.tabhelp)).setContent(
				new Intent(this, HelpActivity.class));
		
		/** Add tabSpec to the TabHost to display. */
		tabHost.addTab(firstTabSpec);
		tabHost.addTab(secondTabSpec);
		tabHost.addTab(thirdTabSpec);
		tabHost.addTab(forthTabSpec);
//		if(!ConfigurateManager.skipAds)
//			AdSenseUtil.addAdSense(this, R.id.adViewinconfig);
	}
}
