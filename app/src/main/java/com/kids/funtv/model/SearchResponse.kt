package com.kids.funtv.model

import com.squareup.moshi.Json

data class SearchResponse(
    @field:Json(name = "kind")
    val kind: String,
    @field:Json(name = "etag")
    val etag: String,
    @field:Json(name = "nextPageToken")
    val nextPageToken: String,
    @field:Json(name = "regionCode")
    val regionCode: String,
    @field:Json(name = "items")
    val items: List<SearchItem>
)