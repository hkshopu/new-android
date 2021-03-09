package com.hkshopu.hk.ui.user.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.databinding.ActivityAddresseditBinding
import com.hkshopu.hk.databinding.ActivityUserinfoBinding
import com.hkshopu.hk.ui.main.activity.ShopmenuActivity
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.KeyboardUtil
import java.util.*


class AddressEditActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityAddresseditBinding
    private val VM = AuthVModel()
    private lateinit var settings: SharedPreferences
    var email : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddresseditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initVM()
        initClick()
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    private fun initVM() {
        VM.registerLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {
                    if (it.data.toString().equals("註冊成功!")) {

                        VM.verifycode(this, email!!)

                    } else {
                        val text1: String = it.data.toString() //設定顯示的訊息
                        val duration1 = Toast.LENGTH_SHORT //設定訊息停留長短
                        Toast.makeText(this, text1,duration1)
                    }

                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })
        VM.verifycodeLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {
                    if (it.data.toString().equals("已寄出驗證碼")) {
                        val intent = Intent(this, EmailVerifyActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val text1: String = it.data.toString() //設定顯示的訊息
                        val duration1 = Toast.LENGTH_SHORT //設定訊息停留長短
                        Toast.makeText(this, text1,duration1)
                    }

                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })
    }

    private fun initView() {

        initEditText()
        initClick()

        KeyboardUtil.showKeyboard(binding.editCountry)


    }

    private fun initClick() {
        binding.titleBack.setOnClickListener {

            finish()
        }
        settings = this.getSharedPreferences("DATA", 0)
        binding.tvNext.setOnClickListener {
            email = settings.getString("email", "")
            val password = settings.getString("password", "")
            val passwordconf = settings.getString("passwordconf", "")
            val country = binding.editCountry.text.toString()
            val admin = binding.editAdmin.text.toString()
            val street = binding.editthoroughfare.text.toString()
            val door = binding.editfeature.text.toString()
            val subaddress = binding.editsubaddress.text.toString()
            val floor = binding.editfloor.text.toString()
            val room = binding.editroom.text.toString()
            var address = country+admin+street+door+subaddress+floor+room
            VM.register(
                this,
                "",
                email!!,
                password!!,
                passwordconf!!,
                "",
                "",
                "",
                "",
                "",
                address
            )
        }


        binding.tvSkip.setOnClickListener {
            val email = settings.getString("email", "")
            val password = settings.getString("password", "")
            val passwordconf = settings.getString("passwordconf", "")
            val country = binding.editCountry.text.toString()
            val admin = binding.editAdmin.text.toString()
            val street = binding.editthoroughfare.text.toString()
            val door = binding.editfeature.text.toString()
            val subaddress = binding.editsubaddress.text.toString()
            val floor = binding.editfloor.text.toString()
            val room = binding.editroom.text.toString()
            var address = country+admin+street+door+subaddress+floor+room
            VM.register(
                this,
                "",
                email!!,
                password!!,
                passwordconf!!,
                "",
                "",
                "",
                "",
                "",
                address
            )
        }

    }

    private fun initEditText() {
        binding.editCountry.addTextChangedListener(this)
        binding.editAdmin.addTextChangedListener(this)
        binding.editthoroughfare.addTextChangedListener(this)
        binding.editfeature.addTextChangedListener(this)
        binding.editsubaddress.addTextChangedListener(this)
        binding.editfloor.addTextChangedListener(this)
        binding.editroom.addTextChangedListener(this)
    }

}