/**
 * 
 */
package com.android.k19.musicproject.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.app.Application;

import com.android.k19.musicproject.models.Song;

/**
 * @author Trung Nguyen
 *
 */
public class MusicApplication extends Application {
	private ArrayList<Song> list_song;
	private int index;
	private int previous;
	private int next;
	private boolean playing_come_back;
	private Timer tm;
	public Timer getTm() {
		return tm;
	}
	public void setTm(Timer tm) {
		this.tm = tm;
	}
	public boolean isPlaying_come_back() {
		return playing_come_back;
	}
	public void setPlaying_come_back(boolean playing_come_back) {
		this.playing_come_back = playing_come_back;
	}
	public ArrayList<Song> getList_song() {
		return list_song;
	}
	public void setList_song(ArrayList<Song> list_song) {
		this.list_song = list_song;
	}
	private boolean shuffle = false;
	private int repeat;
	private long second;
	private boolean timer_stop;
	private boolean playing;
	public boolean isTimer_stop() {
		return timer_stop;
	}
	public void setTimer_stop(boolean timer_stop) {
		this.timer_stop = timer_stop;
	}
	public boolean isPlaying() {
		return playing;
	}
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	public long getSecond() {
		return second;
	}
	public void setSecond(long second) {
		this.second = second;
	}
	public int getPrevious() {
		return previous;
	}
	public void setPrevious(int previous) {
		this.previous = previous;
	}
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		index = 0;
		next = 1;
		previous = -1;
		repeat = 0;
		shuffle = false;
		second = 0;
		timer_stop = false;
		playing = false;
		playing_come_back = false;
		list_song = new ArrayList<Song>();
		tm = new Timer();
	}
	
	public MusicApplication() {
		// TODO Auto-generated constructor stub
		super();
		instance = this;
	}
	private static MusicApplication instance = null;
	public static MusicApplication getInstance(){
		return instance;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isShuffle() {
		return shuffle;
	}
	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}
	public int getRepeat() {
		return repeat;
	}
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	public void SetNewPlaying(ArrayList<Song> list_song,int index){
		this.list_song = list_song;
		this.index = index;
		if(index == 0){
			next = 1;
			previous = -1;
		}else{
			if(index == list_song.size()-1){
				next = -1;
				previous = index - 1;
			}
			else{
				next = index + 1;
				previous = index - 1;
			}
		}
		repeat = 0;
		shuffle = false;
		second = 0;
		timer_stop = false;
		playing = false;
		playing_come_back = false;
		if(tm!=null){
			tm.cancel();
			tm = null;
		}
	}
}
