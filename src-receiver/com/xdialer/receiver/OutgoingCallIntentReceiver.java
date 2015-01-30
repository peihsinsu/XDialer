package com.xdialer.receiver;

import com.xdialer.ConfigurateManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.Toast;

public class OutgoingCallIntentReceiver extends BroadcastReceiver {

//	private static final String XDIALER_FLAG = "XDialerFlag";
//	private static SharedPreferences settings;
//	public static Map preferenceMap;
//	
//	private String prefix;
//	private boolean serviceOn;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		restorePrefs(context);
		
		System.out.println("===========================================================");
		System.out.println("BroadcastReceiver onReceive called....");
		System.out.println("The restored prefix = " + ConfigurateManager.prefix + ", service status=" + (ConfigurateManager.serviceOn?"ON":"OFF"));
		System.out.println("Current intent.getAction() = " + intent.getAction());
		System.out.println("===========================================================");
		if(ConfigurateManager.serviceOn){
			reformatDialNumber(context, intent);
		} else {
			//Do nothing...
		}
		
	}
	
	private void callService(Context context){
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.xdialerpro.XService");//XService.class.getName());
		context.startService(serviceIntent);
	}
	
	private void dialInReceiver(Context context, String prefix, String number, Intent intent){
		if(ConfigurateManager.filterType > 0){
			System.out.println("Using filter of " + 
					(ConfigurateManager.filterType == 1 ? "INCLUDE":"EXCLUDE") +
					",prefix=" + prefix + ",number=" + number);
			if(!isPassFilter(context,prefix,number,ConfigurateManager.filterType)){
				return;
			}
		} else {
			System.out.println("No filter!");
		}
		this.setResultData(null);
		String callIntent  = "android.intent.action.CALL";
		String phoneNumber = prefix + number;
		log("dialInReceiver....dial=" + phoneNumber);
		log(String.format("prefix=%s,  numbere=%s)", prefix, number));
		log("Uri.encode(phoneNumber)=" + Uri.encode(phoneNumber) );
//		log("String.format..........=" + String.format("tel:" + Uri.encode(phoneNumber)));
		log("Uri.parse..............=" + Uri.parse("tel:" + Uri.encode(phoneNumber)));
		Intent newIntent = new Intent(callIntent, Uri.parse(("tel:" + Uri.encode(phoneNumber))));
		//Intent newIntent = new Intent(callIntent, Uri.parse(String.format("tel:" + Uri.encode(phoneNumber))));
		//Intent newIntent = new Intent(callIntent, Uri.parse("tel:" + phoneNumber));
		//newIntent.putExtras(intent);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);
	}
	
	private void log(String msg) {
		Log.d("XDialer", msg);
	}

	private boolean isPassFilter(Context context, String prefix,
			String number, int filterType) {
		boolean isExclude = (filterType == 2 ? true: false);
		System.out.println("filterType = " + filterType + ",isExclude=" + isExclude + ",prefix=" + prefix + ",number=" + number);
		System.out.println("ConfigurateManager.dnStartWith=" + ConfigurateManager.dnStartWith);
		System.out.println("ConfigurateManager.dnEndWith=" + ConfigurateManager.dnEndWith);
		System.out.println("ConfigurateManager.dnInclude=" + ConfigurateManager.dnInclude);
		if(isExclude){ //Exclude
			if(ConfigurateManager.dnStartWith != null && !"".equals(ConfigurateManager.dnStartWith)){
				if(number.startsWith(ConfigurateManager.dnStartWith)){
					System.out.println(">>>1");
					return false;
				} 
			}
			if(ConfigurateManager.dnEndWith != null && !"".equals(ConfigurateManager.dnEndWith)){
				if(number.endsWith(ConfigurateManager.dnEndWith)){
					System.out.println(">>>2");
					return false;
				} 
			}
			if(ConfigurateManager.dnInclude != null && !"".equals(ConfigurateManager.dnInclude)){
				if(number.indexOf(ConfigurateManager.dnInclude) >=0){
					System.out.println(">>>3");
					return false;
				} 
			}
		} else { //Include
			boolean isPass = true;
			if(ConfigurateManager.dnStartWith != null && !"".equals(ConfigurateManager.dnStartWith)){
				if(!number.startsWith(ConfigurateManager.dnStartWith)){
					System.out.println(">>>4");
					isPass = false;
				} 
			}
			if(ConfigurateManager.dnEndWith != null && !"".equals(ConfigurateManager.dnEndWith)){
				if(!number.endsWith(ConfigurateManager.dnEndWith)){
					System.out.println(">>>5");
					isPass = false;
				} 
			}
			if(ConfigurateManager.dnInclude != null && !"".equals(ConfigurateManager.dnInclude)){
				if(number.indexOf(ConfigurateManager.dnInclude) < 0){
					System.out.println(">>>6");
					isPass = false;
				} 
			}
			return isPass;
		}
		return true;
	}

	private void reformatDialNumber(Context context, Intent intent) {
		try {
			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				
				String TELEPHONENUMBER = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
				//To prevent the special dial...
				String dial = ConfigurateManager.getCombinedPrefix();
				dial = dial.replaceAll("p", String.valueOf(PhoneNumberUtils.PAUSE));
				//prefix = prefix.replaceAll("p", String.valueOf(PhoneNumberUtils.PAUSE));
				
				if((TELEPHONENUMBER != null && TELEPHONENUMBER.startsWith(dial)))
					return;
				
				System.out.println("OutgoingCallIntentReceiver get TELEPHONENUMBER=" + TELEPHONENUMBER );
				System.out.println("Before getResultData:" + this.getResultData() + ";code=" + this.getResultCode());
				System.out.println("After getResultData:" + this.getResultData() + ";code=" + this.getResultCode());
				System.out.println("Prefix=" + dial);
				
				if(this.getResultData() != null &&
						this.getResultData().startsWith(dial)) {
					Toast.makeText(context, "Prefix Appended..." + this.getResultData(),Toast.LENGTH_SHORT).show();
					return;
				} else {
					Toast.makeText(context, "Original Dialing..." + this.getResultData(),Toast.LENGTH_SHORT).show();
					dialInReceiver(context, dial, TELEPHONENUMBER, intent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void restorePrefs(Context context){
//    	System.out.println("OutgoingCallIntentReceiver restorePrefs");
//    	settings = context.getSharedPreferences(ProjectBaseActivity.PREF_STORE_NAME, 0);
//        preferenceMap = settings.getAll();
//        System.out.println("OutgoingCallIntentReceiver preferenceMap=" + preferenceMap);
//        
//        this.prefix = (String)preferenceMap.get(ProjectBaseActivity.PREF_KEY_PREFIX);
//        this.serviceOn = settings.getBoolean(ProjectBaseActivity.PREF_KEY_ISSERVICEON, false);
        ConfigurateManager.setContext(context);
        ConfigurateManager.restorePrefs();
    }
    

}