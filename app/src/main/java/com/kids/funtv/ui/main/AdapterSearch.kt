package com.kids.funtv.ui.main

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.kids.funtv.R
import com.kids.funtv.common.loadWithPlaceHolder
import com.kids.funtv.data.model.VideoModel

class AdapterSearch(data: MutableList<VideoModel>?) :
    BaseMultiItemQuickAdapter<VideoModel, BaseViewHolder>(data) {

    init {
        addItemType(0, R.layout.video_item_view)
        addItemType(1, R.layout.video_item_view_ad)
    }

    override fun convert(helper: BaseViewHolder, item: VideoModel) {

        helper.addOnClickListener(R.id.videoItem)

        when (helper.itemViewType) {

            0 -> {
                helper.getView<ImageView>(R.id.videoImage)
                    .loadWithPlaceHolder(item.searchItem!!.snippet.thumbnails.medium.url)
                helper.setText(R.id.videoTitle, item.searchItem!!.snippet.title)
            }

            1 -> {
                val adView = helper.getView<AdView>(R.id.searchAdView)

                adView.loadAd(
                    AdRequest.Builder()
                        .addTestDevice("410E806C439261CF851B922E62D371EB")
                        .build()
                )

                adView.adListener = object : AdListener() {

                    override fun onAdClosed() {

                    }

                    override fun onAdFailedToLoad(error: Int) {
                        adView.visibility = View.GONE
                    }

                    override fun onAdLeftApplication() {

                    }

                    override fun onAdOpened() {

                    }

                    override fun onAdLoaded() {
                        adView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun getVideoImage(videoId: String): String {
        return "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"
    }
}