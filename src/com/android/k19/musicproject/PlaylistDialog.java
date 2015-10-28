package com.android.k19.musicproject;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.k19.musicproject.dataservices.PlaylistAdapter;
import com.android.k19.musicproject.dataservices.PlaylistManager;
import com.android.k19.musicproject.models.Playlist;

public class PlaylistDialog extends Activity {

	ArrayList<Playlist> arrayPlaylistDialog;
	PlaylistAdapter playlistAdapter;
	PlaylistManager playlistManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_dialog);
        
        playlistManager = new PlaylistManager();
        
        final String mUrlSong = getIntent().getExtras().getString("mUrlSong");
        ListView listViewPlaylistDialog = (ListView)findViewById(R.id.listPlaylistsDialog);
        arrayPlaylistDialog = new ArrayList<Playlist>();
        playlistAdapter = new PlaylistAdapter(this, arrayPlaylistDialog);
        listViewPlaylistDialog.setAdapter(playlistAdapter);
        
        listViewPlaylistDialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				boolean isAdded = playlistManager.addSongToPlaylist(mUrlSong, arrayPlaylistDialog.get(arg2).getUrl());
				if(isAdded)
					Toast.makeText(PlaylistDialog.this, "Added to playlist: "+arrayPlaylistDialog.get(arg2).getName(), Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(PlaylistDialog.this, "Already exists "+mUrlSong.substring(mUrlSong.lastIndexOf("/")).replace("/", "").replace(".mp3", "")+" in playlist: "+arrayPlaylistDialog.get(arg2).getName(), Toast.LENGTH_SHORT).show();
				}
		});
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	arrayPlaylistDialog.clear();
    	arrayPlaylistDialog.addAll(playlistManager.loadPlaylist());
    	playlistAdapter.notifyDataSetChanged();
    }
}
