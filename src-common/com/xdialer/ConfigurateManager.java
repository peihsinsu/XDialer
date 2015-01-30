package com.xdialer;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class ConfigurateManager {

    public static String prefix = "P,M,L";
    public static String midfix;
	public static String lastfix;
	public static String pattern = "P,M,L,";
	
	public static int filterType = 0;
	

	public static String dnStartWith;
	public static String dnEndWith;
	public static String dnInclude;
	
	public static boolean serviceOn ;
    public static String PREF_STORE_NAME = "xdialer";
    public static String PREF_KEY_PREFIX = "xdialer_prefix";
    public static String PREF_KEY_MIDFIX = "xdialer_midfix";
    public static String PREF_KEY_LASTFIX = "xdialer_lastfix";
    public static String PREF_KEY_PATTERN = "xdialer_pattern";
    public static String PREF_KEY_ISSERVICEON = "xdialer_is_service_on";
    public static String PREF_KEY_FILTER_TYPE = "xdialer_filter_type";
    public static String PREF_KEY_DN_START_WITH = "xdialer_dnStartWith";
    public static String PREF_KEY_DN_END_WITH = "xdialer_dnEndWith";
    public static String PREF_KEY_DN_INCLUDE = "xdialer_dnInclude";
    public static String PREF_KEY_SKIP_ADS = "xdialer_skip_ads";
    
    public static String MSG_SERVICE_ON = "ON";
    public static String MSG_SERVICE_OFF = "OFF";
	public RemoteViews widgetViews;
	
	private static Context context;

	public static boolean skipAds;
	
	public static String getCombinedPrefix(){
		if(pattern != null)
			return pattern.replaceAll("P", prefix!=null?prefix:"").replaceAll("M", midfix!=null?midfix:"").replaceAll("L", lastfix!=null?lastfix:"");
		return null;
	}
	
	public static int getFilterType() {
		return filterType;
	}

	public static void setFilterType(int filterType) {
		ConfigurateManager.filterType = filterType;
	}

	public static String getDnStartWith() {
		return dnStartWith;
	}

	public static void setDnStartWith(String dnStartWith) {
		ConfigurateManager.dnStartWith = dnStartWith;
	}

	public static String getDnEndWith() {
		return dnEndWith;
	}

	public static void setDnEndWith(String dnEndWith) {
		ConfigurateManager.dnEndWith = dnEndWith;
	}

	public static String getDnInclude() {
		return dnInclude;
	}

	public static void setDnInclude(String dnInclude) {
		ConfigurateManager.dnInclude = dnInclude;
	}

	public static String getPattern() {
		return pattern;
	}

	public static void setPattern(String pattern) {
		ConfigurateManager.pattern = pattern;
	}

    public static String getMidfix() {
		return midfix;
	}

	public static void setMidfix(String midfix) {
		ConfigurateManager.midfix = midfix;
	}

	public static String getLastfix() {
		return lastfix;
	}

	public static void setLastfix(String lastfix) {
		ConfigurateManager.lastfix = lastfix;
	}
	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		ConfigurateManager.context = context;
	}

    public static String getPrefix() {
		return prefix;
	}

	public static void setPrefix(String prefix) {
		ConfigurateManager.prefix = prefix;
	}

	public static boolean isServiceOn() {
		return serviceOn;
	}

	public static void setServiceOn(boolean serviceOn) {
		ConfigurateManager.serviceOn = serviceOn;
	}

	public static void writePrefs(){
    	SharedPreferences settings = context.getSharedPreferences(PREF_STORE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        //System.out.println("ConfigurateManager saving preference: prefix=" + prefix + ",serviceOn=" + serviceOn );
        editor.putString(PREF_KEY_PREFIX, prefix);
        editor.putString(PREF_KEY_MIDFIX, midfix);
        editor.putString(PREF_KEY_LASTFIX, lastfix);
        editor.putString(PREF_KEY_PATTERN, pattern);
        editor.putBoolean(PREF_KEY_ISSERVICEON, serviceOn);

        editor.putString(PREF_KEY_DN_START_WITH, dnStartWith);
        editor.putString(PREF_KEY_DN_END_WITH, dnEndWith);
        editor.putString(PREF_KEY_DN_INCLUDE, dnInclude);
        editor.putInt(PREF_KEY_FILTER_TYPE, filterType);
        
        // Commit the edits!
        editor.commit();

    }
	
	public static void restorePrefs(){
    	System.out.println("ConfigureActivity restorePrefs");
    	SharedPreferences settings = context.getSharedPreferences(ConfigurateManager.PREF_STORE_NAME, 0);
        prefix = settings.getString(ConfigurateManager.PREF_KEY_PREFIX,"");
        midfix = settings.getString(ConfigurateManager.PREF_KEY_MIDFIX,"");
        lastfix = settings.getString(ConfigurateManager.PREF_KEY_LASTFIX,"");
        pattern = settings.getString(ConfigurateManager.PREF_KEY_PATTERN,"P,M,L,");
        if(pattern == null || "".equals(pattern)){
        	pattern = "P,M,L,";
        }
        
        serviceOn = settings.getBoolean(ConfigurateManager.PREF_KEY_ISSERVICEON, false);

        filterType = settings.getInt(ConfigurateManager.PREF_KEY_FILTER_TYPE,0);
        dnStartWith = settings.getString(ConfigurateManager.PREF_KEY_DN_START_WITH,"");
        dnEndWith = settings.getString(ConfigurateManager.PREF_KEY_DN_END_WITH,"");
        dnInclude = settings.getString(ConfigurateManager.PREF_KEY_DN_INCLUDE,"");
        skipAds = settings.getBoolean(ConfigurateManager.PREF_KEY_SKIP_ADS, false);
        
        System.out.println("ConfigureActivity get the prefix=" + prefix + "serviceOn=" + serviceOn);
    }
	

    
}
