package com.cartoons.kids.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Playlist")
data class PlayListDB(
    @PrimaryKey val id: String,
    val name: String,
    val image: String,
    val channelId: String
)