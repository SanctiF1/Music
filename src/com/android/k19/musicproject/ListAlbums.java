
package com.android.k19.musicproject;

import com.android.k19.musicproject.application.MusicApplication;
import com.android.k19.musicproject.dataservices.GroupByAdapter;
import com.android.k19.musicproject.dataservices.ListSong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ListAlbums extends Activity {
	 DisplayMetrics metrics;
	 int width;
	 ExpandableListView listview ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_album);
		listview = (ExpandableListView)findViewById(R.id.ListAlbumView);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        listview.setIndicatorBounds(width - GetDipsFromPixel(50), width - GetDipsFromPixel(10));
        GroupByAdapter adapter = new GroupByAdapter(this,ListSong.TYPE_ALBUMS);
		listview.setAdapter(adapter);
		listview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				MusicApplication musicapp = MusicApplication.getInstance();
				musicapp.SetNewPlaying(ListSong.listAlbum.get(ListSong.listAlbumName.get(groupPosition)), childPosition);
				Intent i =new Intent(ListAlbums.this,NowPlaying.class);
				startActivity(i);
				return false;
			}
		});
		listview.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				
			}
		});
		listview.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private int GetDipsFromPixel(float pixels) {
		// TODO Auto-generated method stub
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
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
			Intent i = new Intent(ListAlbums.this,NowPlaying.class);
			startActivity(i);
			return true;
		}
    	return false;
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	MusicProject.getdataContent();
		GroupByAdapter adapter = new GroupByAdapter(this,ListSong.TYPE_ALBUMS);
	    listview.setAdapter(adapter);
    }
}

