package com.cartoons.kids.data.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.cartoons.kids.data.model.videoModel.VideoItem

data class VideoModel (
    var videoItem: VideoDB?,
    var type:Int

) : MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }
}