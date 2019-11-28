package com.kids.funtv.ui.main

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kids.funtv.MyApp
import com.kids.funtv.R
import com.kids.funtv.data.model.ChannelModel

class AdapterChannels  :
    BaseQuickAdapter<ChannelModel, BaseViewHolder>(R.layout.channel_item_view) {

    override fun convert(helper: BaseViewHolder, item: ChannelModel) {
        helper.getView<ImageView>(R.id.cartoonImage).setImageResource(item.image)
        helper.addOnClickListener(R.id.cartoonItem)
    }

}