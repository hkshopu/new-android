package com.hkshopu.hk.Base

import android.annotation.TargetApi
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.mallotec.reb.localeplugin.utils.LocaleHelper

open class BaseActivity : AppCompatActivity(),LifecycleOwner {
    var isForground = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableAutoFill();
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.setLocale(LocaleHelper.getInstance().getSetLocale())
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

    @TargetApi(Build.VERSION_CODES.O)
    private fun disableAutoFill() {
        getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
    }

    fun toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        if (isForground) {
            Toast.makeText(this, msg, duration).show()
        }
    }
}