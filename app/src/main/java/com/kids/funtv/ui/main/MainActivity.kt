package com.kids.funtv.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elkafrawyel.CustomViews
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.kids.funtv.MyApp
import com.kids.funtv.R
import com.kids.funtv.common.CustomLoadMoreView
import com.kids.funtv.data.model.ChannelModel
import com.kids.funtv.data.model.SearchItem
import com.kids.funtv.data.model.SearchResponse
import com.kids.funtv.data.model.VideoModel
import com.kids.funtv.ui.player.PlayerActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), BaseQuickAdapter.OnItemChildClickListener {

    private val part = "snippet"
    private var pageToken: String? = null
    private var searchQuery: String = ""
    private val AR = "العربية"

    lateinit var interstitialAd: InterstitialAd

    private val adapterSearch: AdapterSearch = AdapterSearch(mutableListOf()).also {
        it.onItemChildClickListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        changeLanguage()
        setContentView(R.layout.activity_main)

        rootView.setLayout(videosRv)
        rootView.setVisible(CustomViews.LAYOUT)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.app_name)
        videosRv.setHasFixedSize(true)
        videosRv.adapter = adapterSearch

        adapterSearch.setEnableLoadMore(true)
        adapterSearch.setOnLoadMoreListener({ searchYoutube(true) }, videosRv)
        adapterSearch.setLoadMoreView(CustomLoadMoreView())

        openCartoonDialog()

        adView.loadAd(
            AdRequest.Builder()
                .addTestDevice("410E806C439261CF851B922E62D371EB")
                .build()
        )

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.interstitialAd)
        interstitialAd.loadAd(AdRequest.Builder().addTestDevice("410E806C439261CF851B922E62D371EB").build())

    }

    private fun openCartoonDialog() {
        val cartoonsDialog = CartoonsDialog(this, object : ICartoonCallback {
            override fun selectedCartoon(
                channelModel: ChannelModel?,
                channelsDialog: CartoonsDialog
            ) {
                if (channelModel != null && searchQuery != channelModel.name) {
                    searchQuery = channelModel.name
                    pageToken = null
                    adapterSearch.data.clear()
                    adapterSearch.notifyDataSetChanged()
                    searchYoutube()
                }
                channelsDialog.dismiss()
            }
        })

        cartoonsDialog.setOnDismissListener {
            if (searchQuery == "" ){
                searchQuery = "كارتون للاطفال + برامج اطفال"
                adapterSearch.data.clear()
                adapterSearch.notifyDataSetChanged()
                searchYoutube()
            }
        }
        cartoonsDialog.show()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.videoItem -> {

                if (interstitialAd.isLoaded) {
                    interstitialAd.show()
                } else {
                    PlayerActivity.start(
                        this@MainActivity,
                        (adapter?.data as List<VideoModel>)[position]
                    )
                }

                interstitialAd.adListener = object : AdListener() {
                    override fun onAdClosed() {
                        PlayerActivity.start(
                            this@MainActivity,
                            (adapter?.data as List<VideoModel>)[position]
                        )

                        interstitialAd.loadAd(AdRequest.Builder().addTestDevice("410E806C439261CF851B922E62D371EB").build())
                    }

                    override fun onAdClicked() {
                        interstitialAd.loadAd(AdRequest.Builder().addTestDevice("410E806C439261CF851B922E62D371EB").build())
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.menu_search)

        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView

            searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    searchView.isIconified = false
                    searchView.requestFocusFromTouch()

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                    return true
                }
            })

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null && query.isNotBlank()) {
                        searchQuery = query
                        adapterSearch.data.clear()
                        adapterSearch.notifyDataSetChanged()
                        searchYoutube()
                        KeyboardUtils.hideSoftInput(this@MainActivity)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_dialog -> {
                openCartoonDialog()
            }
        }
        return true
    }

    private fun searchYoutube(loadMore: Boolean = false) {

        if (!loadMore)
            rootView.setVisible(CustomViews.LOADING)

        Log.i("KidsApp",searchQuery)
        val call = MyApp.createApiService()
            .search(
                key = resources.getString(R.string.key),
                part = part,
                search = searchQuery,
                type = "video",
                pageToken = pageToken,
                maxResult = 50
//                regionCode = getString(R.string.regionCode)
            )

        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                if (response.isSuccessful && response.code() == 200) {
                    val videos = response.body()!!.items
                    pageToken = response.body()!!.nextPageToken
                    setVideos(videos)
                    rootView.setVisible(CustomViews.LAYOUT)
                } else {
                    rootView.setVisible(CustomViews.ERROR)
                    rootView.setErrorText(R.string.error_message)
                    adapterSearch.loadMoreFail()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                rootView.setVisible(CustomViews.INTERNET)
                rootView.retry {
                    rootView.setVisible(CustomViews.LAYOUT)
                    searchYoutube()
                }
            }
        })
    }

    private fun setVideos(videos: List<SearchItem>, loadMore: Boolean = false) {
        if (loadMore) {
            videosRv.recycledViewPool.clear()
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
