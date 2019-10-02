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
import com.kids.funtv.data.model.ChannelModel
import kotlinx.android.synthetic.main.channels_dialog.*

class CartoonsDialog(context: Context, private val iCartoonCallback: ICartoonCallback) :
    Dialog(context,R.style.CustomDialog),
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
        cartoonList.add(
            ChannelModel(
                "سبيستون",
                R.drawable.spacetoon_logo
            )
        )

        cartoonList.add(
            ChannelModel(
                "كارتون نت وورك بالعربية",
                R.drawable.cartoon_network_logo
            )
        )

        cartoonList.add(
            ChannelModel(
                "توم وجيري كامل اطفال",
                R.drawable.tom_jerry_logo
            )
        )

        cartoonList.add(
            ChannelModel(
                "قناة كوكي كيدز للاطفال",
                R.drawable.koky_logo
            )
        )

        cartoonList.add(
            ChannelModel(
                "قناة توتة و حدوتة للاطفال",
                R.drawable.tota_logo
            )
        )

        cartoonList.add(
            ChannelModel(
                " قناة ميكي ماوس للاطفال",
                R.drawable.miky_logo
            )
        )

        cartoonList.add(
            ChannelModel(
                "قناة براعم للاطفال",
                R.drawable.baraem_logo
            )
        )

        cartoonList.add(
            ChannelModel(
                "قناة كراميش  للاطفال",
                R.drawable.caramish_logo
            )
        )

        adapter.replaceData(cartoonList)
        cartoonsRv.adapter = adapter
        cartoonsRv.setHasFixedSize(true)

        close.setOnClickListener {
            this.dismiss()
        }

        this.setOnDismissListener {
            iCartoonCallback.selectedCartoon(null,this)
        }

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.cartoonItem -> {
                val item = (adapter?.data as List<ChannelModel>)[position]
                iCartoonCallback.selectedCartoon(item,this)
            }
        }
    }
}

interface ICartoonCallback {
    fun selectedCartoon(channelModel: ChannelModel?, channelsDialog: CartoonsDialog)
}