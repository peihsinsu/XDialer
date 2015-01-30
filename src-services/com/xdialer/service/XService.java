package com.xdialer.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.base.param.Tool;
import com.xdialer.ConfigurateManager;
import com.xdialer.P;
import com.xdialer.widget.OutgoingCallFilterWidget;


public class XService extends Service {

	public RemoteViews widgetViews;
	
	@Override
	public void onCreate(){
		super.onCreate();
		Log.d(P.TAG, "[XService]Starting service... onCreate");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(P.TAG, "[XService]Starting service... onStart, startId=" + startId + ", intent=" + intent);
		Log.d(P.TAG, "[XService]intent.getStringExtra= " + (intent!=null?intent.getStringExtra(P.FLAG_DO_UPDATE):"NULL!"));
		if(intent != null)
			switchService();
		else
			startService(new Intent(this, WidgetUpdateService.class));
	}
	
	/**
	 * @see android.app.Service#onBind(Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(P.TAG, "[XService]Starting service... onBind");
		return null;
	}
	
	
	public void savePrefs(boolean serviceOn){
		ConfigurateManager.setContext(this);
		Log.d(P.TAG, "Set serviceOn to: " + serviceOn);
		ConfigurateManager.setServiceOn(serviceOn);
		ConfigurateManager.writePrefs();
	}
	
	
	private RemoteViews getWidgetViews(){
		if(widgetViews == null)
			widgetViews = new RemoteViews(this.getPackageName(), Tool.widget_initial_layout);
		//Prepare widget status
		ConfigurateManager.setContext(this);
		ConfigurateManager.restorePrefs();
		return widgetViews;
	}
	
	private void updateWidgetViews(RemoteViews widgetViews){
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        // Push update for this widget to the home screen
        ComponentName thisWidget = new ComponentName(this, OutgoingCallFilterWidget.class);
        manager.updateAppWidget(thisWidget, widgetViews);
	}
	
	private void switchService() {
		Log.d(P.TAG, "[XService]Inner switchService... ");
		widgetViews = getWidgetViews();
		ConfigurateManager.setContext(this);
		boolean isServiceOn = ConfigurateManager.isServiceOn();
		if(isServiceOn) { // To set service off 
			widgetViews.setImageViewResource(Tool.WidgeImageButton01, Tool.serviceoff);
			widgetViews.setTextViewText(Tool.WidgetText, ConfigurateManager.MSG_SERVICE_OFF);
			savePrefs(false);
			Toast.makeText(this, "XDialer Service is turn to OFF!", Toast.LENGTH_SHORT).show();
		} else { // To set service on
			widgetViews.setImageViewResource(Tool.WidgeImageButton01, Tool.serviceon);
			widgetViews.setTextViewText(Tool.WidgetText, ConfigurateManager.MSG_SERVICE_ON);
			savePrefs(true);
			Toast.makeText(this, "XDialer Service is turn to ON!", Toast.LENGTH_SHORT).show();
		}
		
		updateWidgetViews(widgetViews);
		
	}


}
