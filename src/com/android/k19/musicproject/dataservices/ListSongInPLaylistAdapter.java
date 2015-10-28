package com.android.k19.musicproject.dataservices;

import java.util.ArrayList;

import com.android.k19.musicproject.models.Song;

import com.android.k19.musicproject.R;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListSongInPLaylistAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Activity content;
	ArrayList<Song> list ;
	
	
	
	public ListSongInPLaylistAdapter(Activity content, ArrayList<Song> list) {
		this.content = content;
		this.list = list;
		mInflater = this.content.getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		
		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.rowlistsongsinplaylist, null);
			holder.name = (TextView)convertView.findViewById(R.id.row_songname);
			holder.artist = (TextView)convertView.findViewById(R.id.row_artist);
			holder.album = (TextView)convertView.findViewById(R.id.row_Album);
			holder.duration = (TextView)convertView.findViewById(R.id.row_Duration);
			holder.imv = (ImageView)convertView.findViewById(R.id.row_imageinPlaylists);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder)convertView.getTag();
		
		holder.name.setText(list.get(position).getName());
		holder.artist.setText(list.get(position).getArtist());
		holder.album.setText(list.get(position).getAlbum());
		holder.duration.setText(list.get(position).getLength());
		try{
			holder.imv.setImageBitmap(BitmapFactory.decodeByteArray(list.get(position).getPicture(), 0, list.get(position).getPicture().length));
		}catch(Exception e)
		{
			holder.imv.setImageResource(R.drawable.ic_launcher);
		}
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView name;
		TextView artist;
		TextView album;
		TextView duration;
		ImageView imv;
	}

}
