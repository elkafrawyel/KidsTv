package com.kids.funtv.common

import android.os.Handler

class RunAfterTime {
    companion object {
        fun after(delay: Long, process: () -> Unit) {
            Handler().postDelayed({
                process()
            }, delay)
        }
    }
}