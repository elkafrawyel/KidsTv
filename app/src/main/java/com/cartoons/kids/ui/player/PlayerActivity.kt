package com.cartoons.kids.ui.player

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.cartoons.kids.R
import com.cartoons.kids.common.RunAfterTime
import com.cartoons.kids.data.model.VideoDB
import com.cartoons.kids.data.model.VideoModel
import com.cartoons.kids.ui.channelsActivity.AdapterVideos
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import kotlinx.android.synthetic.main.activity_player.*
import java.util.ArrayList
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_player.adView
import kotlinx.android.synthetic.main.activity_player.videosRv


class PlayerActivity : AppCompatActivity(), BaseQuickAdapter.OnItemChildClickListener {

    private var videos: ArrayList<VideoDB> = arrayListOf()
    private var videoPosition = 0

    var mYouTubePlayer: YouTubePlayer? = null
    var mYouTubePlayerTracker: YouTubePlayerTracker? = null
    var lastPlayedPosition: Float = 0F
    var mRewardedVideoAd: RewardedVideoAd? = null

    private val adapterVideos: AdapterVideos = AdapterVideos(
        mutableListOf()
    ).also {
        it.onItemChildClickListener = this
        it.openLoadAnimation(BaseQuickAdapter.SCALEIN)
    }

    companion object {

        const val VIDEOS = "videos"
        const val POSITION = "videoPosition"

        fun start(context: Context, videos: ArrayList<VideoDB>, position: Int) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(VIDEOS, videos)
            intent.putExtra(POSITION, position)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        changeLanguage()
        setContentView(R.layout.activity_player)


        videoPosition = intent.getIntExtra(POSITION, 0)
        videos = intent.getParcelableArrayListExtra(VIDEOS)!!


        playVideo()
        setVideosList()

        youtubeView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                hideSystemUI()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                adView.visibility = View.GONE
            }

            override fun onYouTubePlayerExitFullScreen() {
                showSystemUI()
                adView.visibility = View.VISIBLE
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        })

        backImage.setOnClickListener {
            finish()
        }

        videosRv.setHasFixedSize(true)
        videosRv.adapter = adapterVideos

        adView.loadAd(
            AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .addTestDevice("410E806C439261CF851B922E62D371EB")
                .build()
        )

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd!!.loadAd(
            getString(R.string.videoAd),
            AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .addTestDevice("410E806C439261CF851B922E62D371EB").build()
        )


        RunAfterTime.after(
            5000
        ) {
            if (!mRewardedVideoAd!!.isLoaded) {
                mRewardedVideoAd!!.loadAd(
                    getString(R.string.videoAd),
                    AdRequest.Builder()
                        .tagForChildDirectedTreatment(true)
                        .addTestDevice("410E806C439261CF851B922E62D371EB").build()
                )
            }
        }

        mRewardedVideoAd!!.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {
                playNextVideo()
                mRewardedVideoAd!!.loadAd(
                    getString(R.string.videoAd),
                    AdRequest.Builder()
                        .tagForChildDirectedTreatment(true)
                        .addTestDevice("410E806C439261CF851B922E62D371EB").build()
                )
            }

            override fun onRewardedVideoAdLeftApplication() {
            }

            override fun onRewardedVideoAdLoaded() {
//                    mRewardedVideoAd!!.show()
            }

            override fun onRewardedVideoAdOpened() {
            }

            override fun onRewardedVideoCompleted() {
            }

            override fun onRewarded(p0: RewardItem?) {
                mRewardedVideoAd!!.loadAd(
                    getString(R.string.videoAd),
                    AdRequest.Builder()
                        .tagForChildDirectedTreatment(true)
                        .addTestDevice("410E806C439261CF851B922E62D371EB").build()
                )
            }

            override fun onRewardedVideoStarted() {
            }

            override fun onRewardedVideoAdFailedToLoad(p0: Int) {
            }
        }

    }

    private fun setVideosList() {
        setVideos(videos)

    }

    private fun playNextVideo() {
        if (videoPosition >= 0) {
            if (videoPosition == videos.size - 1)
                videoPosition = -1
            videoPosition++
        }
        playVideo()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.videoItem -> {
                if (videoPosition != position) {
                    videoPosition = position
                    playVideo()
                }
            }
        }
    }

    private fun playVideo() {
        videoTitleEt.text = videos[videoPosition].name!!

        if (mYouTubePlayer != null) {
            mYouTubePlayer!!.loadVideo(videos[videoPosition].id!!, 0f)
            mYouTubePlayerTracker = YouTubePlayerTracker()
            mYouTubePlayer!!.addListener(mYouTubePlayerTracker!!)
        } else {
            lifecycle.addObserver(youtubeView)

            youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    mYouTubePlayer = youTubePlayer
                    youTubePlayer.loadVideo(
                        videos[videoPosition].id!!,
                        lastPlayedPosition
                    )
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
                            if (mRewardedVideoAd!!.isLoaded) {
                                mRewardedVideoAd!!.show()
                            } else {
                                mRewardedVideoAd!!.loadAd(
                                    getString(R.string.videoAd),
                                    AdRequest.Builder()
                                        .tagForChildDirectedTreatment(true)
                                        .addTestDevice("410E806C439261CF851B922E62D371EB").build()
                                )
                                playNextVideo()
                            }
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

    private fun setVideos(videos: List<VideoDB>) {
        adapterVideos.replaceData(addGoogleAdsType(videos))
        adapterVideos.notifyDataSetChanged()
        loading.visibility = View.GONE
    }

    private fun addGoogleAdsType(items: List<VideoDB>): ArrayList<VideoModel> {
        val videosList: ArrayList<VideoModel> = arrayListOf()
        items.forEachIndexed { index, videoItem ->
            //            if ((index) % 5 == 0 && index != 0) {
//                videosList.add(VideoModel(null, 1))
//            } else {
            videosList.add(VideoModel(videoItem, 0))
//            }
        }

        return videosList
    }

    override fun onResume() {
        mRewardedVideoAd!!.resume(this)
        super.onResume()
    }

    override fun onPause() {
        mRewardedVideoAd!!.pause(this)
        super.onPause()
    }

    override fun onDestroy() {
        mRewardedVideoAd!!.destroy(this)
        super.onDestroy()
    }

}
