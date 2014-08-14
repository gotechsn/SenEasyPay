package com.terrangatech.seneasypay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

@SuppressLint("DefaultLocale")
public class MainActivity extends ActionBarActivity {

	private String results;
	private String formatMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void btnScanClick(View v) {
		//Toast.makeText(MainActivity.this, "Response Click", Toast.LENGTH_SHORT).show();
				
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Log.i("xZing", "contents: " + contents + " format: " + format);// Handle
				
				AlertDialog.Builder bd = new AlertDialog.Builder(MainActivity.this);
				bd.setTitle("EasyPay");
				results = contents;
				
				if (formatMessage(contents)!=null){
					
					bd.setMessage(formatMessage(contents));
					bd.setPositiveButton("PAY", new SendSMSListener());
					bd.setNegativeButton("Annuler", null);
					bd.create().show();	
				}
				
				else {
					bd.setMessage("FORMAT DE CODE NON PRISE EN CHARGE");
					bd.setNeutralButton("OK", null);
					bd.create().show();
				}
				

			
			} else if (resultCode == RESULT_CANCELED) { // Handle cancel
				Log.i("xZing", "Cancelled");
			}
		}

	}
	
	@SuppressLint("DefaultLocale")
	private CharSequence formatMessage(String contents) {
		// TODO Auto-generated method stub
		
		String [] splitedContents  = splitContents(contents);
		
	
		// Code RELICAT Payement de relicat
		// Code TRANSPORT Payement de transport
		// Code 3 DON Faire un don
		
		if (splitedContents[0].toLowerCase().contentEquals("relicat")){
			formatMessage = "Payer votre relicat de "+ splitedContents[1] +" à "+ splitedContents[2];
		}
		else if (splitedContents[0].toLowerCase().contentEquals("transport")){
			formatMessage = "Payer votre transport de "+ splitedContents[1] +" à "+ splitedContents[2];
		}
		
		else{
			formatMessage = null;
		}
		
		return formatMessage;
	}

	private String[] splitContents(String contents) {
		// TODO Auto-generated method stub
		return contents.split(" ");
	}

	private class SendSMSListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
			
		}

		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
