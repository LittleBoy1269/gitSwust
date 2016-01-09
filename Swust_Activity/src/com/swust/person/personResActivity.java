package com.swust.person;

import com.swust.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.swust.notepad.NotepadEditActivity;



public class personResActivity extends Activity {

	public TextView Username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_res);
		Username=(TextView) findViewById(R.id.person_res_id);
		Username.setText(com.swust.load.LoadActivity.getUsername());
		
	}
	
	
}
