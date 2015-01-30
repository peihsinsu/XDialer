package com.xdialer;

import android.app.Activity;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class AdSenseUtil {
	public static boolean isTest = true;
//	public static AdRequest request = new AdRequest();
	
	public static void addAdSense(Activity act, int adview){
		// Look up the AdView as a resource and load a request.
//	    AdView adView = (AdView)view;
		AdView adView = (AdView) act.findViewById(adview);
	    AdRequest request = new AdRequest();
	    
//	    if(request == null)
//	    	request = new AdRequest();
	    
	    if(isTest)
	    	request.setTesting(true);
	    
	    adView.loadAd(request);
	}

}
