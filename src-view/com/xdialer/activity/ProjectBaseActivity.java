package com.xdialer.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.xdialer.BaseActivity;
import com.xdialer.ConfigurateManager;
import com.xdialer.R;



public abstract class ProjectBaseActivity extends BaseActivity{
	public static boolean isServiceOn = false;
	public static String prefix = "287137699p0p";
	
	//Use for camera
	public final static String PICTURE_PATH = "/sdcard/XDialer";
	//Use for camera
	public String getPicturePath() {
		return ProjectBaseActivity.PICTURE_PATH;
	}

	public final static int MENU_CONFIG = Menu.FIRST;
	//public final static int MENU_HISTORY = Menu.FIRST + 1;
	public final static int MENU_CAMERA = Menu.FIRST + 1;
	public final static int MENU_ABOUT = Menu.FIRST +2;
	public final static int MENU_EXIT = Menu.FIRST +3;
	
	public String getPrefStoreName(){
		return BaseActivity.PREF_STORE_NAME;
		
	};
	
	public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
//    	menu.add(0, MENU_CONFIG, 0, R.string.BTN_CONFIG).setIcon(R.drawable.btn_config);
//    	//menu.add(0, MENU_HISTORY, 0, R.string.BTN_HISTORY).setIcon(R.drawable.btn_history);
//    	menu.add(0, MENU_CAMERA, 0, R.string.BTN_CAMERA).setIcon(R.drawable.btn_camera);
//    	//menu.add(0, MENU_EXIT, 0, R.string.BTN_EXIT).setIcon(R.drawable.btn_exit); //僅主畫面show EXIT
//    	menu.add(0, MENU_ABOUT, 0, R.string.BTN_ABOUT).setIcon(R.drawable.btn_about);
    	return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case MENU_EXIT:
//    		finish();
    		//super.onDestroy();
    		android.os.Process.killProcess(android.os.Process.myPid());
    		break;
    	case MENU_ABOUT:
//    		switchActivity(this, AboutActivity.class);
    		break;
    	}
    	return true;
    }

    public void openCallOptionDialog(EditText editText){
    	final String number = editText.getText().toString();
    	final String ipNumber = Uri.encode(String.format(ConfigurateManager.getCombinedPrefix()+number)); 
    		//Uri.encode(prefix + number);
    	final Builder builder = new Builder(this);
    	builder.setTitle("Dial Options");
    	builder.setIcon(R.drawable.icon);
    	builder.setMessage("Choice your dialer of dialing number:" + number + ". Current Prefix is: " + ConfigurateManager.getCombinedPrefix());
    	
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

}
