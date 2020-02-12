package com.cartoons.kids.ui.main

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cartoons.kids.R
import com.cartoons.kids.common.loadWithPlaceHolder
import com.cartoons.kids.data.model.ChannelModel

class AdapterChannels :
    BaseQuickAdapter<ChannelModel, BaseViewHolder>(R.layout.channel_item_view) {

    override fun convert(helper: BaseViewHolder, item: ChannelModel) {
        helper.getView<ImageView>(R.id.cartoonImage).loadWithPlaceHolder(item.image)
        helper.setText(R.id.name, item.name)
        helper.addOnClickListener(R.id.cartoonItem)
    }

}