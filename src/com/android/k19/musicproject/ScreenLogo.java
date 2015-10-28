package com.android.k19.musicproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ScreenLogo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo_show);
		new Thread(new Runnable() {            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(2000);
                    Intent i = new Intent(ScreenLogo.this, MusicProject.class);
                    startActivity(i);
                    ScreenLogo.this.finish();
                    //handler.sendMessage(handler.obtainMessage(1));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
	}
	 /*Handler handler=new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case 1:
	                Intent i = new Intent();
	                i.setClass(getApplicationContext(), MusicProject.class);            
	                startActivity(i);
	                finish();
	                break;

	            default:
	                break;
	            }
	            
	        }
	    };*/
}
