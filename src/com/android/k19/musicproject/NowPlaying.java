/**
 * 
 */
package com.android.k19.musicproject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;


import com.android.k19.musicproject.application.MusicApplication;
import com.android.k19.musicproject.dataservices.ListSong;
import com.android.k19.musicproject.dataservices.Service_Playing;
import com.android.k19.musicproject.models.Song;

/**
 * @author Trung Nguyen
 *
 */
public class NowPlaying extends Activity{
	private ArrayList<Song> list_song;
	private int index;
	private int next;
	private int previous;
	private int repeat;
	private boolean shuffle;
	private Timer tm;
	private long second;
	private boolean playing;
	private boolean timer_stop;
	private Adapter adapter;
	private View first_view_list_view;
	private ArrayList<Song> all_song_sdcard;
	
	//Control
	private TextView tv_name;
	private TextView tv_artist;
	private TextView tv_album;
	private TextView tv_duration;
	private TextView tv_time;
	private ImageView im;
	private SeekBar seek;
	private ImageButton bt_play;
	private ImageButton bt_previous;
	private ImageButton bt_next;
	private ImageButton bt_repeat;
	private ImageButton bt_shuffle;
	private SlidingDrawer sliding;
	private ImageButton bt_handle;
	private ListView list_view;
	
	//Notification
	private NotificationManager nMN;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_now_playing);
		try{
			FirstValue();
			DemoData();
			if(MusicApplication.getInstance().getTm()!=null){
				MusicApplication.getInstance().getTm().cancel();
			}
			GetControl();
			SetControl();
			if(!MusicApplication.getInstance().isPlaying_come_back()){
				try{
					stopService(new Intent(this,Service_Playing.class));
				}catch(Exception ex){
					
				}
				MusicApplication.getInstance().setPlaying_come_back(true);
				Drawable dr = getResources().getDrawable(R.drawable.btn_pause);
				bt_play.setImageDrawable(dr);
				StartService();
			}else{
				if(playing){
					StartService();
				}
			}
			//Nofitication
			nMN = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			try {
				nMN.cancel(1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			registerForContextMenu(list_view);
		}catch(Exception ex){
			MusicApplication.getInstance().SetNewPlaying(all_song_sdcard,0);
			Intent i = new Intent(this, NowPlaying.class);
			startActivity(i);
		}
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_now_playing_menu, menu);
        return true;
    }
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
		Intent i;
    	switch (item.getItemId()) {
			case R.id.it_home:
				i = new Intent(NowPlaying.this,MusicProject.class);
				startActivity(i);
				break;
			case R.id.it_all_song:
				i = new Intent(NowPlaying.this,ListAllSongs.class);
				startActivity(i);
				break;
			case R.id.it_list_song_group_by_artist:
				i = new Intent(NowPlaying.this,ListArtists.class);
				startActivity(i);
				break;
			case R.id.it_list_song_group_by_album:
				i = new Intent(NowPlaying.this,ListAlbums.class);
				startActivity(i);
				break;
			case R.id.it_list_playlist:
				i = new Intent(NowPlaying.this,Playlists.class);
				startActivity(i);
				break;
			case R.id.it_history:
				i = new Intent(NowPlaying.this,HistoryActivity.class);
				startActivity(i);
				break;
		}
    	return false;
    }
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {          
	  menu.add(Menu.NONE, 1, Menu.NONE, "Add to Playlist");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int pos = info.position;
		switch(item.getItemId()){
			case 1:
				Intent i = new Intent(NowPlaying.this, PlaylistDialog.class);
           	 	i.putExtra("mUrlSong", ListSong.listAllSongs.get(pos).getPath());
           	 	startActivity(i);
           	 	return true;
		}
		return(super.onOptionsItemSelected(item));
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MusicApplication.getInstance().setIndex(index);
		MusicApplication.getInstance().setNext(next);
		MusicApplication.getInstance().setPrevious(previous);
		MusicApplication.getInstance().setRepeat(repeat);
		MusicApplication.getInstance().setShuffle(shuffle);
		MusicApplication.getInstance().setSecond(second);
		MusicApplication.getInstance().setPlaying(playing);
		MusicApplication.getInstance().setTimer_stop(timer_stop);
		MusicApplication.getInstance().setList_song(list_song);
		MusicApplication.getInstance().setTm(tm);
		if(playing){
			SetNofitication();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Toast.makeText(getApplicationContext(), "Resume", 1000).show();
		try {
			nMN.cancel(1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try{
			TestExisting();
		}catch(Exception ex){
			
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MusicApplication.getInstance().setIndex(index);
		MusicApplication.getInstance().setNext(next);
		MusicApplication.getInstance().setPrevious(previous);
		MusicApplication.getInstance().setRepeat(repeat);
		MusicApplication.getInstance().setShuffle(shuffle);
		MusicApplication.getInstance().setSecond(second);
		MusicApplication.getInstance().setPlaying(playing);
		MusicApplication.getInstance().setTimer_stop(timer_stop);
		MusicApplication.getInstance().setList_song(list_song);
		MusicApplication.getInstance().setTm(tm);
		if(playing){
			SetNofitication();
		}
	}
	//Select all song in sdcard (.mp3)
	private void AllSongSDCard(File aFile) {
        if(aFile.isFile())
        {
		    if(aFile.getName().endsWith(".mp3")||aFile.getName().endsWith(".MP3"))
		    {
		    	all_song_sdcard.add(new Song(aFile.getAbsolutePath()));
		    }
        }
        else if (aFile.isDirectory()) {
        	File[] listOfFiles = aFile.listFiles();
	        if(listOfFiles!=null) {
	        for (int i = 0; i < listOfFiles.length; i++)
	        	AllSongSDCard(listOfFiles[i]);
	        }
        }
    }
	//Set First Value
	private void FirstValue(){
		list_song = MusicApplication.getInstance().getList_song();
		index = MusicApplication.getInstance().getIndex();
		next = MusicApplication.getInstance().getNext();
		previous = MusicApplication.getInstance().getPrevious();
		repeat = MusicApplication.getInstance().getRepeat();
		shuffle = MusicApplication.getInstance().isShuffle();
		tm = new Timer();
		second = MusicApplication.getInstance().getSecond();
		playing = MusicApplication.getInstance().isPlaying();
		timer_stop = MusicApplication.getInstance().isTimer_stop();
		all_song_sdcard = new ArrayList<Song>();
	}
	//DemoData
	private void DemoData(){
		AllSongSDCard(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
		if(list_song.size() == 0)
		{
			list_song = all_song_sdcard;
		}
	}
	//Test existing file when resume
	private void TestExisting(){
		ArrayList<Song> list_song_remove = new ArrayList<Song>();
		int now = index;
		boolean index_exist = true;
		boolean change = false;
		for(int i=0;i<list_song.size();i++){
			boolean exist = new File(list_song.get(i).getPath()).exists();
			if(!exist){
				list_song_remove.add(list_song.get(i));
				if(i == index){
					index_exist = false;
				}else{
					if(i<index){
						now--;
					}
				}
			}
		}
		if(list_song_remove.size()>0){
			for(int j=0;j<list_song_remove.size();j++){
				list_song.remove(list_song_remove.get(j));
			}
			change = true;
		}
		if(change)
		{
			if(list_song.size()>0){
				if(index != now){
					index = now;
				}
				if(!index_exist){
					try {
						stopService(new Intent(NowPlaying.this,Service_Playing.class));
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(index>list_song.size()-1){
						index = list_song.size()-1;
					}
					playing = false;
					timer_stop = true;
					second = 0;
					tv_time.setText("00:00");
					seek.setProgress(0);
				}
				if(index>list_song.size()-1){
					index = list_song.size()-1;
				}
				SetValueControl();
				adapter = new Adapter(getApplicationContext(),R.layout.item_list_nowplaying,list_song);
				list_view.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				list_view.setSelection(index);
			}else{
				try {
					stopService(new Intent(NowPlaying.this,Service_Playing.class));
				} catch (Exception e) {
					// TODO: handle exception
				}
				list_song = all_song_sdcard;
				if(all_song_sdcard.size()>0)
				{
					index = 0;
					next = 1;
					previous = -1;
					repeat = 0;
					shuffle = false;
					second = 0;
					timer_stop = true;
					playing = false;
					tv_time.setText("00:00");
					seek.setProgress(0);
					try{
						SetValueControl();
					}catch(Exception ex){
						
					}
					adapter = new Adapter(getApplicationContext(),R.layout.item_list_nowplaying,list_song);
					list_view.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					list_view.setSelection(index);
				}else{
					Toast.makeText(getApplicationContext(), "Don't have song in your phone!", Toast.LENGTH_LONG).show();
					Intent i = new Intent(NowPlaying.this,MusicProject.class);
					startActivity(i);
				}
			}
		}
	}
	//Get control
	private void GetControl(){
		tv_name = (TextView)findViewById(R.id.tv_name_song);
		tv_artist = (TextView)findViewById(R.id.tv_artist);
		tv_album = (TextView)findViewById(R.id.tv_album);
		tv_duration = (TextView)findViewById(R.id.tv_total_time);
		tv_time = (TextView)findViewById(R.id.tv_time_playing);
		im = (ImageView)findViewById(R.id.im_picture_song);
		seek = (SeekBar)findViewById(R.id.seek_bar);
		bt_play = (ImageButton)findViewById(R.id.imbtn_play);
		//Control listener play
		bt_play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(playing){	
					timer_stop = true;
					Intent i = setIntent();
					stopService(i);
					bt_play.setImageDrawable(getResources().getDrawable(R.drawable.btn_play));
					playing = false;
				}else{
					timer_stop = false;
					playing = true;
					stopService(new Intent(NowPlaying.this,Service_Playing.class));
					Bundle b = new Bundle();
					b.putString("path", list_song.get(index).getPath());
					b.putLong("seek_time", second);
					Intent i = new Intent(NowPlaying.this,Service_Playing.class);
					i.putExtras(b);
					startService(i);
					list_view.setAdapter(adapter);
					list_view.setSelection(index);
					bt_play.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause));
				}
			}
		});
		bt_previous = (ImageButton)findViewById(R.id.imbtn_previous);
		bt_next = (ImageButton)findViewById(R.id.imbtn_next);
		bt_shuffle = (ImageButton)findViewById(R.id.imbtn_shuffle);
		//Control listener shuffle
		bt_shuffle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!shuffle){
					shuffle = true;
					bt_shuffle.setImageDrawable(getResources().getDrawable(R.drawable.shuffle));
				}else{
					shuffle = false;
					bt_shuffle.setImageDrawable(getResources().getDrawable(R.drawable.cancle_shuffle));
				}
			}
		});
		bt_repeat = (ImageButton)findViewById(R.id.imbtn_repeat);
		//Control listener repeat
		bt_repeat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(repeat){
				case 0:
					repeat++;
					bt_repeat.setImageDrawable(getResources().getDrawable(R.drawable.repeat));
					break;
				case 1:
					repeat++;
					bt_repeat.setImageDrawable(getResources().getDrawable(R.drawable.repeat_playlist));
					break;
				case 2:
					repeat = 0;
					bt_repeat.setImageDrawable(getResources().getDrawable(R.drawable.cancle_repeat));
					break;
				}
			}
		});
		//Control previous button
		bt_previous.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(shuffle){
					index = Rand(list_song.size());
					next = Rand(list_song.size());
					if(next == index){
						if(index == list_song.size()-1){
							next = 0;
						}else{
							next = index+1;
						}
					}
					previous = Rand(list_song.size());
					if(previous == index){
						if(index == 0){
							previous = list_song.size()-1;
						}else{
							previous = index-1;
						}
					}
				}else{
					if(repeat > 0){
						switch(repeat){
						case 1:
							next = index;
							previous = index;
							break;
						case 2:
							if(index == 0){
								previous = list_song.size()-2;
								index = list_song.size()-1;
								next = 0;
							}else{
								index--;
								previous = index-1;
								next = index+1;
							}
							break;
						}
					}else{
						if(index == 0){
							timer_stop = true;
							Intent i = setIntent();
							stopService(i);
							playing = false;
							bt_play.setImageDrawable(getResources().getDrawable(R.drawable.btn_play));
						}else{
							index--;
							previous = index-1;
							next = index+1;
						}
					}
				}
				
				if(playing){
					Intent i = setIntent();
					stopService(i);
					startService(i);
				}
				tv_time.setText("00:00");
				seek.setProgress(0);
				SetValueControl();
				list_view.setAdapter(adapter);
				list_view.setSelection(index);
			}
		});
		//Control next button
		bt_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(shuffle){
					index = Rand(list_song.size());
					next = Rand(list_song.size());
					if(next == index){
						if(index == list_song.size()-1){
							next = 0;
						}else{
							next = index+1;
						}
					}
					previous = Rand(list_song.size());
					if(previous == index){
						if(index == 0){
							previous = list_song.size()-1;
						}else{
							previous = index-1;
						}
					}
				}else{
					if(repeat > 0){
						switch(repeat){
						case 1:
							next = index;
							previous = index;
							break;
						case 2:
							if(index == list_song.size()-1){
								previous = index;
								index = 0;
								next = 1;
							}else{
								index++;
								previous = index--;
								next = index++;
							}
							break;
						}
					}else{
						if(index == list_song.size()-1){
							timer_stop = true;
							Intent i = setIntent();
							stopService(i);
							playing = false;
							bt_play.setImageDrawable(getResources().getDrawable(R.drawable.btn_play));
						}else{
							index++;
							previous = index--;
							next = index++;
						}
					}
				}
				if(playing){
					Intent i = setIntent();
					stopService(i);
					startService(i);
				}
				tv_time.setText("00:00");
				seek.setProgress(0);
				SetValueControl();
				list_view.setAdapter(adapter);
				list_view.setSelection(index);
			}
		});
		//Sliding
		sliding = (SlidingDrawer)findViewById(R.id.slidingDrawer1);
		sliding.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				bt_handle.setImageResource(R.drawable.show_list_custom_change);
				bt_play.setClickable(false);
				bt_next.setClickable(false);
				bt_previous.setClickable(false);
				bt_repeat.setClickable(false);
				bt_shuffle.setClickable(false);
				seek.setEnabled(false);
			}
		});
		sliding.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				bt_handle.setImageResource(R.drawable.show_list_custom);
				bt_play.setClickable(true);
				bt_next.setClickable(true);
				bt_previous.setClickable(true);
				bt_repeat.setClickable(true);
				bt_shuffle.setClickable(true);
				seek.setEnabled(true);;
			}
		});
		//List View
		list_view = (ListView)findViewById(R.id.lv_list_song);
		adapter = new Adapter(getApplicationContext(),R.layout.item_list_nowplaying,list_song);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	private View lastSelectedView = null;

            public void clearSelection()
            {
                if(lastSelectedView != null)
                {
                	lastSelectedView.setBackgroundColor(Color.TRANSPARENT);
                }
                if(first_view_list_view != null){
                	first_view_list_view.setBackgroundColor(Color.TRANSPARENT);
                }
            }

			@Override
			public void onItemClick(AdapterView arg0, View view,
                                           int position, long id) {
				// user clicked a list item, make it "selected"
				clearSelection();
				lastSelectedView = view;
				view.setBackgroundColor(Color.YELLOW);
				if(position!=index){
					index = position;
					if(shuffle){
						next = Rand(list_song.size());
						if(next == index){
							if(index == list_song.size()-1){
								next = 0;
							}else{
								next = index+1;
							}
						}
						previous = Rand(list_song.size());
						if(previous == index){
							if(index == 0){
								previous = list_song.size()-1;
							}else{
								previous = index-1;
							}
						}
					}else{
						if(repeat > 0){
							switch(repeat){
							case 1:
								next = index;
								previous = index;
								break;
							case 2:
								if(index == 0){
									previous = list_song.size()-1;
									next = 1;
								}else{
									previous = index-1;
									next = index+1;
								}
								break;
							}
						}else{
							if(index == 0){
								previous = list_song.size()-1;
								next = 1;
							}else{
								if(index == list_song.size()-1){
									previous = index - 1;
									next = 0;
								}else{
									previous = index - 1;
									next = index + 1;
								}
							}
						}
					}
					if(playing){
						Intent i = setIntent();
						stopService(i);
						startService(i);
					}
					tv_time.setText("00:00");
					seek.setProgress(0);
					SetValueControl();
				}
			}
        });
        //Button handle
        bt_handle = (ImageButton)findViewById(R.id.handle);
	}
	//Class Adapter for List View
	class Adapter extends ArrayAdapter<Song>{
		private Context mcontext;
		private int resourceid;
		private List<Song> items;
		
		public Adapter(Context context, int textViewResourceId,
				List<Song> objects) {
			super(context, textViewResourceId, objects);
			mcontext = context;
			resourceid = textViewResourceId;
			items = objects;
			// TODO Auto-generated constructor stub
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			if(view == null)
			{
				LayoutInflater li = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = li.inflate(resourceid, null);
			}
			if(items.size()>0){
				Song item = items.get(position);
				if(item!=null){
					ImageView img = (ImageView)view.findViewById(R.id.im_song_in_list);
					TextView tv_name = (TextView)view.findViewById(R.id.tv_name_song_in_list);
					TextView tv_duration = (TextView)view.findViewById(R.id.tv_duration_song_in_list);
					tv_name.setText(item.getName());
					tv_duration.setText(item.getLength());
					try {
						Bitmap bm = BitmapFactory.decodeByteArray(item.getPicture(), 0, item.getPicture().length);
						if(bm!=null)
						{
							img.setImageBitmap(bm);
						}else{
							img.setImageResource(R.drawable.ic_launcher);
						}
					} catch (Exception e) {
						// TODO: handle exception
						img.setImageResource(R.drawable.ic_launcher);
					}
					if(position == index){
						view.setBackgroundColor(Color.YELLOW);
						first_view_list_view = view;
					}else{
						view.setBackgroundColor(Color.TRANSPARENT);
					}
				}
			}
			return view;	
		}
	}
	public void StartService(){
		Bundle b = new Bundle();
		b.putString("path", list_song.get(index).getPath());
		Intent i = new Intent(this,Service_Playing.class);
		i.putExtras(b);
		startService(i);
		playing = true;
	}
	private void SetValueControl(){ 
		tv_name.setText(list_song.get(index).getName());
		try{
			tv_artist.setText(list_song.get(index).getArtist());
		}catch(Exception e){
			tv_artist.setText("Unkhown artist");
		}
		try{
			tv_album.setText(list_song.get(index).getAlbum());
		}catch(Exception e){
			tv_album.setText("Unkhown album");
		}
		tv_album.setText(list_song.get(index).getAlbum());
		tv_name.startAnimation((Animation)AnimationUtils.loadAnimation(this, R.anim.translate));
		tv_duration.setText(list_song.get(index).getLength());
		tv_time.setText(ChangeFormatSecond(second));
		try{
			Bitmap bm = BitmapFactory.decodeByteArray(list_song.get(index).getPicture(), 0, list_song.get(index).getPicture().length);
			if(bm!=null)
			{
				im.setImageBitmap(bm);
			}else{
				im.setImageResource(R.drawable.ic_launcher);
			}
		}catch(Exception ex){
			im.setImageResource(R.drawable.ic_launcher);
		}
		if(playing){
			bt_play.setImageResource(R.drawable.btn_pause);
		}else{
			bt_play.setImageResource(R.drawable.btn_play);
		}
		switch(repeat){
			case 0:
				bt_repeat.setImageResource(R.drawable.cancle_repeat);
				break;
			case 1:
				bt_repeat.setImageResource(R.drawable.repeat);
				break;
			case 2:
				bt_repeat.setImageResource(R.drawable.repeat_playlist);
				break;
		}
		if(shuffle){
			bt_shuffle.setImageResource(R.drawable.shuffle);
		}else{
			bt_shuffle.setImageResource(R.drawable.cancle_shuffle);
		}
		//Seekbar
		String length_song = list_song.get(index).getLength();
		String minute_length_song = length_song.substring(0, length_song.lastIndexOf(":"));
		String second_length_song = length_song.substring(length_song.lastIndexOf(":")+1);
		int minute_song = new Integer(minute_length_song);
		int second_song = new Integer(second_length_song);
		seek.setMax(minute_song*60 + second_song);
		seek.setProgress(Integer.valueOf(String.valueOf(second)));
	}
	private void SetControl(){
		//Set control value
		SetValueControl();
		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				if(playing)
				{
					stopService(new Intent(NowPlaying.this,Service_Playing.class));
					Bundle b = new Bundle();
					b.putString("path", list_song.get(index).getPath());
					b.putLong("seek_time", second);
					Intent i = new Intent(NowPlaying.this,Service_Playing.class);
					i.putExtras(b);
					startService(i);
					playing = true;
				}
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				second = Long.parseLong(String.valueOf(progress));
				String length = ChangeFormatSecond(second);
				if(length.equals(list_song.get(index).getLength())){
					tv_time.setText("00:00");
					seek.setProgress(0);
				}else{
					seek.setProgress(new Integer(String.valueOf(second)));
					tv_time.setText(length);
				}
			}
		});
		//Timer component
		tm.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NowPlaying.this.runOnUiThread(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						if(!timer_stop)
						{
							second++;
							String seconds = String.valueOf(second % 60);
							String minutes = String.valueOf(second / 60);
							String length = ChangeFormatSecond(second);
							if(length.equals(list_song.get(index).getLength())){
								if(shuffle){
									index = Rand(list_song.size());
									next = Rand(list_song.size());
									if(next == index){
										if(index == list_song.size()-1){
											next = 0;
										}else{
											next = index+1;
										}
									}
									previous = Rand(list_song.size());
									if(previous == index){
										if(index == 0){
											previous = list_song.size()-1;
										}else{
											previous = index-1;
										}
									}
									Intent i = setIntent();
									try{
										stopService(i);
									}catch(Exception ex){
										
									}
									startService(i);
									SetNofitication();
									list_view.setAdapter(adapter);
									list_view.setSelection(index);
									playing = true;
								}else{
									if(repeat > 0){
										switch(repeat){
										case 1:
											next = index;
											previous = index;
											break;
										case 2:
											if(index == list_song.size()-1){
												previous = index;
												index = 0;
												next = 1;
											}else{
												index++;
												previous = index-1;
												next = index+1;
											}
											break;
										}
										Intent i = setIntent();
										try{
											stopService(i);
										}catch(Exception ex){
											
										}
										startService(i);
										SetNofitication();
										list_view.setAdapter(adapter);
										list_view.setSelection(index);
										playing = true;
									}else{
										if(index == list_song.size()-1){
											timer_stop = true;
											Intent i = setIntent();
											try{
												stopService(i);
												nMN.cancel(1);
											}catch(Exception ex){
												
											}
											playing = false;
											bt_play.setImageDrawable(getResources().getDrawable(R.drawable.btn_play));
										}else{
											index++;
											previous = index-1;
											next = index+1;
											Intent i = setIntent();
											try{
												stopService(i);
											}catch(Exception ex){
												
											}
											startService(i);
											SetNofitication();
											list_view.setAdapter(adapter);
											list_view.setSelection(index);
											playing = true;
										}
									}
								}
								tv_time.setText("00:00");
								seek.setProgress(0);
								SetValueControl();
							}else{
								seek.setProgress(new Integer(String.valueOf(second)));
								tv_time.setText(length);
							}
						}
					}
				});
			}
		},0,1000);
		
	}
	private String ChangeFormatSecond(long second){
		String seconds = String.valueOf(second % 60);
		String minutes = String.valueOf(second / 60);
		String length = "";
		if (seconds.length() == 1) {
			if(minutes.length() == 1){
				length = "0" + minutes + ":0" + seconds;
			}else{
				length = minutes + ":0" + seconds;
			}
		}else {
			if(minutes.length() == 1){
				length = "0" + minutes + ":" + seconds;
			}else{
				length = minutes + ":" + seconds;
			}
		}
		return length;
	}
	private int Rand(int size){
    	Random ra = new Random();
		int value = 0;
		do
		{
			value = ra.nextInt(size);
		}while(value == index);
		return value;
    }
	private Intent setIntent(){
		Intent i = new Intent(NowPlaying.this,Service_Playing.class);
		Bundle b = new Bundle();
		b.putString("path", list_song.get(index).getPath());
		i.putExtras(b);
		return i;
	}
	private void SetNofitication(){
		nMN = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification myNotification = new Notification(R.drawable.up, "NOW PLAYING", System.currentTimeMillis());
		Context context = getApplicationContext();
		String notificationTitle = list_song.get(index).getName().toUpperCase();
		String notificationText = list_song.get(index).getLength();
		Intent myIntent = new Intent(getApplicationContext(), NowPlaying.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(NowPlaying.this, 0, myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
		nMN.notify(1, myNotification);        
	}
}
