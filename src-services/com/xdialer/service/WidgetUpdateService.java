package com.xdialer.service;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.base.param.Tool;
import com.shenming.base.service.AbsWidgetUpdateService;
import com.xdialer.ConfigurateManager;
import com.xdialer.P;
import com.xdialer.widget.OutgoingCallFilterWidget;

public class WidgetUpdateService extends AbsWidgetUpdateService {
	int intent_id = 100;
	@Override
	protected void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
		ConfigurateManager.setContext(context);
		ConfigurateManager.restorePrefs();
		Log.d(P.TAG, "Updating widget id[" + appWidgetId + "]...ConfigurateManager.serviceOn="+ConfigurateManager.serviceOn);
        RemoteViews views = new RemoteViews(context.getPackageName(), Tool.widget_initial_layout);
        Intent intent = new Intent(context, XService.class);
        intent.putExtra(P.FLAG_DO_UPDATE, "Y");
		PendingIntent pendingIntent = PendingIntent.getService(context, intent_id, intent, 0);
		
		views.setOnClickPendingIntent(Tool.WidgeImageButton01, pendingIntent);
		ConfigurateManager.setContext(context);
		if(ConfigurateManager.serviceOn){
			views.setImageViewResource(Tool.WidgeImageButton01, Tool.serviceon);
			views.setTextViewText(Tool.WidgetText, ConfigurateManager.MSG_SERVICE_ON);
		} else {
			views.setImageViewResource(Tool.WidgeImageButton01, Tool.serviceoff);
			views.setTextViewText(Tool.WidgetText, ConfigurateManager.MSG_SERVICE_OFF);
		}
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

	@Override
	public Class getWidgetClass() {
		return OutgoingCallFilterWidget.class;
	}

	@Override
	public String getTAG() {
		return P.TAG;
	}
}
