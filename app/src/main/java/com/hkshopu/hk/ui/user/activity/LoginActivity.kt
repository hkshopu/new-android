package com.hkshopu.hk.ui.user.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.databinding.ActivityLoginBinding
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable



class LoginActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityLoginBinding
    var email: String = ""

    var to: Int = 0
    private val VM = AuthVModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
        initVM()
        initClick()
    }

    override fun afterTextChanged(s: Editable?) {
        val email = binding.editEmail.text.toString()
        val password = binding.password1.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            binding.btnLogin.disable()
        } else {
            binding.btnLogin.enable()
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
            binding.editEmail.setText(email)
            binding.password1.requestFocus()
            KeyboardUtil.showKeyboard(binding.password1)
        }

    }

    private fun initClick() {
        binding.titleBack.setOnClickListener {

            finish()
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.password1.text.toString()
            VM.login(this, email, password)
        }

        binding.goRetrieve.setOnClickListener {


        }

        binding.goRegister.setOnClickListener {
            val intent = Intent(this, BuildAccountActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun initEditText() {
        binding.editEmail.addTextChangedListener(this)
        binding.password1.addTextChangedListener(this)
    }

}