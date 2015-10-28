/**
 * 
 */
package com.android.k19.musicproject.dataservices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.k19.musicproject.NowPlaying;
import com.android.k19.musicproject.R;
import com.android.k19.musicproject.application.MusicApplication;
import com.android.k19.musicproject.models.Song;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @author Trung Nguyen
 *
 */
public class Service_Playing extends Service {

	private MediaPlayer mp;
	private List<String> history_list;
	private SharedPreferences share;
	private boolean history_full;
	private String path;
	private Timer tm;
	private Handler myHandle;
	private Runnable runnable;
	
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mp = new MediaPlayer();
		history_list = new ArrayList<String>();
		tm = new Timer();
		share = getSharedPreferences("history", 0);
		myHandle = new Handler();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mp.stop();
		mp.release();
		tm.cancel();
		myHandle.removeCallbacks(runnable);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		final Bundle b = intent.getExtras();
		path = b.getString("path");
		try {
			mp.setDataSource(path);
			mp.prepare();
			mp.start();
			SetHistory(path);
			long time_seek_to = b.getLong("seek_time",-1);
			if(time_seek_to != -1){
				mp.seekTo(Integer.valueOf(String.valueOf(time_seek_to)) * 1000);
			}
			tm.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					runnable = new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
								long s = mp.getCurrentPosition()/1000;
								MusicApplication.getInstance().setSecond(s);
							}catch(Exception ex){
								
							}
						}
					};
					myHandle.post(runnable);
				}
			}, 0, 1000);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return 1;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	private void GetHistory(){
		history_list.add(share.getString("song1", ""));
		history_list.add(share.getString("song2", ""));
		history_list.add(share.getString("song3", ""));
		history_list.add(share.getString("song4", ""));
		history_list.add(share.getString("song5", ""));
		if(history_list.get(4).equals("")){
			history_full = true;
		}
	}
	private int TestExistInHistoryList(String path){
		int i = 0;
		for(i=0;i<5;i++){
			if(path.equals(history_list.get(i))){
				return i;
			}
		}
		return -1;
	}
	private void SetHistory(String path){
		GetHistory();
		int index = TestExistInHistoryList(path);
		if(index!=-1){
			if(index != 0){
				for(int i=index;i>0;i--){
					history_list.set(i, history_list.get(i-1));
				}
				history_list.set(0, path);
			}
		}else{
			for(int i=4;i>0;i--){
				history_list.set(i, history_list.get(i-1));
			}
			history_list.set(0, path);
		}
		Editor edit = share.edit();
		edit.putString("song1", history_list.get(0));
		edit.putString("song2", history_list.get(1));
		edit.putString("song3", history_list.get(2));
		edit.putString("song4", history_list.get(3));
		edit.putString("song5", history_list.get(4));
		edit.commit();
	}

}
