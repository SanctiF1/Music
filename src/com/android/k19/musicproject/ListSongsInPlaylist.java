package com.android.k19.musicproject;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.k19.musicproject.application.MusicApplication;
import com.android.k19.musicproject.dataservices.ListSongInPLaylistAdapter;
import com.android.k19.musicproject.dataservices.PlaylistManager;
import com.android.k19.musicproject.models.Song;

public class ListSongsInPlaylist extends Activity {

	ListView listSongsInPlaylist;
	ListSongInPLaylistAdapter listSongsAdapter;
	String PlaylistUrl;
	ArrayList<Song> arrayListSongs;
	PlaylistManager playlistManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.activity_list_songs_in_playlist);
        
        playlistManager = new PlaylistManager();
        String PlaylistName = getIntent().getExtras().getString("PlaylistName");
        PlaylistUrl = getIntent().getExtras().getString("PlaylistUrl");
        setTitle("Playlist: " + PlaylistName);
        listSongsInPlaylist = (ListView)findViewById(R.id.listSongsInPlaylist);
        arrayListSongs = new ArrayList<Song>();
        listSongsAdapter = new ListSongInPLaylistAdapter(this, arrayListSongs);
        listSongsInPlaylist.setAdapter(listSongsAdapter);
        
        listSongsInPlaylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				MusicApplication.getInstance().SetNewPlaying(arrayListSongs, arg2);
				Intent i = new Intent(ListSongsInPlaylist.this, NowPlaying.class);
				startActivity(i);
			}
		});
        listSongsInPlaylist.setOnItemLongClickListener(new OnItemLongClickListener() {

        	int location;
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				location = arg2;
				new AlertDialog.Builder(ListSongsInPlaylist.this)
				.setTitle("Remove song: " + arrayListSongs.get(location).getName())
				.setMessage("Are you sure?")
				.setPositiveButton("Yes", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						playlistManager.removeSongInPlaylist(arrayListSongs.get(location).getPath(), PlaylistUrl);
						arrayListSongs.remove(location);
						listSongsAdapter.notifyDataSetChanged();
						Toast.makeText(ListSongsInPlaylist.this, arrayListSongs.get(location).getName(), Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("No", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();
				return false;
			}
		});
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	playlistManager.checkSongsExist(PlaylistUrl);
    	arrayListSongs.clear();
    	arrayListSongs.addAll(playlistManager.loadSongsInPlaylist(PlaylistUrl));
    	listSongsAdapter.notifyDataSetChanged();
    	
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
			Intent i = new Intent(ListSongsInPlaylist.this,NowPlaying.class);
			startActivity(i);
			return true;
		}
    	return false;
    }
}
