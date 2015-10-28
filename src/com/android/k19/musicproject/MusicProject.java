package com.android.k19.musicproject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.k19.musicproject.dataservices.ListSong;
import com.android.k19.musicproject.models.Song;

public class MusicProject extends Activity {

	Cursor cursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_music_project);
        final Button Allsongs = (Button)findViewById(R.id.btnAllSong);
        final Button Album = (Button)findViewById(R.id.btnAlbum);
        final Button Artist = (Button)findViewById(R.id.btnArtists);
        final Button Playlist = (Button)findViewById(R.id.btnPlaylists);
        final Button History = (Button)findViewById(R.id.btnHistory);
        getdataContent();
        
        Allsongs.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MusicProject.this,ListAllSongs.class);
				startActivity(i);
				//finish();
			}
		});
        
        Album.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MusicProject.this,ListAlbums.class);
				startActivity(i);
			}
		});
        
        Artist.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MusicProject.this,ListArtists.class);
				startActivity(i);
			}
		});

        Playlist.setOnClickListener(new OnClickListener() {
	
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Playlists.class);
				startActivity(i);
			}
		});
		
        History.setOnClickListener(new OnClickListener() {
			
//			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
				startActivity(i);
				
			}
		});
        
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
			Intent i = new Intent(MusicProject.this,NowPlaying.class);
			startActivity(i);
			return true;
		}
    	return false;
    }
    
    
    public static boolean getdataContent()
    {
    	try {
    		System.gc();
    		ListSong.refesh();
//        	String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//
//        	String[] projection = {
//        	        MediaStore.Audio.Media.DATA,
//        	};
//        	String[] selectionArgs = null;
//    		String sortOrder = null;
//    		cursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);
//    		while(cursor.moveToNext())
//    		{
////    			if(cursor.getString(0).contains("mp3"))
////    			{
//    				ListSong.listAllSongs.add(new Song(cursor.getString(0)));
////    			}
//    		}
//    		cursor = managedQuery(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);
//    		while(cursor.moveToNext())
//    		{
//    			if(cursor.getString(0).contains("mp3"))
//    			{
//    				ListSong.listAllSongs.add(new Song(cursor.getString(0)));
//    			}
//    		}
//    		
    		File file = Environment.getRootDirectory().getAbsoluteFile();
    		getAllSonginLocalPhone(file);
    		file = Environment.getExternalStorageDirectory().getAbsoluteFile();
    		getAllSonginLocalPhone(file);
    		
    		file = new File("....//");
    		getAllSonginLocalPhone(file);
    		//end add list ALL songs
    		ArrayList<Song> list = ListSong.listAllSongs;
    		//add list Album
    		for(int i=0;i<ListSong.listAllSongs.size();i++)
    		{
    			Song temp = ListSong.listAllSongs.get(i);
    			ArrayList<Song> listsong;
    			if(temp.getAlbum()== "" || temp.getAlbum() == null)
    			{
    				if(ListSong.listAlbumName.contains("Unknown"))
    				{
    					listsong = ListSong.listAlbum.get("Unknown");
    					listsong.add(temp);
    					ListSong.listAlbum.remove("Unknown");
    					ListSong.listAlbum.put("Unknown", listsong);
    				}else
    				{
    					listsong = new ArrayList<Song>();
    					listsong.add(temp);
    					ListSong.listAlbumName.add("Unknown");
    					ListSong.listAlbum.put("Unknown",listsong);
    				}
    			}
    			else if(ListSong.listAlbum.containsKey(temp.getAlbum()))
    			{
    				listsong = ListSong.listAlbum.get(temp.getAlbum());
    				listsong.add(temp);
    				ListSong.listAlbum.remove(temp.getAlbum());
    				ListSong.listAlbum.put(temp.getAlbum(),listsong);
    			}else
    			{
    				listsong = new ArrayList<Song>();
    				listsong.add(temp);
    				ListSong.listAlbum.put(temp.getAlbum(),listsong);
    				ListSong.listAlbumName.add(temp.getAlbum());
    			}
    		}
    		
    		//add list Artist
    		for(int i=0;i<ListSong.listAllSongs.size();i++)
    		{
    			Song temp = ListSong.listAllSongs.get(i);
    			ArrayList<Song> listsong;
    			if(temp.getArtist()== "" || temp.getArtist() == null)
    			{
    				if(ListSong.listArtistName.contains("Unknown"))
    				{
    					listsong = ListSong.listArtist.get("Unknown");
    					listsong.add(temp);
    					ListSong.listArtist.remove("Unknown");
    					ListSong.listArtist.put("Unknown", listsong);
    				}else
    				{
    					listsong = new ArrayList<Song>();
    					listsong.add(temp);
    					ListSong.listArtistName.add("Unknown");
    					ListSong.listArtist.put("Unknown",listsong);
    				}
    			}
    			else if(ListSong.listArtist.containsKey(temp.getArtist()))
    			{
    				listsong = ListSong.listArtist.get(temp.getArtist());
    				listsong.add(temp);
    				ListSong.listArtist.remove(temp.getArtist());
    				ListSong.listArtist.put(temp.getArtist(),listsong);
    			}else
    			{
    				listsong = new ArrayList<Song>();
    				listsong.add(temp);
    				ListSong.listArtist.put(temp.getArtist(),listsong);
    				ListSong.listArtistName.add(temp.getArtist());
    			}
    		}
    		return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}  	
    	
    }
    static void getAllSonginLocalPhone(File file)
    {
    	if(file.isFile())
        {
		    if(file.getName().endsWith(".mp3")||file.getName().endsWith(".MP3"))
		    {
		    	ListSong.listAllSongs.add(new Song(file.getAbsolutePath()));
		    }
        }
        else if (file.isDirectory()) {
        	File[] listOfFiles = file.listFiles();
	        if(listOfFiles!=null) {
	        for (int i = 0; i < listOfFiles.length; i++)
	        	getAllSonginLocalPhone(listOfFiles[i]);
	        }
        }
    }

}
