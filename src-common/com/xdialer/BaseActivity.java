package com.xdialer;

import com.base.param.Tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

public abstract class BaseActivity extends Activity{

    public static String prefix="287137699p0p";
    public static boolean serviceOn = false;
    public static String PREF_STORE_NAME = "xdialer";
    public static String PREF_KEY_PREFIX = "xdialer_prefix";
    public static String PREF_KEY_ISSERVICEON = "xdialer_is_service_on";
    

    public static String getPrefix() {
		return prefix;
	}

	public static void setPrefix(String prefix) {
		BaseActivity.prefix = prefix;
	}
	
	public static boolean isServiceOn(){
		return serviceOn;
	}
	
	public static void setServiceOn(boolean serviceOn){
		BaseActivity.serviceOn = serviceOn;
	}
	
	@Override
    protected void onStop(){
       super.onStop();
       //writePrefs();
    }
    
    
    public void showToastMessage(String message){
    	Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    public void showAlertMessage(Builder builder, String title, String message){
    	AlertDialog alertDialog = builder.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.show();
    }
    
    public void performDial(String number){
        if(number!=null){
          try {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }//if
    }
    
    /**
     * Restore preferences
     * 
     */
    public void restorePrefs(){
        SharedPreferences settings = getSharedPreferences(PREF_STORE_NAME, 0);
        String prefStoreValue = settings.getString(PREF_KEY_PREFIX, "");
        if(! "".equals(prefStoreValue)){
        	setPrefix(prefStoreValue);
        }
        setServiceOn(settings.getBoolean(PREF_KEY_ISSERVICEON, false));
        
    }
    
    /**
     * Get preferences
     * 
     * @param key
     * @return
     */
    public String getPrefs(String key){
        SharedPreferences settings = getSharedPreferences(PREF_STORE_NAME, 0);
        String prefStoreValue = settings.getString(key, "");
        if(! "".equals(prefStoreValue)){
        	return prefStoreValue;
        }
        return "";
    }
    
    /**
     * Write preference
     * 
     */
    public void writePrefs(){
    	SharedPreferences settings = getSharedPreferences(PREF_STORE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        System.out.println("Saving preference: prefix=" + getPrefix() + ",serviceOn=" + serviceOn );
        editor.putString(PREF_KEY_PREFIX, getPrefix());
        editor.putBoolean(PREF_KEY_ISSERVICEON, isServiceOn());
        // Commit the edits!
        editor.commit();

    }
    
    public void openCallOptionDialog(EditText editText){
    	final String number = editText.getText().toString();
    	final String ipNumber = Uri.encode(String.format(prefix+number)); 
    		//Uri.encode(prefix + number);
    	final Builder builder = new Builder(this);
    	builder.setTitle("Dial Options");
    	builder.setIcon(Tool.ICON);
    	builder.setMessage("Choice your dialer of dialing number:" + number + ". Current Prefix is: " + prefix);
    	
    	builder.setPositiveButton("IP Dialer", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				showToastMessage("Dial the number:[" + ipNumber + "]");
				performDial(ipNumber);
			}
		});
    	
    	builder.setNeutralButton("Normal Dialer", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				showToastMessage("Dial the number:[" + number + "]");
				performDial(number);
			}
    		
    	});
    	
    	builder.setNegativeButton("Site", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				gotoUrl("http://code.google.com/p/xdialer/");
			}
    		
    	});
    	
    	builder.show();
    }
    
    public void gotoUrl(String url){
    	Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
    }
    
    
}
