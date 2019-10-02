package com.kids.funtv.data.model

import com.squareup.moshi.Json

data class Id(
    @field:Json(name = "kind")
    val kind: String,
    @field:Json(name = "videoId")
    val videoId: String
)