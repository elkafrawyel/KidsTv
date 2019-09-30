package com.kids.funtv.ui.main

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.kids.funtv.MyApp
import com.kids.funtv.R
import com.kids.funtv.common.CustomLoadMoreView
import com.kids.funtv.model.SearchItem
import com.kids.funtv.model.SearchResponse
import com.kids.funtv.model.VideoModel
import com.kids.funtv.ui.player.PlayerActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainActivity : AppCompatActivity(), BaseQuickAdapter.OnItemChildClickListener {

    private val part = "snippet"
    private var pageToken: String? = null
    private var searchQuery: String = "Cartoon Network"

    lateinit var interstitialAd: InterstitialAd

    private val adapterSearch: AdapterSearch = AdapterSearch(mutableListOf()).also {
        it.onItemChildClickListener = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)

        videosRv.setHasFixedSize(true)
        videosRv.adapter = adapterSearch

        adapterSearch.setEnableLoadMore(true)
        adapterSearch.setOnLoadMoreListener({ searchYoutube(true) }, videosRv)
        adapterSearch.setLoadMoreView(CustomLoadMoreView())

        searchYoutube()

        refresh.setOnClickListener {
            refresh.visibility = View.GONE
            searchYoutube()
        }

        adView.loadAd(
            AdRequest.Builder()
                .addTestDevice("410E806C439261CF851B922E62D371EB")
                .build()
        )

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = "ca-app-pub-5669751081498672/4802194956"
//        interstitialAd.loadAd(AdRequest.Builder().build())
        interstitialAd.loadAd(AdRequest.Builder().addTestDevice("410E806C439261CF851B922E62D371EB").build())

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

//                        interstitialAd.loadAd(AdRequest.Builder().build())
                        interstitialAd.loadAd(AdRequest.Builder().addTestDevice("410E806C439261CF851B922E62D371EB").build())
                    }

                    override fun onAdClicked() {
//                        interstitialAd.loadAd(AdRequest.Builder().build())
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

    private fun searchYoutube(loadMore: Boolean = false) {

        refresh.visibility = View.GONE
        if (!loadMore)
            loading.visibility = View.VISIBLE

        val call = MyApp.createApiService()
            .search(
                key = resources.getString(R.string.key),
                part = part,
                search = searchQuery,
                type = "video",
                pageToken = pageToken,
                maxResult = 20
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
