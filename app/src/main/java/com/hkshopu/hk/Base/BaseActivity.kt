package com.hkshopu.hk.Base

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.mallotec.reb.localeplugin.utils.LocaleHelper

open class BaseActivity : AppCompatActivity() {

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.setLocale(LocaleHelper.getInstance().getSetLocale())
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}