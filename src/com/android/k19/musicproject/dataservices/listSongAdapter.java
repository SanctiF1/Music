package com.android.k19.musicproject.dataservices;

import java.util.ArrayList;

import com.android.k19.musicproject.R;
import com.android.k19.musicproject.models.Song;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class listSongAdapter extends ArrayAdapter<Song> {
	
	private ArrayList<Song> original;
	private ArrayList<Song> fitems;
	private Filter filter;
	
	public listSongAdapter(Context context, int textViewResourceId, ArrayList<Song> items)
	{
		super(context, textViewResourceId, items);
		this.original = new ArrayList<Song>(items);
		this.fitems = new ArrayList<Song>(items);
		this.filter = new SongFilter();
	}
	
		
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (filter == null)
		{
		    filter  = new SongFilter();
		}
		return filter;
	}



	
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View v = convertView;
		Log.v("ConvertView", String.valueOf(position));
		if(v == null){
	        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        v = vi.inflate(R.layout.list_song_view, null);
	    }

	    Song song = fitems.get(position);

	    if(song != null)
	    {
	    	TextView txtname = (TextView)v.findViewById(R.id.txtName);
			TextView txtArtist = (TextView)v.findViewById(R.id.txtArtist);
			TextView txtAlbum= (TextView)v.findViewById(R.id.txtAlbum);
			TextView txtLengh= (TextView)v.findViewById(R.id.txtLengh);

	        if(txtname != null){
	        	txtname.setText(song.getName());
	        }
	        if(txtArtist != null){
	        	txtArtist.setText(song.getArtist());
	        }
	        if(txtAlbum != null){
	        	txtAlbum.setText(song.getAlbum());
	        }
	        if(txtLengh != null){
	        	txtLengh.setText(song.getLength());
	        }
	    }
		return v;
	}
	public class SongFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// TODO Auto-generated method stub
			FilterResults results = new FilterResults();
	        String prefix = constraint.toString().toLowerCase();

	        if (prefix == null || prefix.length() == 0){
	            ArrayList<Song> list = new ArrayList<Song>(original);
	            results.values = list;
	            results.count = list.size();
	        }else{
	            final ArrayList<Song> list = new ArrayList<Song>(original);
	            ArrayList<Song> nlist = new ArrayList<Song>();
	            int count = list.size();

	            for (int i = 0; i<count; i++){
	                final Song song = list.get(i);
	                final String value = song.getName().toLowerCase();

	                if(value.contains(prefix)){
	                    nlist.add(song);
	                }
	                results.values = nlist;
	                results.count = nlist.size();
	            }
	        }
	        return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			// TODO Auto-generated method stub
			
			fitems = (ArrayList<Song>) results.values;
			if (fitems != null)
			{
				//Log.v("Filter", "co new value");
				notifyDataSetChanged();
		        clear();
		        //Log.v("Filter", "Starting to publish the results with " + fitems.size() + " items");
		        int count = fitems.size();
		        for(int i = 0; i<count; i++)
		        {
		            add(fitems.get(i));    
		        }
		        //Log.v("Filter", "Finished publishing results");
		        notifyDataSetInvalidated();
			}

		}
	}

}
