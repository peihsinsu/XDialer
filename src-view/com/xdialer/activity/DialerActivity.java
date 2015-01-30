package com.xdialer.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;

import com.xdialer.ConfigurateManager;
import com.xdialer.P;
import com.xdialer.R;

public class DialerActivity extends ProjectBaseActivity {
    private static Button button_Dial;
    private static AutoCompleteTextView acTextView;
    
    private static List<HashMap<String, String>> items;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        items = new ArrayList<HashMap<String,String>>();
        
        setContentView(R.layout.main);
		//鎖定橫向螢幕
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        restorePrefs();

		ConfigurateManager.setContext(this);
		ConfigurateManager.restorePrefs();
		
        findViews();
        setListeners();
        
        acTextView.setSingleLine();
    }

    @Override
    protected void onStop(){
       super.onStop();
       ConfigurateManager.writePrefs();
    }
    
    private void findViews(){
    	button_Dial = (Button)findViewById(R.id.Button01);
//    	button_phoneBookBtn = (Button)findViewById(R.id.phoneBookBtn);
//    	button_help = (Button)findViewById(R.id.button_help);
    	acTextView = (AutoCompleteTextView)findViewById(R.id.EditText01);
    	
    	acTextView.setThreshold(2);
//    	acTextView.
        //buildComplete(acTextView, null);
        acTextView.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable editable) {
				Log.d(P.TAG, "editable=" + editable);
				if(editable != null && 
						editable.toString().length() >= 2){
					String queryStr = editable.toString();
					buildComplete2(acTextView, queryStr);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
//				Log.d(P.TAG, "s=" + s + "::start=" + start + "::count=" + count);
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
//				Log.d(P.TAG, "s=" + s + "::start=" + start + "::before=" + before);
			}
		  }
        );
    }
    
    private void buildComplete2(AutoCompleteTextView acTextView, String query) {
		if(currentQuery == null)
			currentQuery = query;
		else if(currentQuery != query && !query.startsWith("{")){ //避免change時候又去動到auto complete text
			Log.d(P.TAG, "currentQuery=" + currentQuery + ";query=" + query);
			items  = new ArrayList<HashMap<String,String>>();
			currentQuery = query;
		}
        Log.d(P.TAG, "before query, kept items size=" + items.size() + ", using query:" + query);
    	if(items == null || items.size() <= 0){
    		
			Cursor phones = null;
			if(query != null || !"".equals(query))
				phones = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null, 
					ContactsContract.CommonDataKinds.Phone.NUMBER + " like '%" + query + "%' or " + 
					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like '%" + query + "%'",
					null, 
					null);
			else
				phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, 
						ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " > 0",
						null, 
						null);
			Log.d(P.TAG, "Got peoples count=" + (phones != null ? phones.getCount() : 0));
			
			Log.d(P.TAG, "Get the phone size:" + phones.getCount() );
			if(phones != null && phones.getCount() >0){
				phones.moveToFirst();
				while (!phones.isAfterLast()) {
					String strPhoneName = 
						phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String strPhoneNumber = 
						phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					Log.d(P.TAG, ">>name:"+strPhoneName+";phone number:" + strPhoneNumber);
					if(strPhoneName != null && strPhoneNumber != null && query != null 
							&& !"".equals(strPhoneName) && !"".equals(strPhoneNumber)){
						if(strPhoneName.toLowerCase().indexOf(query.toLowerCase()) >=0 ||
								strPhoneNumber.toLowerCase().indexOf(query.toLowerCase()) >=0	){
							HashMap<String,String> i = new HashMap<String,String>();
							i.put("name",formatName(strPhoneName, 10));
							i.put("number", strPhoneNumber);
							items.add(i);
						}
					}
					phones.moveToNext();
				}
			}
			
		}
    	
    	Log.d(P.TAG, "Final item size..." + items.size());
		SimpleAdapter adapter = 
			new SimpleAdapter(
				this,
				items,
				R.layout.phonebook,
				new String[]{"name", "number"},
				new int[]{R.id.row_entry, R.id.row_entry2 });
		acTextView.setAdapter(adapter);
    }
    
    /**
     * Build AutoCompleteTextView from cursor
     * @param acTextView
     * @param query
     */
    private void buildComplete(AutoCompleteTextView acTextView, String query) {
    	Log.d(P.TAG, "Get query string..." + query);
    	//先查出有電話號碼的人員
        Cursor peoples = buildQuery(query);
        if(peoples == null || peoples.getCount() <= 0){
        	Log.d(P.TAG, "Query result is null.... query string=" + query);
        	return;
        }
        
        //查出該些人員的電話資訊
        peoples.moveToFirst();
        Log.d(P.TAG, "Check.........." + peoples.isAfterLast());
		while(!peoples.isAfterLast()){
			String contactId = peoples.getString(peoples.getColumnIndex(ContactsContract.Contacts._ID));
			int hasPhone = (peoples.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if( hasPhone > 0 ) {
				final Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
						null, 
						ContactsContract.CommonDataKinds.Phone.NUMBER + " ASC");
				Log.d(P.TAG, "Get the phone size:" + phones.getCount() + ", query by " + contactId);
				if(phones != null && phones.getCount() >0){
					phones.moveToFirst();
					while (!phones.isAfterLast()) {
						String strPhoneName = 
							phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						String strPhoneNumber = 
							phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						Log.d(P.TAG, ">>name:"+strPhoneName+";phone number:" + strPhoneNumber);
						if(strPhoneName != null && strPhoneNumber != null && query != null 
								&& !"".equals(strPhoneName) && !"".equals(strPhoneNumber)){
							if(strPhoneName.toLowerCase().indexOf(query.toLowerCase()) >=0 ||
									strPhoneNumber.toLowerCase().indexOf(query.toLowerCase()) >=0	){
								HashMap<String,String> i = new HashMap<String,String>();
								i.put("name",formatName(strPhoneName, 10));
								i.put("number", strPhoneNumber);
								items.add(i);
							}
						}
						phones.moveToNext();
					}
				}
			}
			peoples.moveToNext();
		}
		
		Log.d(P.TAG, "Final item size..." + items.size());
		SimpleAdapter adapter = 
			new SimpleAdapter(
				this,
				items,
				R.layout.phonebook,
				new String[]{"name", "number"},
				new int[]{R.id.row_entry, R.id.row_entry2 });
		acTextView.setAdapter(adapter);
        
        
        
    }
    
    public String formatName(String name, int digit){
		if(name != null && name.length() > digit)
			return name.substring(0, digit) + "...";
		return name;
	}
    
    private String currentQuery ;
    
    /**
     * Execution query
     * @param query
     * @return
     */
	private Cursor buildQuery(String query) {
		if(currentQuery == null)
			currentQuery = query;
		else if(currentQuery != query){
			Log.d(P.TAG, "currentQuery=" + currentQuery + ";query=" + query);
			items  = new ArrayList<HashMap<String,String>>();
			currentQuery = query;
		}
        Log.d(P.TAG, "before query, kept items size=" + items.size() + ", using query:" + query);
    	if(items == null || items.size() <= 0){
    		
			Cursor peoples = null;
			if(query != null || !"".equals(query))
				peoples = getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI,
					null, 
					ContactsContract.Contacts.DISPLAY_NAME + " like '%" + query + "%'",
					null, 
					ContactsContract.Contacts.DISPLAY_NAME + " ASC");
			else
				peoples = getContentResolver().query(
						ContactsContract.Contacts.CONTENT_URI,
						null, 
						ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0",
						null, 
						ContactsContract.Contacts.DISPLAY_NAME + " ASC");
			Log.d(P.TAG, "Got peoples count=" + (peoples != null ? peoples.getCount() : 0));
			return peoples;
		}
        return null;
    }
    
    private void setListeners() {
    	button_Dial.setOnClickListener(click);
    	
    	/*Define the on-click listener for the list items*/
        acTextView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Build the Intent used to open WordActivity with a specific word Uri
            	Log.d(P.TAG, ">>position=" + position + ";id="+ id + ";items.size()=" + items.size());
            	if(items.size() > position){
	            	HashMap<String, String> rtn = items.get(position);
	            	Log.d(P.TAG, "rtn=" + rtn);
					acTextView.setText(rtn.get("number"));
					
            	} else {
            		showToastMessage("Query Phone Out of Bound...");
            	}
            }
        });
	}
    
    private OnClickListener click = new OnClickListener(){
		public void onClick(View arg0) {
			openOptionDialog();
		}
    };
    
    private View.OnClickListener phoneBookInvoker = new View.OnClickListener(){
		public void onClick(View view) {
			Intent intent = new Intent();
			intent.setClass(view.getContext(), PhoneBookActivity.class);
			//startActivity(intent);
			startActivityForResult(intent, 0);
		}
    };
    
    private Button.OnClickListener helpInvoker = new Button.OnClickListener(){
		public void onClick(View view) {
			Intent intent = new Intent();
			intent.setClass(view.getContext(), ConfigureActivity.class);
			startActivity(intent);
		}
    };
    
    private void openOptionDialog(){
    	openCallOptionDialog(acTextView);
    }
    
    /**
     * Using the res/menu/options_menu.xml as the option menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    
    /**
     * Configure the option menu item actions
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.d(P.TAG, "onOptionsItemSelected>>" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.exit:
            	finish();
            	return true;
            default:
                return false;
        }
    }
    
}