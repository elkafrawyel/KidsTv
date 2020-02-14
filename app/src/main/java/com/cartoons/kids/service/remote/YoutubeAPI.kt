package com.cartoons.kids.service.remote

import com.cartoons.kids.data.model.playlistModel.PlayListResponse
import com.cartoons.kids.data.model.videoModel.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface YoutubeAPI {

    @GET("playlists")
    fun getPlayLists(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("channelId") channelId: String
    ): Call<PlayListResponse>

    @GET("playlistItems")
    fun getPlayListItems(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("playlistId") playlistId: String,
        @Query("maxResults") maxResult: Int
        ): Call<VideoResponse>

}