/**
 * 
 */
package com.android.k19.musicproject.models;

import java.util.ArrayList;

/**
 * @author Trung Nguyen
 *
 */
public class Playlist {
	private String name;
	private String url;
	private ArrayList<Song> list_song;
	public Playlist(String name, String url) {
		super();
		this.name = name;
		this.url = url;
		this.list_song = new ArrayList<Song>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ArrayList<Song> getList_song() {
		return list_song;
	}
	public void AddSongToPlaylist(Song song){
		list_song.add(song);
	}
	public void RemoveSong(int index){
		list_song.remove(index);
	}
	public int CountSong(){
		return list_song.size();
	}
}
