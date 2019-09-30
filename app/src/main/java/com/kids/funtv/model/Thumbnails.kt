package com.kids.funtv.model

import com.squareup.moshi.Json

data class Thumbnails(
    @field:Json(name = "high")
    val high: High,
    @field:Json(name = "medium")
    val medium: High
)