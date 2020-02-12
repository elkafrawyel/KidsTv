package com.cartoons.kids.data.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.cartoons.kids.data.model.videoModel.VideoItem

data class VideoModel (
    var searchItem: VideoItem?,
    var type:Int

) : MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }
}