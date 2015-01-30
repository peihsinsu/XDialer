package com.xdialer.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.xdialer.ConfigurateManager;
import com.xdialer.P;
import com.xdialer.service.WidgetUpdateService;

public class OutgoingCallFilterWidget extends AppWidgetProvider {

	public static boolean needInitial;
	public RemoteViews widgetViews;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(P.TAG, "OutgoingCallFilterWidget innser onUpdate...");
		ConfigurateManager.setContext(context);
		ConfigurateManager.restorePrefs();
		Intent xintent = new Intent(context, WidgetUpdateService.class);
		context.startService(xintent);
	}
	
}
