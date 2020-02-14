package com.cartoons.kids.data.storage.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cartoons.kids.data.model.PlayListDB
import com.cartoons.kids.data.model.VideoDB

@Dao
interface MyDao {

    @Query("SELECT * FROM Playlist WHERE channelId= :channelId")
    fun getPlayLists(channelId: String): List<PlayListDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(item: List<PlayListDB>)

    @Query("DELETE FROM Playlist")
    fun deletePlaylist()

    @Query("SELECT * FROM Playlist WHERE channelId = :channelId")
    fun getPlaylistsByChannelId(channelId: String): List<PlayListDB>?

    @Query("SELECT * FROM Playlist WHERE id = :playlistId")
    fun getPlaylistBylId(playlistId: String): PlayListDB

    @Query("SELECT * FROM Video")
    fun getVideos(): List<VideoDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideo(item: List<VideoDB>)

    @Query("DELETE FROM video WHERE playlistId = :playlistId")
    fun deleteVideo(playlistId: String)

    @Query("SELECT * FROM Video WHERE playlistId= :playlistId")
    fun getVideosByPlaylist(playlistId: String): List<VideoDB>?

}