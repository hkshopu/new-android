package com.hkshopu.hk.ui.user.vm


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import com.hkshopu.hk.Base.BaseViewModel
import com.hkshopu.hk.Base.response.StatusResourceObserver
import com.hkshopu.hk.Base.response.UIDataBean
import com.hkshopu.hk.data.repository.AuthRepository

class AuthVModel : BaseViewModel() {


    private val repository = AuthRepository()
    val registerLiveData = MediatorLiveData<UIDataBean<Any>>()
    val socialloginLiveData = MediatorLiveData<UIDataBean<Any>>()
    val loginLiveData = MediatorLiveData<UIDataBean<Any>>()
    val resetLiveData = MediatorLiveData<UIDataBean<Any>>()

    fun sociallogin(lifecycleOwner: LifecycleOwner,email: String, facebook_account: String, google_account: String,apple_account: String) {
        repository.sociallogin(lifecycleOwner, email,facebook_account, google_account,apple_account)
                .subscribe(StatusResourceObserver(socialloginLiveData, silent = false))
    }

    fun register(lifecycleOwner: LifecycleOwner, account_name : String,email : String,password : String,confirm_password : String,first_name : String,last_name : String,gender : String,birthday : String,phone : String,  address: String) {
        repository.register(lifecycleOwner,account_name,email,password,confirm_password,first_name,last_name,gender,birthday,phone,address)
            .subscribe(StatusResourceObserver(registerLiveData))
    }

    fun login(lifecycleOwner: LifecycleOwner, phone: String, password: String) {
        repository.login(lifecycleOwner, phone, password)
            .subscribe(StatusResourceObserver(loginLiveData, silent = false))
    }

    fun reset(lifecycleOwner: LifecycleOwner, phone: String, password_orig: String, password: String) {
        repository.reset(lifecycleOwner, phone, password_orig, password)
                .subscribe(StatusResourceObserver(resetLiveData, silent = false))
    }



}