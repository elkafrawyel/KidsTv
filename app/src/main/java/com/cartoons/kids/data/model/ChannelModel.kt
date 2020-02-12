package com.cartoons.kids.data.model

data class ChannelModel(
    val id: String,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}