package com.kids.funtv.ui.main

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kids.funtv.R
import com.kids.funtv.model.ChannelModel

class AdapterCartoons  :
    BaseQuickAdapter<ChannelModel, BaseViewHolder>(R.layout.channel_item_view) {

    override fun convert(helper: BaseViewHolder, item: ChannelModel) {
        helper.setText(R.id.cartoonName, item.name)
        helper.getView<ImageView>(R.id.cartoonImage).setImageResource(item.image)
        helper.addOnClickListener(R.id.cartoonItem)
    }

}