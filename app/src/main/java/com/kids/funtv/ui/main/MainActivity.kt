package com.kids.funtv.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.KeyboardUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN
import com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_LEFT
import com.elkafrawyel.CustomViews
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.kids.funtv.MyApp
import com.kids.funtv.R
import com.kids.funtv.common.CustomLoadMoreView
import com.kids.funtv.common.RunAfterTime
import com.kids.funtv.common.SpacesItemDecoration
import com.kids.funtv.common.changeLanguage
import com.kids.funtv.data.model.ChannelModel
import com.kids.funtv.data.model.SearchItem
import com.kids.funtv.data.model.SearchResponse
import com.kids.funtv.data.model.VideoModel
import com.kids.funtv.ui.player.PlayerActivity
import kotlinx.android.synthetic.main.activity_main.*
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
    private lateinit var viewModel: MainActivityViewModel

    private val adapterSearch: AdapterSearch = AdapterSearch(mutableListOf()).also {
        it.onItemChildClickListener = this
        it.openLoadAnimation(SLIDEIN_LEFT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLanguage()
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        rootView.setLayout(homeLn)
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

    private fun changeGridItems(spanCount: Int) {
        (videosRv.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapterSearch.data.size - 1 > position) {
                        if (adapterSearch.data[position].type == 1 && position != 0) {
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
//        RunAfterTime.after(
//            10000
//        ) {
//            if (interstitialAd.isLoaded)
//                interstitialAd.show()
//            else {
//                interstitialAd.loadAd(AdRequest.Builder().addTestDevice("410E806C439261CF851B922E62D371EB").build())
//            }
//        }
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
                    supportActionBar!!.title = searchQuery

                }
                channelsDialog.dismiss()
            }
        })

        cartoonsDialog.setOnDismissListener {
            if (searchQuery == "") {
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

                    interstitialAd.loadAd(AdRequest.Builder().addTestDevice("410E806C439261CF851B922E62D371EB").build())

                    PlayerActivity.start(
                        this@MainActivity,
                        (adapter?.data as List<VideoModel>)[position]
                    )
                }

                interstitialAd.adListener = object : AdListener() {
                    override fun onAdClosed() {

                        interstitialAd.loadAd(AdRequest.Builder().addTestDevice("410E806C439261CF851B922E62D371EB").build())

                        PlayerActivity.start(
                            this@MainActivity,
                            (adapter?.data as List<VideoModel>)[position]
                        )

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
            val searchSrcTextId = resources.getIdentifier("android:id/search_src_text", null, null)
            val searchEditText = searchView.findViewById(searchSrcTextId) as EditText
            searchEditText.hint = getString(R.string.search_hint)
            searchEditText.setTextColor(Color.WHITE)
            searchEditText.setHintTextColor(Color.WHITE)

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

                        searchView.clearFocus()
                        searchView.setQuery("", false)
                        searchView.isFocusable = false
                        searchItem.collapseActionView()
                        supportActionBar!!.title = searchQuery
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

        Log.i("KidsApp", searchQuery)
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
                    if (videos.isEmpty()) {
                        rootView.setVisible(CustomViews.EMPTY)
                    } else {
                        pageToken = response.body()!!.nextPageToken
                        setVideos(videos)
                        rootView.setVisible(CustomViews.LAYOUT)
                    }
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
            videosRv.scrollToPosition(0)
        } else {
            adapterSearch.addData(addGoogleAdsType(videos))
        }
        adapterSearch.loadMoreComplete()

        adapterSearch.notifyDataSetChanged()

    }

    private fun addGoogleAdsType(items: List<SearchItem>): ArrayList<VideoModel> {
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
