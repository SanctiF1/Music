/**
 * 
 */
package com.android.k19.musicproject.models;

import java.io.File;
import android.media.MediaMetadataRetriever;

/**
 * @author Trung Nguyen
 * In class, we only use 1 parameter. This parameter called "path".
 * "path" include path file and file name. After constructer with this parameter, we have all information such as name, artist....
 */
public class Song {
	private String name;
	private String artist;
	private String album;
	private long size;
	private String length;
	private byte[] picture;
	private String path;
	public Song(String path) {
		super();
		File file = new File(path);
		MediaMetadataRetriever info = new MediaMetadataRetriever();
		info.setDataSource(path);
		this.name = file.getName().substring(0,file.getName().lastIndexOf("."));
		this.artist = info.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		this.album = info.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		this.size = file.length();
		String duration = info.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		long dur = Long.parseLong(duration);
		String seconds = String.valueOf((dur % 60000) / 1000);
		String minutes = String.valueOf(dur / 60000);
		String length = "";
		if (seconds.length() == 1) {
			if(minutes.length() == 1){
				length = "0" + minutes + ":0" + seconds;
			}else{
				length = minutes + ":0" + seconds;
			}
		}else {
			if(minutes.length() == 1){
				length = "0" + minutes + ":" + seconds;
			}else{
				length = minutes + ":" + seconds;
			}
		}
		this.length = length;
		this.picture = info.getEmbeddedPicture();
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArtist() {
		return artist;
	}
	public String getAlbum() {
		return album;
	}
	public long getSize() {
		return size;
	}
	public String getLength() {
		return length;
	}
	public byte[] getPicture() {
		return picture;
	}
	public String getPath() {
		return path;
	}
	
}
