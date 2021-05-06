package com.hkshopu.hk.ui.main.store.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged

import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.component.EventAddShopBriefSuccess
import com.hkshopu.hk.component.EventChangeShopPhoneSuccess

import com.hkshopu.hk.databinding.ActivityPhoneeditBinding
import com.hkshopu.hk.net.ApiConstants
import com.hkshopu.hk.net.Web
import com.hkshopu.hk.net.WebListener

import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.utils.rxjava.RxBus
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.tencent.mmkv.MMKV
import com.zilchzz.library.widgets.EasySwitcher
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class PhoneEditActivity : BaseActivity(){
    private lateinit var binding: ActivityPhoneeditBinding

    private val VM = AuthVModel()
    var phone_country: String = ""
    var phone_number: String = ""
    var phone: String = ""
    var isphoneShow: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneeditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initVM()
        initClick()

    }

    private fun initView() {
        binding.editShopphoneNumber.doAfterTextChanged {
            phone_number = binding.editShopphoneNumber.text.toString()
            phone_country = binding.tvShopphoneCountry.text.toString()
            phone = phone_country + phone_number

        }
        binding.layoutPhoneEdit.setOnClickListener {
            KeyboardUtil.hideKeyboard(it)
        }
        val phoneShow: Boolean = MMKV.mmkvWithID("http").getBoolean("PhoneShow", false)
        if (phoneShow) {
            binding.switchview.openSwitcher()
        }

    }

    private fun initVM() {
//        VM.socialloginLiveData.observe(this, Observer {
//            when (it?.status) {
//                Status.Success -> {
//                    if (url.isNotEmpty()) {
//                        toast("登录成功")
//
//                    }
//
//                    finish()
//                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
//            }
//        })
    }

    private fun initClick() {
        binding.ivBack.setOnClickListener {

            finish()
        }

        binding.tvSave.setOnClickListener {
            doShopDesUpdate(phone_country,phone_number,isphoneShow)
        }
        binding.switchview.setOnStateChangedListener(object :
            EasySwitcher.SwitchStateChangedListener {
            override fun onStateChanged(isOpen: Boolean) {

                MMKV.mmkvWithID("http").putBoolean("PhoneShow", isOpen)
                    .putString("phone", phone)
                if(isOpen){
                    isphoneShow ="Y"
                }else{
                    isphoneShow ="N"
                }
            }
        })


    }

    private fun doShopDesUpdate(countrycode: String,phone: String, is_phone_show:String) {
        val shopId = MMKV.mmkvWithID("http").getInt("ShopId",0)
        var url = ApiConstants.API_PATH+"shop/"+shopId+"/update/"

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("PhoneEditActivity", "返回資料 resStr：" + resStr)
                    Log.d("PhoneEditActivity", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {
                        RxBus.getInstance().post(EventChangeShopPhoneSuccess(phone))
                        finish()
                    } else {
                        runOnUiThread {

                            Toast.makeText(this@PhoneEditActivity, ret_val.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: JSONException) {

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onErrorResponse(ErrorResponse: IOException?) {

            }
        })
        web.Do_ShopPhoneUpdate(url,countrycode,phone,is_phone_show)
    }


}