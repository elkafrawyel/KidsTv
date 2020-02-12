package com.cartoons.kids.data.model.playlistModel

import com.squareup.moshi.Json


data class PlayListResponse(
    @field:Json(name = "etag")
    val etag: String,
    @field:Json(name = "items")
    val playLists: List<PlayListItem>,
    @field:Json(name = "kind")
    val kind: String,
    @field:Json(name = "nextPageToken")
    val nextPageToken: String,
    @field:Json(name = "pageInfo")
    val pageInfo: PageInfo
)

data class PlayListItem(
    @field:Json(name = "etag")
    val etag: String,
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "kind")
    val kind: String,
    @field:Json(name = "snippet")
    val snippet: Snippet
)

data class Snippet(
    @field:Json(name = "channelId")
    val channelId: String,
    @field:Json(name = "channelTitle")
    val channelTitle: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "localized")
    val localized: Localized,
    @field:Json(name = "publishedAt")
    val publishedAt: String,
    @field:Json(name = "thumbnails")
    val thumbnails: Thumbnails,
    @field:Json(name = "title")
    val title: String
)

data class Localized(
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "title")
    val title: String
)

data class Thumbnails(
    @field:Json(name = "default")
    val default: Default,
    @field:Json(name = "high")
    val high: High,
    @field:Json(name = "maxres")
    val maxres: Maxres,
    @field:Json(name = "medium")
    val medium: Medium,
    @field:Json(name = "standard")
    val standard: Standard
)

data class Default(
    @field:Json(name = "height")
    val height: Int,
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "width")
    val width: Int
)

data class High(
    @field:Json(name = "height")
    val height: Int,
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "width")
    val width: Int
)

data class Maxres(
    @field:Json(name = "height")
    val height: Int,
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "width")
    val width: Int
)

data class Medium(
    @field:Json(name = "height")
    val height: Int,
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "width")
    val width: Int
)

data class Standard(
    @field:Json(name = "height")
    val height: Int,
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "width")
    val width: Int
)

data class PageInfo(
    @field:Json(name = "resultsPerPage")
    val resultsPerPage: Int,
    @field:Json(name = "totalResults")
    val totalResults: Int
)