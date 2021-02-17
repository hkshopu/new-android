package com.hkshopu.hk.ui.user.activity

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.titleBack
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity(), TextWatcher {

    private val VM = AuthVModel()
    var email: String = ""
    var gender: String = ""
    val genders = arrayListOf("Male", "Female")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        initView()
        initVM()
        initClick()
    }

    override fun afterTextChanged(s: Editable?) {
        val username = editUserName.text.toString()
        val email = editEmail_reg.text.toString()
        val password = password_reg.text.toString()
        val passwordcof = passwordConf.text.toString()
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordcof.isEmpty()) {
            btnSignUp.disable()
        } else {
            btnSignUp.enable()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    private fun initVM() {
        VM.registerLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {
//                    if (url.isNotEmpty()) {
//                        toast("登录成功")
//
//                    }

                    finish()
                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })
    }

    private fun initView() {
        layout_register.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> KeyboardUtil.hideKeyboard(v)
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        gender_spinner.adapter = adapter
        gender_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                Log.d("RegisterActivity", "你選的是" + genders[pos])
                gender = genders[pos]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        initEditText()
        initClick()
        editUserName.requestFocus()
        KeyboardUtil.showKeyboard(editUserName)
        password_reg.setFilters(arrayOf<InputFilter>(LengthFilter(16)))
        editmobile1.setFilters(arrayOf<InputFilter>(LengthFilter(8)))

    }

    private fun initClick() {
        titleBack.setOnClickListener {

            finish()
        }
        btnSignUp.setOnClickListener {
            val account_name = editUserName.text.toString()
            val email = editEmail_reg.text.toString()
            val password = password_reg.text.toString()
            val confirm_password = passwordConf.text.toString()
            val first_name = editFirstName.text.toString()
            val last_name = editlastName.text.toString()
            val birthday = editbirth.text.toString()
            val phone = editmobile.text.toString() + editmobile1.text.toString()
            val address = editaddress.text.toString()
            if (confirm_password.equals(password)) {
                VM.register(
                    this,
                    account_name,
                    email,
                    password,
                    confirm_password,
                    first_name,
                    last_name,
                    gender,
                    birthday,
                    phone,
                    address
                )
            } else {
//                toast(R.string.please_confirm_password)
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this, R.string.please_confirm_password, duration)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                toast.show()
                btnSignUp.disable()
            }

        }

    }

    private fun initEditText() {
        editEmail_reg.addTextChangedListener(this)
        password_reg.addTextChangedListener(this)
        passwordConf.addTextChangedListener(this)
    }

}