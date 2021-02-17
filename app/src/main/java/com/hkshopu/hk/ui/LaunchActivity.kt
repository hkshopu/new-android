package com.hkshopu.hk.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.R
import com.hkshopu.hk.ui.main.activity.ShopmenuActivity
import kotlinx.android.synthetic.main.activity_launch.*


class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        nextPage()
        initVM()
        initClick()

    }

    private fun nextPage() {
        val backgroundImage: ImageView = findViewById(R.id.launch_img_logo)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.start)
        backgroundImage.startAnimation(slideAnimation)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent()

            intent.setClass(this@LaunchActivity, ShopmenuActivity::class.java)

            startActivity(intent)
           finish()
        }, 3000)
    }

    private fun initVM() {

    }

    private fun initClick() {

    }
}