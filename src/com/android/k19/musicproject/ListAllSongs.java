package com.android.k19.musicproject;

import java.io.File;


import com.android.k19.musicproject.application.MusicApplication;
import com.android.k19.musicproject.dataservices.ListSong;
import com.android.k19.musicproject.dataservices.listSongAdapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

import android.os.Bundle;
import android.text.Editable;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ListAllSongs extends Activity{
	
	listSongAdapter dataAdapter = null;
	final int remove =1;
	 final int delete =2;
	 final int addToPlaylist =3;
	 int pos;
	 ListView listview;
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_all_song_interface);
		
		final EditText inputsearch = (EditText)findViewById(R.id.inputSearch);
		
		listview = (ListView)findViewById(R.id.ListSongView);
		dataAdapter = new listSongAdapter(this,R.layout.list_song_view, ListSong.listAllSongs);
//		dataAdapter.notifyDataSetChanged();
		listview.setAdapter(dataAdapter);
		inputsearch.addTextChangedListener(new DelayedTextWatcher(300) {
			
			@Override
			public void afterTextChangedDelayed(Editable s) {
				// TODO Auto-generated method stub
				/*
				EditText inputsearch = (EditText)findViewById(R.id.inputSearch);
				String input = inputsearch.getText().toString();
				Toast.makeText(getApplicationContext(),	input, Toast.LENGTH_SHORT).show();
				*/
				ListAllSongs.this.dataAdapter.getFilter().filter(s);
			}
		});
		/*
		inputsearch.addTextChangedListener(new TextWatcher() {
		
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				ListAllSongs.this.dataAdapter.getFilter().filter(s);
				
	
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		*/
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				MusicApplication musicapp = MusicApplication.getInstance();
				musicapp.SetNewPlaying(ListSong.listAllSongs,position);
				Intent i =new Intent(ListAllSongs.this,NowPlaying.class);
				startActivity(i);
			}
			
		});
		
		registerForContextMenu(listview);
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_music_project, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	switch (item.getItemId()) {
		case R.id.menu_NowPlaying:
			Intent i = new Intent(ListAllSongs.this,NowPlaying.class);
			startActivity(i);
			return true;
		}
    	return false;
    }
	 @Override
	 public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
	           
	  menu.add(Menu.NONE, delete, Menu.NONE, "Delete");
	  menu.add(Menu.NONE, addToPlaylist, Menu.NONE, "Add to Playlist");
	 }
	 @Override
	 public boolean onContextItemSelected(MenuItem item) {
	 
	      AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	    //  Long id = getListAdapter().getItemId(info.position);/*what item was selected is ListView*/
	     pos = info.position;
	      switch (item.getItemId()) {
	             
	             case delete:
	            	 new AlertDialog.Builder(ListAllSongs.this)
	     			.setTitle("Delete Song")
	     			.setMessage("Are you sure?")
	     			.setPositiveButton("Yes", new OnClickListener() {
	     				
	     				@Override
	     				public void onClick(DialogInterface dialog, int which) {
	     					// TODO Auto-generated method stub
	     					try{
	     						

	     						File file = new File( ListSong.listAllSongs.get(pos).getPath());
	     						file.delete();
	     						Toast.makeText(getApplicationContext(), "Delete Song " + pos,
	     								Toast.LENGTH_SHORT).show();

//	     						
//     						finish();
	     						}catch(Exception e){}
	     					Intent i = new Intent(getApplicationContext(),ListAllSongs.class);
     						startActivity(i);
	     				}
	     				
	     					
	     			})
	     			.setNegativeButton("No", new OnClickListener() {
	     				
	     				@Override
	     				public void onClick(DialogInterface dialog, int which) {
	     					// TODO Auto-generated method stub
	     					
	     				}
	     			})
	     			.show();
	            		
	            	 return(true);
	             case addToPlaylist:
	            	 Intent i = new Intent(ListAllSongs.this, PlaylistDialog.class);
	            	 i.putExtra("mUrlSong", ListSong.listAllSongs.get(pos).getPath());
	            	 startActivity(i);
	            	 return true;
	      }
	  return(super.onOptionsItemSelected(item));
	}
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MusicProject.getdataContent();
		listSongAdapter adapter = new listSongAdapter(this,R.layout.list_song_view, ListSong.listAllSongs);
//		adapter.notifyDataSetChanged();
//		adapter.
		listview.setAdapter(adapter);
	}
}
