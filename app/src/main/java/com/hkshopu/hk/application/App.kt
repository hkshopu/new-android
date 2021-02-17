package com.hkshopu.hk.application

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.mallotec.reb.localeplugin.LocaleConstant
import com.mallotec.reb.localeplugin.LocalePlugin

class App : Application(), LifecycleOwner {

    override fun onCreate() {
        super.onCreate()
        instance = this
        LocalePlugin.init(this, LocaleConstant.RECREATE_CURRENT_ACTIVITY)
    }

    companion object {
        lateinit var instance: App
            private set
    }

    val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}