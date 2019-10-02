package com.kids.funtv.ui.player

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.gms.ads.AdRequest
import com.kids.funtv.MyApp
import com.kids.funtv.R
import com.kids.funtv.common.CustomLoadMoreView
import com.kids.funtv.common.changeLanguage
import com.kids.funtv.data.model.SearchItem
import com.kids.funtv.data.model.SearchResponse
import com.kids.funtv.data.model.VideoModel
import com.kids.funtv.ui.main.AdapterSearch
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PlayerActivity : AppCompatActivity(), BaseQuickAdapter.OnItemChildClickListener {

    private var videoId: String? = null
    private var videoTitle: String? = null
    var mYouTubePlayer: YouTubePlayer? = null
    var mYouTubePlayerTracker: YouTubePlayerTracker? = null
    var lastPlayedPosition: Float = 0F

    private val part = "snippet"
    private var pageToken: String? = null

    private val adapterSearch: AdapterSearch = AdapterSearch(mutableListOf()).also {
        it.onItemChildClickListener = this
    }

    companion object {

        const val ID = "videoId"
        const val TITLE = "videoTitle"

        fun start(context: Context, video: VideoModel) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(ID, video.searchItem!!.id.videoId)
            intent.putExtra(TITLE, video.searchItem!!.snippet.title)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLanguage()
        setContentView(R.layout.activity_player)


        videoId = intent.getStringExtra(ID)
        videoTitle = intent.getStringExtra(TITLE)

        if (videoId == null || videoTitle == null) {
            finish()
        }else {

            playVideo()

            youtubeView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {
                    hideSystemUI()
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    adView.visibility =View.GONE
                }

                override fun onYouTubePlayerExitFullScreen() {
                    showSystemUI()
                    adView.visibility =View.VISIBLE
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            })

            backImage.setOnClickListener {
                finish()
            }

            refresh.setOnClickListener {
                refresh.visibility = View.GONE
                getRelatedVideos()
            }

            videosRv.setHasFixedSize(true)
            videosRv.adapter = adapterSearch

            adapterSearch.setEnableLoadMore(true)
            adapterSearch.setOnLoadMoreListener({ getRelatedVideos(true) }, videosRv)
            adapterSearch.setLoadMoreView(CustomLoadMoreView())

            adView.loadAd(
                AdRequest.Builder()
                    .addTestDevice("410E806C439261CF851B922E62D371EB")
                    .build()
            )
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.videoItem -> {
                val video = (adapter?.data as List<VideoModel>)[position]
                videoId = video.searchItem!!.id.videoId
                videoTitle = video.searchItem!!.snippet.title
                adapterSearch.data.clear()
                adapterSearch.notifyDataSetChanged()
                playVideo()
            }
        }
    }

    private fun playVideo() {
        videoTitleEt.text = videoTitle
        getRelatedVideos()

        if (mYouTubePlayer != null) {
            mYouTubePlayer!!.loadVideo(videoId!!, 0f)
            mYouTubePlayerTracker = YouTubePlayerTracker()
            mYouTubePlayer!!.addListener(mYouTubePlayerTracker!!)
        } else {
            lifecycle.addObserver(youtubeView)

            youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    mYouTubePlayer = youTubePlayer
                    youTubePlayer.loadVideo(videoId!!, lastPlayedPosition)
                    mYouTubePlayerTracker = YouTubePlayerTracker()
                    mYouTubePlayer!!.addListener(mYouTubePlayerTracker!!)

                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    when (state) {
                        PlayerConstants.PlayerState.UNKNOWN -> {

                        }
                        PlayerConstants.PlayerState.UNSTARTED -> {

                        }
                        PlayerConstants.PlayerState.ENDED -> {

                        }
                        PlayerConstants.PlayerState.PLAYING -> {

                        }
                        PlayerConstants.PlayerState.PAUSED -> {

                        }
                        PlayerConstants.PlayerState.BUFFERING -> {

                        }
                        PlayerConstants.PlayerState.VIDEO_CUED -> {

                        }
                    }
                }

            })
        }

    }

    override fun onBackPressed() {
        if (youtubeView.isFullScreen()) {
            youtubeView!!.exitFullScreen()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @SuppressLint("InlinedApi")
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    private fun getRelatedVideos(loadMore: Boolean = false) {
        if (!loadMore)
            loading.visibility = View.VISIBLE

        val call = MyApp.createApiService()
            .getRelatedVideos(
                key = resources.getString(R.string.key),
                part = part,
                type = "video",
                pageToken = pageToken,
                maxResult = 10,
                relatedToVideoId = videoId!!
            )

        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                loading.visibility = View.GONE
                refresh.visibility = View.GONE
                if (response.isSuccessful && response.code() == 200) {
                    val videos = response.body()!!.items
                    pageToken = response.body()!!.nextPageToken
                    setVideos(videos)
                } else {
                    refresh.visibility = View.VISIBLE
                    adapterSearch.loadMoreFail()
                    toast(getString(R.string.error_message))
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                loading.visibility = View.GONE
                refresh.visibility = View.VISIBLE
                toast(getString(R.string.error_message))
            }
        })
    }

    private fun setVideos(videos: List<SearchItem>, loadMore: Boolean = false) {
        if (loadMore) {
            adapterSearch.data.clear()
            adapterSearch.replaceData(addGoogleAdsType(videos))
        } else {
            adapterSearch.addData(addGoogleAdsType(videos))
        }
        adapterSearch.loadMoreComplete()

        adapterSearch.notifyDataSetChanged()
    }

    private fun addGoogleAdsType(items: List<SearchItem>): ArrayList<VideoModel> {
        val videosList: ArrayList<VideoModel> = arrayListOf()
        items.forEachIndexed { index, searchItem ->
            if ((index) % 5 == 0 && index != 0) {
                videosList.add(VideoModel(null, 1))
            } else {
                videosList.add(VideoModel(searchItem, 0))
            }
        }

        return videosList
    }
}
