package com.kids.funtv.model

import com.squareup.moshi.Json

data class High(
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "width")
    val width: String,
    @field:Json(name = "height")
    val height: String
)