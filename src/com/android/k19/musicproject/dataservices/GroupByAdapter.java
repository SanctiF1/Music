


package com.android.k19.musicproject.dataservices;
import java.util.ArrayList;
import java.util.HashMap;

import com.android.k19.musicproject.R;
import com.android.k19.musicproject.models.Song;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupByAdapter extends BaseExpandableListAdapter {
	private Context myContext;
	private String Type;
	public GroupByAdapter(Context context,String Type) {
	   myContext = context;
	   this.Type = Type;
	}
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
		    LayoutInflater inflater =  (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    convertView = inflater.inflate(R.layout.list_song_view, null);
		   }
		    
		   TextView tvName = (TextView) convertView.findViewById(R.id.txtName);
		   TextView tvArtist = (TextView) convertView.findViewById(R.id.txtArtist);
		   TextView tvLength = (TextView) convertView.findViewById(R.id.txtLengh);
		   ImageView img =(ImageView)convertView.findViewById(R.id.imageView1);
		   ArrayList<Song> listsong ;
		   if(Type.equals(ListSong.TYPE_ALBUMS))
		   {
			   listsong = ListSong.listAlbum.get(ListSong.listAlbumName.get(groupPosition));
		   }
		   else{
			   listsong = ListSong.listArtist.get(ListSong.listArtistName.get(groupPosition));
		   }
		   if(listsong.get(childPosition).getPicture()!=null)
		   {
			   Bitmap bm = BitmapFactory.decodeByteArray(listsong.get(childPosition).getPicture(), 0, listsong.get(childPosition).getPicture().length);
			   img.setImageBitmap(bm);
		   }
		   tvName.setText(listsong.get(childPosition).getName());
		   tvArtist.setText(listsong.get(childPosition).getArtist());
		   tvLength.setText(listsong.get(childPosition).getLength());
		   return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(Type.equals(ListSong.TYPE_ALBUMS))
		{
			ArrayList<Song> listsong = ListSong.listAlbum.get(ListSong.listAlbumName.get(groupPosition));
			return listsong.size();
		}else
		{
			ArrayList<Song> listsong = ListSong.listArtist.get(ListSong.listArtistName.get(groupPosition));
			return listsong.size();
		}
	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		if(Type.equals(ListSong.TYPE_ALBUMS))
		{
			return ListSong.listAlbumName.size();
		}else
		{
			return ListSong.listArtistName.size();
		}
		
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) 
		{
		    LayoutInflater inflater =  (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    convertView = inflater.inflate(R.layout.group, null);
		   }
		TextView tvGroupName = (TextView) convertView.findViewById(R.id.txtGroupName);
		   TextView tvGroupTotal = (TextView) convertView.findViewById(R.id.txtGroupTotal);
		   TextView tvGroupLength = (TextView) convertView.findViewById(R.id.txtGroupLength);
		   ImageView imgView = (ImageView)convertView.findViewById(R.id.ImgGroup);
		ArrayList<String> listgroup = null;
		ArrayList<Song> listChild = null;
		   if(Type.equals(ListSong.TYPE_ALBUMS))
		   {
			   listgroup = ListSong.listAlbumName;
			   listChild = ListSong.listAlbum.get(listgroup.get(groupPosition));
			   imgView.setImageResource(R.drawable.img_album_icon);
		   }else
		   {
			   listgroup = ListSong.listArtistName;
			   listChild = ListSong.listArtist.get(listgroup.get(groupPosition));
			   imgView.setImageResource(R.drawable.img_artist_icon);
		   }
//			    
		   for(int i=0;i<listChild.size();i++)
		   {
			   if(listChild.get(i).getPicture() != null)
			   {
				   Bitmap bm = BitmapFactory.decodeByteArray(listChild.get(i).getPicture(), 0, listChild.get(i).getPicture().length);
				   imgView.setImageBitmap(bm);
			   }
		   }
		   
		   tvGroupName.setText(listgroup.get(groupPosition));
		   tvGroupTotal.setText("Total: "+String.valueOf(listChild.size())); 
		   tvGroupLength.setText(TotalLength(listChild));
		   return convertView;
	}

	public String TotalLength(ArrayList<Song> list)
	{
		int length = 0;
		for(int i = 0;i< list.size();i++)
		{
			String lng = list.get(i).getLength();
			int num1 = (Integer.parseInt(lng.substring(0,lng.indexOf(":")))) * 60;
			int num2 = Integer.parseInt(lng.substring(lng.indexOf(":") +1));
			length = length + (num1 +num2);
		}
		return String.valueOf(length/60) + ":" + String.valueOf(length%60);
	}
	
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	
}
}

