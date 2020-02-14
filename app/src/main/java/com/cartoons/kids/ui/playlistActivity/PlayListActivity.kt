package com.cartoons.kids.ui.playlistActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.NetworkUtils
import com.cartoons.kids.MyApp
import com.cartoons.kids.R
import com.cartoons.kids.common.SpacesItemDecoration
import com.cartoons.kids.data.model.ChannelModel
import com.cartoons.kids.data.model.PlayListDB
import com.cartoons.kids.data.model.VideoDB
import com.cartoons.kids.data.model.VideoModel
import com.cartoons.kids.data.model.videoModel.VideoResponse
import com.cartoons.kids.data.storage.ChannelsDataBase
import com.cartoons.kids.ui.channelsActivity.AdapterVideos
import com.cartoons.kids.ui.player.PlayerActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elkafrawyel.CustomViews
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_play_list.*
import kotlinx.android.synthetic.main.activity_play_list.playlistTv
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class PlayListActivity : AppCompatActivity(), BaseQuickAdapter.OnItemChildClickListener {
    private var bigAdShowTime = 0
    lateinit var interstitialAd: InterstitialAd
    private lateinit var viewModel: PlaylistsViewModel
    private var playlists: ArrayList<PlayListDB> = arrayListOf()
    private var playlistItems: ArrayList<VideoDB> = arrayListOf()
    private var currentPlaylist: PlayListDB? = null
    private var blockRequest = false

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, PlayListActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val adapterVideos: AdapterVideos = AdapterVideos(mutableListOf()).also {
        it.onItemChildClickListener = this
        it.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)
        backImage.setOnClickListener {
            finish()
        }

        viewModel = ViewModelProviders.of(this).get(PlaylistsViewModel::class.java)

        rootView.setLayout(videosRv)
        rootView.setVisible(CustomViews.LAYOUT)

        videosRv.setHasFixedSize(true)
        videosRv.adapter = adapterVideos

        playlistTv.isSelected = true
//        openCartoonDialog()

        adapterVideos.data.clear()
        adapterVideos.notifyDataSetChanged()
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

        addPlaylists()
    }

    private fun addPlaylists() {
        val adapterPlaylists = ArrayAdapter<ChannelModel>(
            this,
            R.layout.spinner_item_view,
            ChannelsDataBase.getPlaylists()
        )

        adapterPlaylists.setDropDownViewResource(R.layout.spinner_dropped_item_view)
        playlistsSpinner.adapter = adapterPlaylists
        playlistsSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (NetworkUtils.isConnected()) {
                        if (!blockRequest)
                            getVideos(ChannelsDataBase.getPlaylists()[position].id)
                    } else {
                        rootView.setVisible(CustomViews.INTERNET)
                        rootView.retry {
                            if (!blockRequest)
                                getVideos(ChannelsDataBase.getPlaylists()[position].id)
                        }
                    }
                }
            }
    }

    private fun getVideos(playlistId: String) {
        val storedVideos = MyApp.getAppDatabase().dao().getVideosByPlaylist(playlistId)

        if (storedVideos == null || storedVideos.isEmpty()) {
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

                        val databaseVideos = response.body()!!.videoItems.map {

                            try {
                                VideoDB(
                                    id = it.snippet!!.resourceId!!.videoId!!,
                                    name = it.snippet.title!!,
                                    image = it.snippet.thumbnails!!.medium!!.url!!,
                                    playlistId = it.snippet.playlistId!!
                                )
                            } catch (ex: Exception) {
                                null
                            }
                        }
                        MyApp.getAppDatabase().dao().insertVideo(databaseVideos.filterNotNull())

                        playlistItems.clear()
                        playlistItems.addAll(databaseVideos.filterNotNull())
                        videosRv.recycledViewPool.clear()
                        setUpPlaylistVideos()
                    } else {
                        rootView.setVisible(CustomViews.ERROR)
                    }
                }

                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    rootView.setVisible(CustomViews.ERROR)
                    rootView.setEmptyText(getString(R.string.error_message))
                }
            })
        } else {
            //load from db
            adapterVideos.data.clear()
            adapterVideos.notifyDataSetChanged()
            adapterVideos.notifyDataSetChanged()

            playlistItems.clear()
            playlistItems.addAll(storedVideos)
            videosRv.recycledViewPool.clear()
            setUpPlaylistVideos()
        }


    }

    private fun setUpPlaylistVideos() {
        if (playlistItems.isEmpty()) {
            rootView.setVisible(CustomViews.EMPTY)
        } else {
            setVideosList(playlistItems)
            rootView.setVisible(CustomViews.LAYOUT)
        }
    }

    private fun setVideosList(videos: List<VideoDB>) {

        videosRv.recycledViewPool.clear()
        adapterVideos.replaceData(addGoogleAdsType(videos))
        adapterVideos.notifyDataSetChanged()
        videosRv.layoutManager!!.scrollToPosition(0)

    }

    private fun addGoogleAdsType(items: List<VideoDB>): ArrayList<VideoModel> {
        val videosList: ArrayList<VideoModel> = arrayListOf()
        items.forEachIndexed { index, videoDB ->
            //            if ((index) % 7 == 0 && index != 0) {
//                videosList.add(VideoModel(null, 1))
//            } else {
            videosList.add(VideoModel(videoDB, 0))
//            }
        }

        return videosList
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
                            this@PlayListActivity,
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
                                this@PlayListActivity,
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
                        this@PlayListActivity,
                        playlistItems, position
                    )
                }
            }
        }
    }
}
