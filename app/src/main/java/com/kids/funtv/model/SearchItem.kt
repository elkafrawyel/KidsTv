package com.kids.funtv.model

import com.squareup.moshi.Json

class SearchItem (
    @field:Json(name = "id")
    val id: Id,
    @field:Json(name = "snippet")
    val snippet: Snippet)