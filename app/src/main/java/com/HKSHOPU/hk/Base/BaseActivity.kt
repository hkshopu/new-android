package com.HKSHOPU.hk.Base

import android.content.res.Configuration
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner


open class BaseActivity : AppCompatActivity(),LifecycleOwner {
    var isForground = true
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onResume() {
        super.onResume()
        isForground = true

    }

    override fun onPause() {
        super.onPause()
        isForground = false

    }

    fun toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        if (isForground) {
            Toast.makeText(this, msg, duration).show()
        }
    }
}