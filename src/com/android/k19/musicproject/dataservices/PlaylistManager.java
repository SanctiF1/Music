package com.android.k19.musicproject.dataservices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.android.k19.musicproject.models.Playlist;
import com.android.k19.musicproject.models.Song;

public class PlaylistManager {

	static List<String> playlistInSDCard;
	
	public ArrayList<Playlist> loadPlaylist()
	{
		ArrayList<Playlist> arrayPlaylist = new ArrayList<Playlist>();
		File dir = new File(Environment.getExternalStorageDirectory().getPath());
		playlistInSDCard = new ArrayList<String>();
    	playlistInSDCard.clear();
    	Process(dir);
    	for(int i=0;i<playlistInSDCard.size();i++)
    	{
    		Playlist p = new Playlist(playlistInSDCard.get(i).substring(playlistInSDCard.get(i).lastIndexOf("/")).replace(".m3u8", "").replace("/", ""), playlistInSDCard.get(i));
    		arrayPlaylist.add(p);
    	}
    	return arrayPlaylist;
	}
	
	static void Process(File aFile) {
        if(aFile.isFile())
        {
          if(aFile.getName().endsWith("m3u8"))
          {
        	  playlistInSDCard.add(aFile.getAbsolutePath());
          }
        }
        else if (aFile.isDirectory()) {
          File[] listOfFiles = aFile.listFiles();
          if(listOfFiles!=null) {
            for (int i = 0; i < listOfFiles.length; i++)
              Process(listOfFiles[i]);
          } else {
            System.out.println("[ACCESS DENIED]"+aFile.getAbsolutePath());
          }
        }
      }
	public boolean isExist(String url)
	{
		File file = new File(url);
		return file.exists();
	}
	
	File mDir;
    public Playlist createNewPlaylist(String file)
    {
    	if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            mDir=new File(android.os.Environment.getExternalStorageDirectory(),"playlists");
        else
            mDir= new File(Environment.getDataDirectory()+"/Playlists");
        if(!mDir.exists())
            mDir.mkdirs();
        try{
        File myFile = new File(mDir+"/"+file+".m3u8");
        if(myFile.exists())
        	return null;
        else
        {
        	myFile.createNewFile();
        	return new Playlist(file, mDir.getAbsolutePath());
        }
        }
        catch(Exception e){
        	return null;
        }
    }
    
    public void deleteFile(String url)
    {
    	File file = new File(url);
    	file.delete();
    }
    
    public boolean isSongExist(String urlSong, String urlPlaylist)
    {
    	ArrayList<Song> arrayListSongs = loadSongsInPlaylist(urlPlaylist);
    	boolean isExist = false;
		if(arrayListSongs.size()>0)
		{
			for (Song s : arrayListSongs) {
				if(s.getPath().equals(urlSong))
				{
					isExist=true;
					break;
				}
			}
		}
		if(isExist==false)
		{
			try{
				BufferedWriter writer = new BufferedWriter(new FileWriter(urlPlaylist, true));
				writer.append(urlSong+"\n");
				writer.flush();
				writer.close();
				return isExist;

			}
			catch(Exception e){}
		}
		return isExist;
    }
    
    
    public boolean addSongToPlaylist(String urlSong, String urlPlaylist)
    {
    	ArrayList<Song> arrayListSongs = loadSongsInPlaylist(urlPlaylist);
    	boolean isExist = false;
		if(arrayListSongs.size()>0)
		{
			for (Song s : arrayListSongs) {
				if(s.getPath().equals(urlSong))
				{
					isExist=true;
					break;
				}
			}
		}
		if(isExist==false)
		{
			try{
				BufferedWriter writer = new BufferedWriter(new FileWriter(urlPlaylist, true));
				writer.append(urlSong+"\n");
				writer.flush();
				writer.close();
				return true;

			}
			catch(Exception e){}
		}
		return false;
    }
    
    public ArrayList<Song> loadSongsInPlaylist(String urlPlaylist)
    {
    	ArrayList<Song> arrayListSongs = new ArrayList<Song>();
    	try {
    		arrayListSongs.clear();
			File myFile = new File(urlPlaylist);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null && aDataRow.contains(".mp3")) {
				Song song = new Song(aDataRow);
				arrayListSongs.add(song);
			}
			myReader.close();
		} catch (Exception e) {}
    	return arrayListSongs;
    }
    
    public void removeSongInPlaylist(String urlSong, String urlPlaylist)
    {
    	try{
			File inputFile = new File(urlPlaylist);
			File tempFile = new File(urlPlaylist+"temp");
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			String currentLine;
			while((currentLine = reader.readLine()) != null) {
			    // trim newline when comparing with lineToRemove
			    //String trimmedLine = currentLine.trim();
			    if(currentLine.equals(urlSong)) continue;
			    writer.write(currentLine+"\n");
			}
			tempFile.renameTo(inputFile);
			writer.close();
			reader.close();
			}catch(Exception e){}
    }
    
    public void checkSongsExist(String urlPlaylist)
    {
    	try{
			File inputFile = new File(urlPlaylist);
			File tempFile = new File(urlPlaylist+".temp");
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			String currentLine;
			while((currentLine = reader.readLine()) != null) {
			    // trim newline when comparing with lineToRemove
			    //String trimmedLine = currentLine.trim();
				File s = new File(currentLine);
				if(s.exists())
					writer.write(currentLine+"\n");
				else
					continue;
			}
			tempFile.renameTo(inputFile);
			writer.close();
			reader.close();
			}catch(Exception e){}
    }
}
