package com.hkshopu.hk.ui.user.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.databinding.ActivityLoginBinding
import com.hkshopu.hk.databinding.ActivityLoginPasswordBinding
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable

class LoginPasswordActivity : BaseActivity(), TextWatcher {

    private lateinit var binding: ActivityLoginPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {

        initEditText()
        initClick()

    }

    private fun initClick() {

        binding.goRetrieve.setOnClickListener {
            val intent = Intent(this, Retrieve::class.java)
            startActivity(intent)
        }

        //hide showPassword eye and hidePassword eye show
        binding.showPassword.setOnClickListener {
            binding.showPassword.visibility = View.INVISIBLE
            binding.hidePassword.visibility = View.VISIBLE
            binding.password1.transformationMethod= PasswordTransformationMethod.getInstance()
        }

        //hide hidePassword eye and showPassword eye show
        binding.hidePassword.setOnClickListener {
            binding.hidePassword.visibility = View.INVISIBLE
            binding.showPassword.visibility = View.VISIBLE
            binding.password1.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }

    }

    private fun initEditText() {

//        binding.editEmail.addTextChangedListener(this)
        binding.password1.addTextChangedListener(this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit


    override fun afterTextChanged(s: Editable?) {
//        val email = binding.editEmail.text.toString()
        val password = binding.password1.text.toString()
        if (password.isEmpty()) {
            binding.btnLogin.disable()
        } else {
            binding.btnLogin.enable()
        }
    }
}