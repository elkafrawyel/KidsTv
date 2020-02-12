package com.cartoons.kids.ui.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cartoons.kids.R
import kotlinx.android.synthetic.main.channels_dialog.*
import com.cartoons.kids.data.model.ChannelModel as ChannelModel

class CartoonsDialog(context: Context, private val iCartoonCallback: ICartoonCallback) :
    Dialog(context, R.style.CustomDialog),
    BaseQuickAdapter.OnItemChildClickListener {


    private val adapter = AdapterChannels().also {
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

        close.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        setChannels()
    }

    private fun setChannels() {
        val cartoonList = ArrayList<ChannelModel>()

        cartoonList.add(
            ChannelModel(
                "UCwCLmo14vG4luqGsYcYxKrQ",
                "قناة سبيستون",
                R.drawable.spacetoon_logo
            )
        )

//        cartoonList.add(ChannelModel("قناة Mbc3", R.drawable.mbc3_logo))
//
//        cartoonList.add(ChannelModel("كارتون نت ورك بالعربية", R.drawable.cartoon_network_logo))
//
//        cartoonList.add(ChannelModel("قناة طيور الجنة", R.drawable.tyor_elgana))
//
//        cartoonList.add(ChannelModel("قناة كروان اطفال", R.drawable.karawan_logo))
//
//        cartoonList.add(ChannelModel(" قناة ميكي للاطفال", R.drawable.miky_logo))
//
//        cartoonList.add(ChannelModel("قناة براعم للاطفال", R.drawable.baraem_logo))
//
//        cartoonList.add(ChannelModel("قناة كوكي كيدز للاطفال", R.drawable.koky_logo))
//
//        cartoonList.add(ChannelModel("قناة كراميش للاطفال", R.drawable.caramish_logo))

        adapter.replaceData(cartoonList)
        cartoonsRv.adapter = adapter
        cartoonsRv.setHasFixedSize(true)
//        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//        itemDecorator.setDrawable(context.getDrawable(R.drawable.divider)!!)
//
//        cartoonsRv.addItemDecoration(itemDecorator)

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.cartoonItem -> {
                val item = (adapter?.data as List<ChannelModel>)[position]
                iCartoonCallback.selectedCartoon(item, this)
            }
        }
    }
}

interface ICartoonCallback {
    fun selectedCartoon(channelModel: ChannelModel?, channelsDialog: CartoonsDialog)
}