package com.kids.funtv.ui.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kids.funtv.R
import com.kids.funtv.model.ChannelModel
import kotlinx.android.synthetic.main.channels_dialog.*

class CartoonsDialog(context: Context, private val iCartoonCallback: ICartoonCallback) :
    Dialog(context),
    BaseQuickAdapter.OnItemChildClickListener {


    private val adapter = AdapterCartoons().also {
        it.onItemChildClickListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this@CartoonsDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.setCancelable(true)
        setContentView(R.layout.channels_dialog)
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val cartoonList = ArrayList<ChannelModel>()
        cartoonList.add(ChannelModel("Spacetoon", R.drawable.spacetoon_logo))
        cartoonList.add(ChannelModel("Cartoon Network", R.drawable.cartoon_network_logo))

        adapter.replaceData(cartoonList)
        cartoonsRv.adapter = adapter
        cartoonsRv.setHasFixedSize(true)

        close.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.cartoonItem -> {
                val item = (adapter?.data as List<ChannelModel>)[position]
                iCartoonCallback.selectedCartoon(item)
                this.dismiss()
            }
        }
    }
}

interface ICartoonCallback {
    fun selectedCartoon(channelModel: ChannelModel)
}