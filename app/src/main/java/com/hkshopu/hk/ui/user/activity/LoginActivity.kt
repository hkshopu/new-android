package com.hkshopu.hk.ui.user.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), TextWatcher {

    var email: String = ""

    var to: Int = 0
    private val VM = AuthVModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        initView()
        initVM()
        initClick()
    }

    override fun afterTextChanged(s: Editable?) {
        val email = editEmail.text.toString()
        val password = password1.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            btnLogin.disable()
        } else {
            btnLogin.enable()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    private fun initVM() {
        VM.loginLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {

                    finish()
                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })
    }

    private fun initView() {

        initEditText()
        initClick()
        if (email.isNotEmpty()) {
            editEmail.setText(email)
            password1.requestFocus()
            KeyboardUtil.showKeyboard(password1)
        }

    }

    private fun initClick() {
        titleBack.setOnClickListener {

            finish()
        }
        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = password1.text.toString()
            VM.login(this, email, password)
        }

        goRetrieve.setOnClickListener {


        }

        goRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun initEditText() {
        editEmail.addTextChangedListener(this)
        password1.addTextChangedListener(this)
    }

}