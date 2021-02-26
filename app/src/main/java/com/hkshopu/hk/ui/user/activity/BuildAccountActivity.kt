package com.hkshopu.hk.ui.user.activity

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.databinding.ActivityBuildacntBinding
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable



class BuildAccountActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityBuildacntBinding
    private val VM = AuthVModel()
    var email: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildacntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initVM()
        initClick()
    }

    override fun afterTextChanged(s: Editable?) {

        val email = binding.editEmailReg.text.toString()
        val password = binding.passwordReg.text.toString()
        val passwordcof = binding.passwordConf.text.toString()
        if (email.isEmpty() || password.isEmpty() || passwordcof.isEmpty()) {
            binding.tvNext.disable()
        } else {
            binding.tvNext.enable()
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

        initEditText()
        initClick()

        KeyboardUtil.showKeyboard(binding.editEmailReg)


    }

    private fun initClick() {
        binding.titleBack.setOnClickListener {

            finish()
        }
        binding.showPassBtn.setOnClickListener {
            ShowHidePass(it)
        }
        binding.showPassconfBtn.setOnClickListener {
            ShowHidePass(it)
        }
    }

    private fun initEditText() {
        binding.editEmailReg.addTextChangedListener(this)
        binding.passwordReg.addTextChangedListener(this)
        binding.passwordReg.setFilters(arrayOf<InputFilter>(LengthFilter(16)))
        binding.passwordReg.setTransformationMethod(PasswordTransformationMethod.getInstance())
        binding.passwordConf.addTextChangedListener(this)
        binding.passwordConf.setFilters(arrayOf<InputFilter>(LengthFilter(16)))
        binding.passwordConf.setTransformationMethod(PasswordTransformationMethod.getInstance())
    }
    fun ShowHidePass(view: View) {
        if (view.getId() === R.id.show_pass_btn) {
            if (binding.passwordReg.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())
            ) {
                (view as ImageView).setImageResource(R.mipmap.ic_eyeon)
                //Show Password
                binding.passwordReg.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            } else {
                (view as ImageView).setImageResource(R.mipmap.ic_eyeoff)
                //Hide Password
                binding.passwordReg.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
        }
        if (view.getId() === R.id.show_passconf_btn) {
            if (binding.passwordConf.getTransformationMethod()
                    .equals(PasswordTransformationMethod.getInstance())
            ) {
                (view as ImageView).setImageResource(R.mipmap.ic_eyeon)
                //Show Password
                binding.passwordConf.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            } else {
                (view as ImageView).setImageResource(R.mipmap.ic_eyeoff)
                //Hide Password
                binding.passwordConf.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
        }
    }
}