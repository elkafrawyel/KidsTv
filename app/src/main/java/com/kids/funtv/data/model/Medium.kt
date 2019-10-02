package com.kids.funtv.data.model

import com.squareup.moshi.Json

data class Medium(
    @field:Json(name = "url")
    val url: String,
    @field:Json(name = "width")
    val width: String,
    @field:Json(name = "height")
    val height: String
)