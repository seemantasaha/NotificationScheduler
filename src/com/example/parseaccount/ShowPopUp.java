package com.example.parseaccount;

import android.app.Activity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ShowPopUp extends Activity implements OnClickListener {

	TextView tv_msg;
	Button ok;
	Button cancel;
	String msg;
	boolean click = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Notification");
		setContentView(R.layout.popupdialog);
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
            msg = extras.getString("MESSAGE");
        }
        
        tv_msg = (TextView) findViewById(R.id.message);
        tv_msg.setText(msg);
		
		ok = (Button)findViewById(R.id.popOkB);
		ok.setOnClickListener(this);
		cancel = (Button)findViewById(R.id.popCancelB);
		cancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	}
}