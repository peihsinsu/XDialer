package com.xdialer.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xdialer.R;

public class PhoneBookActivity extends Activity {
	private ListAdapter mAdapter;
	private ListView listView;

    private static EditText textView;
    
	private static List<HashMap<String, String>> items = new ArrayList<HashMap<String,String>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.contactquery);
        
		//鎖定橫向螢幕
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		final Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				null,
				null, //ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " > 0",
				null, 
				ContactsContract.CommonDataKinds.Phone.NUMBER + " ASC");
		if(phones != null)
		while (phones.moveToNext()) {
			String strPhoneName = 
				phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String strPhoneNumber = 
				phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			//System.out.println("name:"+strPhoneName+";phone number:" + strPhoneNumber);
			if(strPhoneName != null && strPhoneNumber != null
					&& !"".equals(strPhoneName) && !"".equals(strPhoneNumber)){
				HashMap<String,String> i = new HashMap<String,String>();
				i.put("name",formatName(strPhoneName, 10));
				i.put("number", strPhoneNumber);
				items.add(i);
			}
		}
		
//		SimpleAdapter adapter = 
//			new SimpleAdapter(
//				this,
//				items,
//				R.layout.phonebook2,
//				new String[]{"name", "number"},
//				new int[]{R.id.row_entry, R.id.row_entry2 });
//		setListAdapter(adapter);
		
		SimpleAdapter adapter = 
			new SimpleAdapter(
				this,
				items,
				R.layout.phonebook2,
				new String[]{"name", "number"},
				new int[]{R.id.row_entry, R.id.row_entry2 });
		listView = (ListView) findViewById(R.id.ListView01);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {

				String phoneStr = ((HashMap<String, String>)listView.getItemAtPosition(position)).get("number").toString();
				
				openCallOptionDialog(phoneStr);
			}
		});
		
		listView.setAdapter(adapter);

		findViews();
		
	}
	
	private void findViews(){
		textView = (EditText)findViewById(R.id.EditText01);
        textView.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable editable) {
				System.out.println("editable=" + editable);
				if(editable != null && 
						editable.toString().length() >= 2){
					String queryStr = editable.toString();
					rebuildItems(textView, queryStr);
					SimpleAdapter adapter = 
						new SimpleAdapter(
							PhoneBookActivity.this,
							items,
							R.layout.phonebook2,
							new String[]{"name", "number"},
							new int[]{R.id.row_entry, R.id.row_entry2 });
					listView = (ListView) findViewById(R.id.ListView01);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View view, int position,
								long id) {

							String phoneStr = ((HashMap<String, String>)listView.getItemAtPosition(position)).get("number").toString();
							
							openCallOptionDialog(phoneStr);
						}
					});
					
					listView.setAdapter(adapter);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
//				System.out.println("s=" + s + "::start=" + start + "::count=" + count);
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
//				System.out.println("s=" + s + "::start=" + start + "::before=" + before);
			}
		  }
        );
	}
	
	private String currentQuery ;
	
	private void rebuildItems(TextView textView, String query) {
		if(currentQuery == null)
			currentQuery = query;
		else if(currentQuery != query && !query.startsWith("{")){ //避免change時候又去動到auto complete text
			System.out.println("currentQuery=" + currentQuery + ";query=" + query);
			items  = new ArrayList<HashMap<String,String>>();
			currentQuery = query;
		}
        System.out.println("before query, kept items size=" + items.size() + ", using query:" + query);
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
			System.out.println("Got peoples count=" + (phones != null ? phones.getCount() : 0));
			
			System.out.println("Get the phone size:" + phones.getCount() );
			if(phones != null && phones.getCount() >0){
				phones.moveToFirst();
				while (!phones.isAfterLast()) {
					String strPhoneName = 
						phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String strPhoneNumber = 
						phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					System.out.println(">>name:"+strPhoneName+";phone number:" + strPhoneNumber);
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
    	
    }
	
	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
////		final Cursor cursor = getContentResolver().query(
////				People.CONTENT_URI, 
////				null, 
////				Contacts.Phones.NUMBER + ">0", 
////				null, 
////				Phones.NAME + " ASC");
////		startManagingCursor(cursor);
////
////		String[] columns = new String[] { People.NAME, Contacts.People.NUMBER };
////		int[] names = new int[] { R.id.row_entry, R.id.row_entry2 };
////
////		//TODO 1: How to clean the empty number people....OK
////		//TODO 2: How to get the sim card contact
////		mAdapter = new SimpleCursorAdapter(this, R.layout.phonebook, cursor, columns, names);
////		setListAdapter(mAdapter);
//		
//		if(items == null || items.size() <= 0){
//			final Cursor peoples = getContentResolver().query(
//					ContactsContract.Contacts.CONTENT_URI,
//					null, 
//					ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0", 
//					null, 
//					ContactsContract.Contacts.DISPLAY_NAME + " ASC");
//			peoples.moveToFirst();
//	//		List<HashMap<String, String>> items = new ArrayList<HashMap<String,String>>();
//			while(peoples.moveToNext()){
//				String contactId = peoples.getString(peoples.getColumnIndex(ContactsContract.Contacts._ID));
//				int hasPhone = (peoples.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//				if( hasPhone > 0 ) {
//					final Cursor phones = getContentResolver().query(
//							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//							null,
//							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
//							null, 
//							ContactsContract.CommonDataKinds.Phone.NUMBER + " ASC");
//					if(phones != null)
//					while (phones.moveToNext()) {
//						String strPhoneName = 
//							phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//						String strPhoneNumber = 
//							phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//						//System.out.println("name:"+strPhoneName+";phone number:" + strPhoneNumber);
//						if(strPhoneName != null && strPhoneNumber != null
//								&& !"".equals(strPhoneName) && !"".equals(strPhoneNumber)){
//							HashMap<String,String> i = new HashMap<String,String>();
//							i.put("name",formatName(strPhoneName, 10));
//							i.put("number", strPhoneNumber);
//							items.add(i);
//						}
//					}
//				}
//			}
//		}
//		//SimpleAdapter demo
//		SimpleAdapter adapter = 
//			new SimpleAdapter(
//				this,
//				items,
//				R.layout.phonebook2,
//				new String[]{"name", "number"},
//				new int[]{R.id.row_entry, R.id.row_entry2 });
//		setListAdapter(adapter);
//
//	}
	
	public String formatName(String name, int digit){
		if(name != null && name.length() > digit)
			return name.substring(0, digit) + "...";
		return name;
	}

	//@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		
		//super.onListItemClick(listView, view, position, id);

//		Cursor cursor = (Cursor) mAdapter.getItem(position);
//		int name = cursor.getColumnIndex(People.NAME);
//		int phone = cursor.getColumnIndex(People.NUMBER);
//		String nameStr = cursor.getString(name);
//		String phoneStr = cursor.getString(phone);
		
		String phoneStr = ((HashMap<String, String>)listView.getItemAtPosition(position)).get("number").toString();
		
		openCallOptionDialog(phoneStr);
	}
	
	private void performDial(String number){
        if(number!=null){
          
          try {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }//if
    }
	
	public void openCallOptionDialog(final String number){
//    	final String number = editText.getText().toString();
		final String prefix = getCurrPrefix();
    	final String ipNumber = prefix + number;
    	final Builder builder = new Builder(this);
    	builder.setTitle("Title");
    	builder.setIcon(R.drawable.icon);
    	builder.setMessage("Choice your dialer..." + number + ". Current Prefix is: " + prefix);
    	
    	builder.setPositiveButton("IP Dialer", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				System.out.println("label 01....");
				showToastMessage("Dial the number:[" + ipNumber + "]");
				performDial(ipNumber);
			}
		});
    	
    	builder.setNeutralButton("Normal Dialer", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("label 02....");
				showToastMessage("Dial the number:[" + number + "]");
				performDial(number);
			}
    		
    	});
    	
    	builder.setNegativeButton("Help", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("label 03....");
				Uri uri = Uri.parse("http://www.google.com");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
    		
    	});
    	
    	builder.show();
    }
	
	public void showToastMessage(String message){
    	Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
	
	public static String PREF = "xdialer";
    public static String PREF_STORE_NAME = "xdialer_store";
	//get preferences
    public String getCurrPrefix(){
        SharedPreferences settings = getSharedPreferences(PREF, 0);
        String prefStoreValue = settings.getString(PREF_STORE_NAME, "");
        if(! "".equals(prefStoreValue)){
        	return prefStoreValue;
        }
        return "";
    }
}
