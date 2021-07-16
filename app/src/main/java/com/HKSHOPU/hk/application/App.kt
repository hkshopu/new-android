package com.HKSHOPU.hk.application

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.HKSHOPU.hk.BuildConfig

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import com.tencent.mmkv.MMKV

class App : Application(), LifecycleOwner {

    override fun onCreate() {
        super.onCreate()
        instance = this
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        initMMKV()
        val config = CheckoutConfig(
            application = this,
            clientId = "ATQnt-r5Gm45zeZaEkjaDG9qG3jpM2IDrzNqhXUzNJc1-0USna3hg5gt6lm73M5wyvbTQCG_1iD1KoZv",
            environment = Environment.LIVE,
            returnUrl = "com.hkshopu.hk://paypalpay",
            currencyCode = CurrencyCode.HKD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true,
                shouldFailEligibility = false
            )
        )
        PayPalCheckout.setConfig(config)
    }

    companion object {
        lateinit var instance: App
            private set
    }

    val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
    private fun initMMKV() {
        MMKV.initialize(this)
    }
}