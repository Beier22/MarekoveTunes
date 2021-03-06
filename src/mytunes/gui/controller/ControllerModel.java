/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.AudioPlayer;
import mytunes.bll.IModel;
import mytunes.bll.IPlayerModel;
import mytunes.bll.Model;

/**
 *
 * @author Marek
 */
public class ControllerModel
{
    //List with all songs
    private ObservableList<Song> songList;  
    
    //List with all playlists
    private ObservableList<Playlist> playlistList;
    
    //Currently selected playlist songs
    private ObservableList<Song> playlistSongsList;
    
    //Index of currently selected playlist
    private int selectedPlaylistIndex;
    
    //BusinessModel
    private IModel appModel; 
    
    //AudioPlayer object
    private IPlayerModel audioPlayer;
    
    //Constructor - initialize audioplayer and lists
    public ControllerModel()
    {
        selectedPlaylistIndex = 0;  
        appModel = new Model();
        songList = getSongList();
        playlistList = getPlaylists();
        playlistSongsList = FXCollections.observableArrayList();
        audioPlayer = new AudioPlayer();
    }
    
    /**
     * 
     * @return listindex of currently playing song
     */
    public int getCurrentIndex()
    {
        return audioPlayer.getCurrentIndex();
    }
    
    /** 
     * 
     * @return list of all songs in database
     */
    public ObservableList<Song> getSongList()
    {
        if(songList == null)
        {
            songList = FXCollections.observableArrayList();
            songList.setAll(getModel().getAllSongs());
        } 
        return songList;
    }
    
    /**
     * 
     * @return list of all playlists in database
     */
    public ObservableList<Playlist> getPlaylists()
    {
        if(playlistList == null)
        {
            playlistList = FXCollections.observableArrayList(); 
            playlistList.setAll(getModel().getAllPlaylists());
        } 
        return playlistList;
    }
    
    /**
     * 
     * @return list of all songs in selected playlist
     */
    public ObservableList<Song> getSelectedPlaylistSongs()
    {
        if(playlistSongsList == null)
        {
            setSelectedPlaylistIndex(selectedPlaylistIndex);
        } 
        return playlistSongsList; 
    }
    
    /**
     * 
     * @param playlist from which the songs will be returned
     * @return all songs in provided playlist
     */
    private List<Song> getSongsFromPlaylist(Playlist playlist)
    {
        List<Song> retval = new ArrayList();
        List<Integer> ids = playlist.getSongIds();
        for( int id : ids)
        {
            for( Song song: getSongList())
            {
                if(song.getID() == id)
                {
                    retval.add(song);
                    break;
                }
            }
        }
        return retval;
    }
    
    //Sets currently selected playlist
    public void setSelectedPlaylistIndex(int playlistIndex)
    {
       selectedPlaylistIndex = playlistIndex; 
       playlistSongsList.setAll(getSongsFromPlaylist(playlistList.get(playlistIndex)));
    }
    
    /**
     * 
     * @return currently selected playlist 
     */
    public Playlist selectedPlaylist()
    {
        return playlistList.get(selectedPlaylistIndex);
    }
    
    //Deletes currently selected playlist
    public void deletePlaylist()
    {
        playlistSongsList.clear();
        appModel.deletePlaylist(selectedPlaylist());
        playlistList.remove(selectedPlaylist());
        if(selectedPlaylistIndex > 0)
            setSelectedPlaylistIndex(selectedPlaylistIndex -1 );
    }
    
    //Adds playlist to database and playlists list
    public void addPlaylist(Playlist p)
    {
        playlistList.add(p);
        appModel.addPlaylist(p);
    }
    
    public void updatePlaylist(Playlist p)
    {
        appModel.updatePlaylist(p);
    }
    
    //Adds song to currently selected playlist
    public void addSongToPlaylist(int index)
    {
        Song song = songList.get(index);
        if(!playlistSongsList.contains(song))
            playlistSongsList.add(song);
        appModel.addSongToPlaylist(selectedPlaylist(), song);
        selectedPlaylist().addSongId(song.getID());
    }
    
    //Deletes song from currently selected playlist
    public void deleteSongFromPlaylist(int index)
    {
        Song song = playlistSongsList.get(index);
        if(playlistSongsList.contains(song))
            playlistSongsList.remove(song);
        appModel.deleteSongFromPlaylist(selectedPlaylist(),song);
        selectedPlaylist().removeSongId(song.getID());
    }
    
    //Adds song to song list and database
    public void addSong(Song s)
    {
        if(!songList.contains(s))
            songList.add(s);
        appModel.addSong(s);
    }
    
    //Deletes song from list and database
    public void deleteSong(Song s)
    {
        if(songList.contains(s))
            songList.remove(s);
        appModel.deleteSong(s);
    }
    
    
    public void filterPlaylists(String filter)
    {
        playlistList.setAll(appModel.getFilteredPlaylists(filter));//getAllPlaylists());
    }
    
    public void filterSongs(String filter)
    {
        songList.setAll(appModel.getFilteredSongs(filter));
    }
    
    public void filterPlaylistSongs(String filter)
    {
        playlistSongsList.setAll(appModel.getFilteredPlaylistSongs(selectedPlaylist(), filter) );
    }
    
    public void playSong(Song s)
    {
        audioPlayer.playPlaylist(songList, songList.indexOf(s));
    }
    
    public Song playNext()
    {
        return audioPlayer.playNext();
    }
    
    public Song playPrev()
    {
        return audioPlayer.playPrev();
    }
    
    public void setRepeat(boolean repeat)
    {
        audioPlayer.setRepeat(repeat);
    }
    
    public void playPlaylist(int index)
    {
        audioPlayer.playPlaylist(playlistSongsList, index);
    }
    
    public void pressPlay()
    {
        if(audioPlayer.isPlaying())
            audioPlayer.pause();
        else audioPlayer.play();
    }
    
    public void stopSong()
    {
        audioPlayer.stop();
    }
    
    public void setVolume(double vol)
    {
        audioPlayer.setVolume(vol);
    }
    
    public void setTime(double time)
    {
        audioPlayer.setTime(time);
    }
   
    //Returns percentage of time passed on currently playing song
    public double getTimePosition()
    {
        return audioPlayer.getTimePercentage();
    }  
    
    //Returns time string form currently playing song
    public String getTimeString()
    {
        return audioPlayer.getFormatedCurrentTime();
    }
    
    public boolean isSongPlaying()
    {
        return audioPlayer.isPlaying();
    }
    
    public void setShuffle(boolean shuffle)
    {
        audioPlayer.setShuffle(shuffle);
    }
    
    private IModel getModel()
    {
        if(appModel == null)
            appModel = new Model();
        return appModel;
    }
}
