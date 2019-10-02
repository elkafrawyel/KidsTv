package com.kids.funtv.data.model

import com.squareup.moshi.Json

data class Snippet(
    @field:Json(name = "publishedAt")
    val publishedAt: String,
    @field:Json(name = "channelId")
    val channelId: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "thumbnails")
    val thumbnails: Thumbnails,
    @field:Json(name = "channelTitle")
    val channelTitle: String,
    @field:Json(name = "liveBroadcastContent")
    val liveBroadcastContent: String
)