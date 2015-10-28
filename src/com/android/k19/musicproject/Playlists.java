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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.k19.musicproject.dataservices.PlaylistAdapter;
import com.android.k19.musicproject.dataservices.PlaylistManager;
import com.android.k19.musicproject.models.Playlist;

public class Playlists extends Activity {

	ArrayList<Playlist> arrayPlaylist;
	PlaylistAdapter playlistAdapter;
	PlaylistManager playlistManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        playlistManager = new PlaylistManager();
        ListView listViewPlaylist = (ListView)findViewById(R.id.listPlaylists);
        arrayPlaylist = new ArrayList<Playlist>();
        playlistAdapter = new PlaylistAdapter(this, arrayPlaylist);
        listViewPlaylist.setAdapter(playlistAdapter);
        
        listViewPlaylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Playlists.this, ListSongsInPlaylist.class);
				i.putExtra("PlaylistName", arrayPlaylist.get(arg2).getName());
				i.putExtra("PlaylistUrl", arrayPlaylist.get(arg2).getUrl());
				startActivity(i);
			}
		});
        
        listViewPlaylist.setOnItemLongClickListener(new OnItemLongClickListener() {

        	int location;
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				location = arg2;
				new AlertDialog.Builder(Playlists.this)
				.setTitle("Delete Playlist " + arrayPlaylist.get(arg2).getName())
				.setMessage("Are you sure?")
				.setPositiveButton("Yes", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						playlistManager.deleteFile(arrayPlaylist.get(location).getUrl());
						Toast.makeText(getApplicationContext(), "Removed Playlist: "+arrayPlaylist.get(location).getName(), Toast.LENGTH_SHORT).show();
						arrayPlaylist.remove(location);
						playlistAdapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton("No", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				})
				.show();
				return false;
			}
		});
    }

    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	arrayPlaylist.clear();
    	arrayPlaylist.addAll(playlistManager.loadPlaylist());
    	playlistAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_playlists, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	switch (item.getItemId()) {
		case R.id.menu_AddPlaylist:
			AlertDialog.Builder b = new AlertDialog.Builder(this);
	        final EditText input = new EditText(this);
	        input.setSingleLine();
	        b.setTitle("New Playlist");
	        b.setMessage("Create New Playlist");
	        b.setView(input);
	        b.setPositiveButton("Create", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(input.getText().toString().trim().equals(""))
						Toast.makeText(Playlists.this, "Enter playlist name", Toast.LENGTH_LONG).show();
					else
					{
						Playlist p = playlistManager.createNewPlaylist(input.getText().toString().trim());
						if(p != null)
						{
							arrayPlaylist.add(p);
							Toast.makeText(Playlists.this, "Created Playlist: "+input.getText().toString().trim(), Toast.LENGTH_SHORT).show();
							playlistAdapter.notifyDataSetChanged();
						}
						else
							Toast.makeText(getApplicationContext(), input.getText().toString().trim()+" Already exists playlist: "+input.getText(), Toast.LENGTH_SHORT).show();
					}
				}
			});
	        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface arg0, int arg1) {
	                return;
	                }});
	        b.show();
	        return true;
		case R.id.it_now_playing:
			Intent i = new Intent(Playlists.this,NowPlaying.class);
			startActivity(i);
			return true;
		}
    	return false;
    }
}
