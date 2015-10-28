package com.android.k19.musicproject.dataservices;

import java.util.ArrayList;
import java.util.List;

import com.android.k19.musicproject.R;
import com.android.k19.musicproject.models.Song;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Activity content;
	ArrayList<Song> list;


	public HistoryAdapter(Activity content, ArrayList<Song> list)
	{

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
		View songView =convertView;
		if(songView==null){
			songView=mInflater.inflate(R.layout.history_custom, null);
		}
		final Song songInfo= list.get(position);
		if(songInfo!=null){
			TextView myText= (TextView)songView.findViewById(R.id.txtName);
			TextView myArtist= (TextView)songView.findViewById(R.id.txtArtist);
			TextView myAlbum= (TextView)songView.findViewById(R.id.txtAlbum);
			TextView myLengh= (TextView)songView.findViewById(R.id.txtLengh);
			ImageView myImg=(ImageView)songView.findViewById(R.id.imageView1);
			if(position==0){
				myImg.setImageResource(R.drawable.icon_recent_history1);
			}
			if(position==1){
				myImg.setImageResource(R.drawable.icon_recent_history2);
			}
			if(position==2){
				myImg.setImageResource(R.drawable.icon_recent_history3);
			}
			if(position==3){
				myImg.setImageResource(R.drawable.icon_recent_history4);
			}
			if(position==4){
				myImg.setImageResource(R.drawable.icon_recent_history5);
			}
			myText.setText(songInfo.getName());
			myArtist.setText(songInfo.getArtist());
			myAlbum.setText(songInfo.getAlbum());
			myLengh.setText(songInfo.getLength());
		}
		return songView;
	}

}
