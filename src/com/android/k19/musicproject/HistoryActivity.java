package com.android.k19.musicproject;

import java.io.File;
import java.util.ArrayList;
import com.android.k19.musicproject.application.MusicApplication;
import com.android.k19.musicproject.dataservices.HistoryAdapter;
import com.android.k19.musicproject.models.Song;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends Activity {
	private SharedPreferences myShare;
	HistoryAdapter adapter;
	ArrayList<Song> myArray;
	ListView listview;
	String path;
	final int add = 1;
	final int delete = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_show);

		show();
		try {
			if (myArray.equals("")) {
			} else {

				listview = (ListView) findViewById(R.id.listView1);

				adapter = new HistoryAdapter(this, myArray);

				listview.setAdapter(adapter);

				listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent i = new Intent(HistoryActivity.this,
								NowPlaying.class);
						MusicApplication.getInstance().SetNewPlaying(myArray,
								position);

						startActivity(i);
					}
				});
				registerForContextMenu(listview);
			}
		} catch (Exception e) {
		}

	}

	public void show() {

		myShare = getSharedPreferences("history", 0);
		myArray = new ArrayList<Song>();
		for (int i = 1; i <= 5; i++) {
			try {
				String path = myShare.getString("song" + i, "");
				if (path.equals("")) {
					// check = true;

				} else {
					Song mySong = new Song(path);

					myArray.add(mySong);

				}
			}

			catch (Exception e) {
			}

		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {

		menu.add(Menu.NONE, add, Menu.NONE, "Add To Playlists");
		menu.add(Menu.NONE, delete, Menu.NONE, "Delete");

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		final int pos = info.position + 1;
		switch (item.getItemId()) {
		case add:
			myShare = getSharedPreferences("history", 0);
			String myPathAdd = myShare.getString("song" + pos, "");
			 Intent i = new Intent(HistoryActivity.this, PlaylistDialog.class);
        	 i.putExtra("mUrlSong",myPathAdd);
        	 startActivity(i);
        	 return true;
		case delete:
			new AlertDialog.Builder(HistoryActivity.this)
			.setTitle("Delete Song")
			.setMessage("Are you sure?")
			.setPositiveButton("Yes", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					try{
						myShare = getSharedPreferences("history", 0);
						String myPathDelete = myShare.getString("song" + pos, "");

						File file = new File(myPathDelete);
						file.delete();
						Toast.makeText(getApplicationContext(), "Delete Song " + pos,
								Toast.LENGTH_SHORT).show();
						
						removeSong(pos);
						finish();
						startActivity(getIntent());
						}catch(Exception e){}
				}
				
					
			})
			.setNegativeButton("No", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			})
			.show();
		
			return (true);
		}
		return (super.onOptionsItemSelected(item));
	}

	public void removeSong(int pos) {
		myShare = getSharedPreferences("history", 0);
		Editor edit = myShare.edit();

		if (pos == 1) {
			edit.putString("song1", myShare.getString("song2", ""));
			edit.putString("song2", myShare.getString("song3", ""));
			edit.putString("song3", myShare.getString("song4", ""));
			edit.putString("song4", myShare.getString("song5", ""));
			edit.putString("song5", "");
			edit.commit();
//			Toast.makeText(getApplicationContext(), "Remove Song " + pos,
//					Toast.LENGTH_SHORT).show();

		}
		if (pos == 2) {
			edit.putString("song2", myShare.getString("song3", ""));
			edit.putString("song3", myShare.getString("song4", ""));
			edit.putString("song4", myShare.getString("song5", ""));
			edit.putString("song5", "");
			edit.commit();
//			Toast.makeText(getApplicationContext(), "Remove Song " + pos,
//					Toast.LENGTH_SHORT).show();

		}
		if (pos == 3) {
			edit.putString("song3", myShare.getString("song4", ""));
			edit.putString("song4", myShare.getString("song5", ""));
			edit.putString("song5", "");
			edit.commit();
//			Toast.makeText(getApplicationContext(), "Remove Song " + pos,
//					Toast.LENGTH_SHORT).show();

		}
		if (pos == 4) {
			edit.putString("song4", myShare.getString("song5", ""));
			edit.putString("song5", "");
			edit.commit();
//			Toast.makeText(getApplicationContext(), "Remove Song " + pos,
//					Toast.LENGTH_SHORT).show();

		} else {
			edit.putString("song5", "");
			edit.commit();
//			Toast.makeText(getApplicationContext(), "Remove Song " + pos,
//					Toast.LENGTH_SHORT).show();

		}

	}

	
}
