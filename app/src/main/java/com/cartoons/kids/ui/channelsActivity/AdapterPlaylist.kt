package com.cartoons.kids.ui.channelsActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.cartoons.kids.R
import com.cartoons.kids.common.loadWithPlaceHolder
import com.cartoons.kids.data.model.PlayListDB

class AdapterPlaylist (private val clicked: (Int) -> Unit) : PagerAdapter() {

    private val playLists = ArrayList<PlayListDB>()

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

        imageView.loadWithPlaceHolder(playLists[position].image)
        imageView.setOnClickListener { clicked(position) }
        return cardView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun submitList(playLists: List<PlayListDB>) {
        this.playLists.clear()
        this.playLists.addAll(playLists)
        notifyDataSetChanged()
    }

}