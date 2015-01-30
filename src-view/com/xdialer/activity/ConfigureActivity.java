package com.xdialer.activity;

import android.app.AlertDialog.Builder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.xdialer.BaseActivity;
import com.xdialer.ConfigurateManager;
import com.xdialer.R;
import com.xdialer.widget.OutgoingCallFilterWidget;

public class ConfigureActivity extends BaseActivity {
	private Button button_setup ;
	private EditText et_block_prefix, et_block_midfix, et_block_lastfix, et_pattern;
	private EditText et_dnStartWith, et_dnEndWith, et_dnInclude;
	private CheckBox serviceSwitch;
	private Spinner spinnerFilterType;
	private ImageButton qes_service, qes_pattern, qes_rule;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure);
		findViews();
		setListeners();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("Resume service...");
		//Restore prefix as set to page
		findViews();
	}
	
	@Override
    protected void onStop(){
       super.onStop();
    }
	
	private void findViews(){
		button_setup = (Button)findViewById(R.id.help_setup_prefix);
		et_block_prefix = (EditText)findViewById(R.id.block_prefix);
		et_block_midfix = (EditText)findViewById(R.id.block_midfix);
		et_block_lastfix = (EditText)findViewById(R.id.block_postfix);
		et_pattern = (EditText)findViewById(R.id.pattern);

		et_dnStartWith = (EditText)findViewById(R.id.start_width);
		et_dnEndWith = (EditText)findViewById(R.id.end_width);
		et_dnInclude = (EditText)findViewById(R.id.include_of);
		
		qes_service = (ImageButton) findViewById(R.id.qes_service);
		qes_rule = (ImageButton) findViewById(R.id.qes_rule);
		qes_pattern = (ImageButton) findViewById(R.id.qes_pattern);
		
		serviceSwitch = (CheckBox)findViewById(R.id.help_service_switch);
		spinnerFilterType = (Spinner) findViewById(R.id.spinner_filter_type);
		
		//Restore prefix as set to page
		ConfigurateManager.setContext(this);
		ConfigurateManager.restorePrefs();
		et_block_prefix.setText(ConfigurateManager.prefix);
		et_block_midfix.setText(ConfigurateManager.midfix);
		et_block_lastfix.setText(ConfigurateManager.lastfix);
		et_pattern.setText(ConfigurateManager.pattern);

		et_dnStartWith.setText(ConfigurateManager.dnStartWith);
		et_dnEndWith.setText(ConfigurateManager.dnEndWith);
		et_dnInclude.setText(ConfigurateManager.dnInclude);
		
		serviceSwitch.setChecked(ConfigurateManager.serviceOn);
		Resources resb = this.getResources();
		CharSequence[] res = {
				resb.getString(R.string.config_for_no_filter),
				resb.getString(R.string.config_for_dial),
				resb.getString(R.string.config_for_not_dial)};
		ArrayAdapter listAdapter = 
        	//ArrayAdapter.createFromResource(this, R.string.maillist, android.R.layout.simple_spinner_item);
			new ArrayAdapter(this, android.R.layout.simple_list_item_1, res);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
		if(spinnerFilterType != null){
			spinnerFilterType.setAdapter(listAdapter);
			spinnerFilterType.setSelection(ConfigurateManager.filterType);
			spinnerFilterType.setSelected(true);
		} else {
			showToastMessage("Spinner is NULL!!");
		}
    }
	
	
	private void setListeners() {
		button_setup.setOnClickListener(save);
		qes_rule.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final Builder builder = new Builder(ConfigureActivity.this);
		    	builder.setTitle("Rule configure");
		    	builder.setIcon(R.drawable.icon);
		    	builder.setMessage(getResources().getString(R.string.help_rule_config));
		    	builder.show();
			}
		});
		qes_service.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final Builder builder = new Builder(ConfigureActivity.this);
		    	builder.setTitle("Service configure");
		    	builder.setIcon(R.drawable.icon);
		    	builder.setMessage(getResources().getString(R.string.help_service_config));
		    	builder.show();
			}
		});
		qes_pattern.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final Builder builder = new Builder(ConfigureActivity.this);
		    	builder.setTitle("Pattern configure");
		    	builder.setIcon(R.drawable.icon);
		    	builder.setMessage(getResources().getString(R.string.help_pattern_config));
		    	builder.show();
			}
		});
		spinnerFilterType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int id, long i) {
				//System.out.println(">>>id=" + id + ";i=" + i);
				ConfigurateManager.setFilterType(id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private OnClickListener save = new OnClickListener(){
		public void onClick(View v) {
			String v_prefix = et_block_prefix.getText().toString();
			String v_midfix = et_block_midfix.getText().toString();
			String v_lastfix = et_block_lastfix.getText().toString();
			String v_pattern = et_pattern.getText().toString();

			String v_dnStartWith = et_dnStartWith.getText().toString();
			String v_dnEndWith = et_dnEndWith.getText().toString();
			String v_dnInclude = et_dnInclude.getText().toString();
			boolean isServiceOn = serviceSwitch.isChecked();
			
			showToastMessage("Saving prefix to " + v_prefix + ", Service is " + (isServiceOn?"ON":"OFF"));
			ConfigurateManager.setContext(v.getContext());
			ConfigurateManager.setPrefix(v_prefix);
			ConfigurateManager.setMidfix(v_midfix);
			ConfigurateManager.setLastfix(v_lastfix);
			ConfigurateManager.setPattern(v_pattern);
			ConfigurateManager.setServiceOn(isServiceOn);
			
			ConfigurateManager.setDnStartWith(v_dnStartWith);
			ConfigurateManager.setDnEndWith(v_dnEndWith);
			ConfigurateManager.setDnInclude(v_dnInclude);
			
			ConfigurateManager.writePrefs();
			
			RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_initial_layout);
			if(ConfigurateManager.serviceOn){
				views.setImageViewResource(R.id.WidgeImageButton01, R.drawable.serviceon);
				views.setTextViewText(R.id.WidgetText, ConfigurateManager.MSG_SERVICE_ON);
			} else {
				views.setImageViewResource(R.id.WidgeImageButton01, R.drawable.serviceoff);
				views.setTextViewText(R.id.WidgetText, ConfigurateManager.MSG_SERVICE_OFF);
			}
			AppWidgetManager manager = AppWidgetManager.getInstance(v.getContext());
	        // Push update for this widget to the home screen
	        ComponentName thisWidget = new ComponentName(v.getContext(), OutgoingCallFilterWidget.class);
	        manager.updateAppWidget(thisWidget, views);
			
			
			
		}
    	
    };
    
    
    
}
