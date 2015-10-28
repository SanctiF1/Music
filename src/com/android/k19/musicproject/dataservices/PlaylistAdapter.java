package com.android.k19.musicproject.dataservices;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.k19.musicproject.R;
import com.android.k19.musicproject.models.Playlist;

public class PlaylistAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Activity mcontext;
	private ArrayList<Playlist> listPlaylist;
	
	
	
	public PlaylistAdapter(Activity mcontext, ArrayList<Playlist> listPlaylist) {
		this.mcontext = mcontext;
		mInflater = this.mcontext.getLayoutInflater();
		this.listPlaylist = listPlaylist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPlaylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.rowlistplaylist, null);
		TextView txtPlaylist = (TextView)convertView.findViewById(R.id.row_textPlaylist);
		txtPlaylist.setText(listPlaylist.get(position).getName());
		return convertView;
	}

}
