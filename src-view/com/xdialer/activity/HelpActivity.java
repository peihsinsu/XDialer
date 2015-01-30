package com.xdialer.activity;

import com.xdialer.R;
import com.xdialer.R.id;
import com.xdialer.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.widget.TextView;

public class HelpActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		findViews();
	}

	private TextView helpContent ;
	
	private void findViews() {
		helpContent = (TextView) findViewById(R.id.help_content);
		String text = String.valueOf(helpContent.getText());
		text = text.concat("\n\nPhone specific number:");
		text = text.concat("\n*Wait is [" + PhoneNumberUtils.WAIT + "]");
		text = text.concat("\n*Wild is [" + PhoneNumberUtils.WILD + "]");
		text = text.concat("\n*Pause is [" + PhoneNumberUtils.PAUSE + "]");
		System.out.println(">>>>"+text);
		helpContent.setText(text);
	}
}
