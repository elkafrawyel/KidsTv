package com.cartoons.kids.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.NetworkUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_LEFT
import com.elkafrawyel.CustomViews
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.cartoons.kids.MyApp
import com.cartoons.kids.R
import com.cartoons.kids.common.RunAfterTime
import com.cartoons.kids.common.SpacesItemDecoration
import com.cartoons.kids.common.changeLanguage
import com.cartoons.kids.data.model.ChannelModel
import com.cartoons.kids.data.storage.ChannelsDataBase
import com.cartoons.kids.data.model.VideoModel
import com.cartoons.kids.data.model.playlistModel.PlayListItem
import com.cartoons.kids.data.model.playlistModel.PlayListResponse
import com.cartoons.kids.data.model.videoModel.VideoItem
import com.cartoons.kids.data.model.videoModel.VideoResponse
import com.cartoons.kids.ui.player.PlayerActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        var defaultChannelId = ChannelsDataBase.getChannelsList()[0]
    }

    private var timer: Timer? = null

    //show big ad when 3 times
    private var bigAdShowTime = 0
    lateinit var interstitialAd: InterstitialAd
    private lateinit var viewModel: MainActivityViewModel
    private var playlists: ArrayList<PlayListItem> = arrayListOf()
    private var playlistItems: ArrayList<VideoItem> = arrayListOf()
    private var currentChannelId: String? = null
    private var currentChannelIndex = 0
    private var currentPlaylistIndex = 0
    private var currentPlaylistId: String? = null
    private val playlistBannerAdapter =
        PlaylistBannerAdapter {
            if (playlists.isNotEmpty() && playlists[it].id != currentPlaylistId) {
                currentPlaylistId = playlists[it].id
                channelTv.text = playlists[it].snippet.channelTitle
                playlistTv.text = playlists[it].snippet.title
                getPlayListItems(currentPlaylistId!!)
            }
        }

    private val adapterVideos: AdapterVideos = AdapterVideos(mutableListOf()).also {
        it.onItemChildClickListener = this
        it.openLoadAnimation(SLIDEIN_LEFT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLanguage()
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        rootView.setLayout(videosRv)
        rootView.setVisible(CustomViews.LAYOUT)

        setSupportActionBar(toolbar)
        videosRv.setHasFixedSize(true)
        videosRv.adapter = adapterVideos

        playlistTv.isSelected = true
//        openCartoonDialog()

        adapterVideos.data.clear()
        adapterVideos.notifyDataSetChanged()
        if (NetworkUtils.isConnected()) {
            getPlayLists(defaultChannelId)
        } else {
            rootView.setVisible(CustomViews.INTERNET)
            bannerSliderVp.visibility = View.GONE
        }

        adView.loadAd(
            AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .addTestDevice("410E806C439261CF851B922E62D371EB")
                .build()
        )

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.interstitialAd)
        interstitialAd.loadAd(
            AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .addTestDevice("410E806C439261CF851B922E62D371EB").build()
        )


        //grid and list
        val spacesItemDecoration = SpacesItemDecoration(8)

        if (viewModel.isList) {
            listImgv.setImageDrawable(getDrawable(R.drawable.list))
            gridImgv.setImageDrawable(getDrawable(R.drawable.grid_not_selected))
            videosRv.layoutManager = GridLayoutManager(
                this,
                1,
                RecyclerView.VERTICAL,
                false
            )
            changeGridItems(1)

        } else {
            listImgv.background = getDrawable(R.drawable.list_not_selected)
            gridImgv.background = getDrawable(R.drawable.grid)

            videosRv.layoutManager = GridLayoutManager(
                this,
                2,
                RecyclerView.VERTICAL,
                false
            )
            videosRv.addItemDecoration(spacesItemDecoration)
            changeGridItems(2)
        }

        listImgv.setOnClickListener {
            if (!viewModel.isList) {
                listImgv.setImageDrawable(getDrawable(R.drawable.list))
                gridImgv.setImageDrawable(getDrawable(R.drawable.grid_not_selected))
                videosRv.post {
                    TransitionManager.beginDelayedTransition(videosRv)
                    (videosRv.layoutManager as GridLayoutManager).spanCount = 1
                    videosRv.removeItemDecoration(spacesItemDecoration)
                }

                changeGridItems(1)

                viewModel.isList = true
            }
        }

        gridImgv.setOnClickListener {
            if (viewModel.isList) {
                listImgv.setImageDrawable(getDrawable(R.drawable.list_not_selected))
                gridImgv.setImageDrawable(getDrawable(R.drawable.grid))
                videosRv.post {
                    TransitionManager.beginDelayedTransition(videosRv)
                    (videosRv.layoutManager as GridLayoutManager).spanCount = 2
                    videosRv.addItemDecoration(spacesItemDecoration)
                }
                viewModel.isList = false
                changeGridItems(2)
            }
        }
    }

    private fun getPlayLists(channelId: String) {
        val call = MyApp.createApiService()
            .getPlayLists(
                key = resources.getString(R.string.key),
                part = "snippet",
                channelId = channelId
            )

        call.enqueue(object : Callback<PlayListResponse> {
            override fun onResponse(
                call: Call<PlayListResponse>,
                response: Response<PlayListResponse>
            ) {

                if (response.isSuccessful && response.body() != null && response.code() == 200) {
                    playlists.clear()
                    playlists.addAll(response.body()!!.playLists)

                    setUpPlaylistSlider()
                } else {
                    Toast.makeText(this@MainActivity, "There is no Playlists", Toast.LENGTH_LONG)
                        .show()
                    bannerSliderVp.visibility = View.GONE

                    //load other source
                    getPlayListItems(ChannelsDataBase.getDefaultPlayList())
                }
            }

            override fun onFailure(call: Call<PlayListResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getPlayListItems(playlistId: String) {
        rootView.setVisible(CustomViews.LOADING)

        val call = MyApp.createApiService()
            .getPlayListItems(
                key = resources.getString(R.string.key),
                part = "snippet",
                playlistId = playlistId,
                maxResult = 50
            )

        call.enqueue(object : Callback<VideoResponse> {
            override fun onResponse(
                call: Call<VideoResponse>,
                response: Response<VideoResponse>
            ) {

                if (response.isSuccessful && response.body() != null && response.code() == 200) {
                    adapterVideos.data.clear()
                    adapterVideos.notifyDataSetChanged()
                    adapterVideos.notifyDataSetChanged()

                    playlistItems.clear()
                    playlistItems.addAll(response.body()!!.videoItems)
                    videosRv.recycledViewPool.clear()
                    setUpPlaylistVideos()
                    rootView.setVisible(CustomViews.LAYOUT)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.noPlaylists),
                        Toast.LENGTH_LONG
                    ).show()
                    bannerSliderVp.visibility = View.GONE
                    rootView.setVisible(CustomViews.ERROR)

                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }


    private fun setUpPlaylistVideos() {
        if (playlistItems.isEmpty()) {
            rootView.setVisible(CustomViews.EMPTY)
        } else {
            setVideosList(playlistItems)
            rootView.setVisible(CustomViews.LAYOUT)
        }
    }

    private fun setUpPlaylistSlider() {
        playlistBannerAdapter.submitList(playlists)
        bannerSliderVp.adapter = playlistBannerAdapter

        getPlayListItems(playlists[0].id)
        channelTv.text = playlists[0].snippet.channelTitle
        playlistTv.text = playlists[0].snippet.title

        rootView.setVisible(CustomViews.LAYOUT)
        bannerSliderVp.visibility = View.VISIBLE
    }

    private fun changeGridItems(spanCount: Int) {
        (videosRv.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapterVideos.data.size - 1 > position) {
                        if (adapterVideos.data[position].type == 1 && position != 0) {
                            spanCount
                        } else {
                            1
                        }
                    } else {
                        1
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()

        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            runOnUiThread {
                if (bannerSliderVp != null) {
                    if (bannerSliderVp.currentItem < playlistBannerAdapter.count - 1) {
                        bannerSliderVp.setCurrentItem(bannerSliderVp.currentItem + 1, true)
                    } else {
                        bannerSliderVp.setCurrentItem(0, true)
                    }
                }
            }
        }, 5000, 10000)
    }

    private fun openCartoonDialog() {
        val cartoonsDialog = CartoonsDialog(this, object : ICartoonCallback {
            override fun selectedCartoon(
                channelModel: ChannelModel?,
                channelsDialog: CartoonsDialog
            ) {
                if (channelModel != null && currentChannelId != channelModel.id) {
                    currentChannelId = channelModel.id
                    adapterVideos.data.clear()
                    adapterVideos.notifyDataSetChanged()
//                    getPlayLists(currentChannelId!!)
                    supportActionBar!!.title = channelModel.name
                }
                channelsDialog.dismiss()
            }
        })

        cartoonsDialog.setOnDismissListener {
            if (currentChannelId == null) {
                currentChannelId = defaultChannelId
                adapterVideos.data.clear()
                adapterVideos.notifyDataSetChanged()
//                getPlayLists(currentChannelId!!)
            }
        }
        cartoonsDialog.show()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.videoItem -> {

                if (bigAdShowTime == 3) {
                    bigAdShowTime = 0
                    if (interstitialAd.isLoaded) {
                        interstitialAd.show()
                    } else {

                        interstitialAd.loadAd(
                            AdRequest.Builder()
                                .tagForChildDirectedTreatment(true)
                                .addTestDevice("410E806C439261CF851B922E62D371EB").build()
                        )
                        PlayerActivity.start(
                            this@MainActivity,
                            playlistItems, position
                        )
                    }

                    interstitialAd.adListener = object : AdListener() {
                        override fun onAdClosed() {

                            interstitialAd.loadAd(
                                AdRequest.Builder()
                                    .tagForChildDirectedTreatment(true)
                                    .addTestDevice("410E806C439261CF851B922E62D371EB").build()
                            )

                            PlayerActivity.start(
                                this@MainActivity,
                                playlistItems, position
                            )

                        }

                        override fun onAdClicked() {
                            interstitialAd.loadAd(
                                AdRequest.Builder()
                                    .tagForChildDirectedTreatment(true)
                                    .addTestDevice("410E806C439261CF851B922E62D371EB").build()
                            )
                        }
                    }
                } else {
                    bigAdShowTime++
                    PlayerActivity.start(
                        this@MainActivity,
                        playlistItems, position
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_dialog -> {
//                openCartoonDialog()
                refreshHome()
            }
        }
        return true
    }

    private fun refreshHome() {
        if (NetworkUtils.isConnected()) {
            if (currentChannelIndex >= 0) {
                if (currentChannelIndex == ChannelsDataBase.getChannelsList().size - 1)
                    currentChannelIndex = -1
                currentChannelIndex++
            }
            currentChannelId = ChannelsDataBase.getChannelsList()[currentChannelIndex]

            getPlayLists(currentChannelId!!)
        } else {
            rootView.setVisible(CustomViews.INTERNET)
            bannerSliderVp.visibility = View.GONE
        }


    }

    private fun setVideosList(videos: List<VideoItem>) {

        val videosList = ArrayList<VideoItem>()
        videos.forEach {
            if (it.snippet!!.thumbnails != null) {
                videosList.add(it)
            }
        }

        videosRv.recycledViewPool.clear()
        adapterVideos.replaceData(addGoogleAdsType(videosList))
        adapterVideos.notifyDataSetChanged()
        videosRv.layoutManager!!.scrollToPosition(0)

    }

    private fun addGoogleAdsType(items: List<VideoItem>): ArrayList<VideoModel> {
        val videosList: ArrayList<VideoModel> = arrayListOf()
        items.forEachIndexed { index, searchItem ->
            if ((index) % 7 == 0 && index != 0) {
                videosList.add(VideoModel(null, 1))
            } else {
                videosList.add(VideoModel(searchItem, 0))
            }
        }

        return videosList
    }
}
