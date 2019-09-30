package com.kids.funtv.service.remote

import com.kids.funtv.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeAPI {
    @GET("search/")
    fun search(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("q") search: String?,
        @Query("type") type: String,
        @Query("pageToken") pageToken: String?,
        @Query("maxResults") maxResult: Int
    ): Call<SearchResponse>

    @GET("search/")
    fun getRelatedVideos(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("relatedToVideoId") relatedToVideoId: String,
        @Query("type") type: String,
        @Query("pageToken") pageToken: String?,
        @Query("maxResults") maxResult: Int
    ): Call<SearchResponse>
}