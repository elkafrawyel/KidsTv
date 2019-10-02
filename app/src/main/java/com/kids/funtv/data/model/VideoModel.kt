package com.kids.funtv.data.model

import com.chad.library.adapter.base.entity.MultiItemEntity

data class VideoModel (
    var searchItem: SearchItem?,
    var type:Int

) : MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }
}