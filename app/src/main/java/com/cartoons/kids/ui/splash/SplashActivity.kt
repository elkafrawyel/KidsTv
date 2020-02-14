package com.cartoons.kids.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.cartoons.kids.R
import com.cartoons.kids.ui.channelsActivity.ChannelsActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val myFadeInAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        image.startAnimation(myFadeInAnimation)

        myFadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(Intent(this@SplashActivity, ChannelsActivity::class.java))
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }

}
