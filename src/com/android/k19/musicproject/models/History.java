/**
 * 
 */
package com.android.k19.musicproject.models;

import java.util.ArrayList;

/**
 * @author Trung Nguyen
 *
 */
public class History {
	private ArrayList<Song> list_song;
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public History(String url) {
		super();
		list_song = new ArrayList<Song>();
		this.url = url;
	}
	public void AddSongToHistory(Song song){
		list_song.add(song);
	}
	public void RemoveSong(int index){
		list_song.remove(index);
	}
	public int CountSong(){
		return list_song.size();
	}
}
