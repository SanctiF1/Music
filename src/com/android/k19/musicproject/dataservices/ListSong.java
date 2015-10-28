package com.android.k19.musicproject.dataservices;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.k19.musicproject.models.Song;

public final class ListSong {
	public static ArrayList<Song> listAllSongs = new ArrayList<Song>();
	public static ArrayList<String> listAlbumName = new ArrayList<String>();
	public static ArrayList<String> listArtistName = new ArrayList<String>();
	public static HashMap<String, ArrayList<Song>> listAlbum = new HashMap<String, ArrayList<Song>>();
	public static HashMap<String, ArrayList<Song>> listArtist = new HashMap<String, ArrayList<Song>>();
	public static final String TYPE_ALBUMS ="album";
	public static final String TYPE_ARTISTS ="artist";
	public static boolean refesh()
	{
		listAllSongs = new ArrayList<Song>();
		listAlbumName = new ArrayList<String>();
		listArtistName = new ArrayList<String>();
		 listAlbum = new HashMap<String, ArrayList<Song>>();
		 listArtist = new HashMap<String, ArrayList<Song>>();
		return true;
	}
}
