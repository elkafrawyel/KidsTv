package com.cartoons.kids.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.cartoons.kids.R
import com.cartoons.kids.common.loadWithPlaceHolder
import com.cartoons.kids.data.model.playlistModel.PlayListItem

class PlaylistBannerAdapter (private val clicked: (Int) -> Unit) : PagerAdapter() {

    private val playLists = ArrayList<PlayListItem>()

    override fun isViewFromObject(view: View, v: Any): Boolean {
        return view == v
    }

    override fun getCount(): Int {
        return playLists.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val cardView = LayoutInflater.from(container.context)
            .inflate(R.layout.image_slider_item, container, false) as CardView

        container.addView(cardView)

        val imageView = cardView.findViewById<ImageView>(R.id.imageSlider)

        imageView.loadWithPlaceHolder(playLists[position].snippet.thumbnails.medium.url)
        imageView.setOnClickListener { clicked(position) }
        return cardView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun submitList(imagesList: List<PlayListItem>) {
        playLists.clear()
        playLists.addAll(imagesList)
        notifyDataSetChanged()
    }

}