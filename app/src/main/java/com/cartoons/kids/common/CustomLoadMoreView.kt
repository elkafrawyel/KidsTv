package com.cartoons.kids.common

import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.cartoons.kids.R

class CustomLoadMoreView : LoadMoreView() {

    override fun getLayoutId(): Int {
        //layout name that hold the custom views
        return R.layout.loading_view
    }

    override fun isLoadEndGone(): Boolean {
        return false
    }

    override fun getLoadingViewId(): Int {
        return R.id.load_more_loading_view
    }

    override fun getLoadFailViewId(): Int {
        return R.id.load_more_load_fail_view
    }

    override fun getLoadEndViewId(): Int {
        return R.id.load_more_end_view
    }
}