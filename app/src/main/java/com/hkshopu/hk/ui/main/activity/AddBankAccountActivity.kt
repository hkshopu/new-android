package com.hkshopu.hk.ui.main.activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View

import com.hkshopu.hk.Base.BaseActivity

import com.hkshopu.hk.R
import com.hkshopu.hk.component.EventPhoneShow
import com.hkshopu.hk.component.EventShopCatSelected
import com.hkshopu.hk.databinding.*

import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.utils.rxjava.RxBus
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.tencent.mmkv.MMKV
import com.zilchzz.library.widgets.EasySwitcher


class AddBankAccountActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityAddbankaccountBinding

    private val VM = AuthVModel()

    var bankCode: String = ""
    var bankName: String = ""
    var accountName: String = ""
    var accountNumber: String = ""
    private lateinit var settings: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddbankaccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settings = this.getSharedPreferences("shopdata", 0)
        initView()
        initVM()
        initClick()

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(p0: Editable?) {
        bankCode = binding.etBankcode.text.toString()
        bankName = binding.etBankname.text.toString()
        accountName = binding.etBankaccountname.text.toString()
        accountNumber = binding.etBankaccountnumber.text.toString()
    }

    private fun initView() {
        binding.etBankcode.addTextChangedListener(this)
        binding.etBankname.addTextChangedListener(this)
        binding.etBankaccountname.addTextChangedListener(this)
        binding.etBankaccountnumber.addTextChangedListener(this)

        binding.layoutBankaccountEdit.setOnClickListener {
            KeyboardUtil.hideKeyboard(it)
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
    val editor = settings.edit()
    private fun initClick() {
        binding.ivBack.setOnClickListener {

            finish()
        }

        binding.tvToaddshopaddress.setOnClickListener {
            editor.putString("bankcode",bankCode)
            editor.putString("bankname",bankName)
            editor.putString("accountname",accountName)
            editor.putString("accountnumber",accountNumber)
            editor.apply()
            val intent = Intent(this, AddShopAddressActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}