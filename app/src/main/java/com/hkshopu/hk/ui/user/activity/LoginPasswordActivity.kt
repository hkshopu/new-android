package com.hkshopu.hk.ui.user.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.databinding.ActivityLoginPasswordBinding
import com.hkshopu.hk.ui.main.activity.ShopmenuActivity
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable

class LoginPasswordActivity : BaseActivity(), TextWatcher {

    private lateinit var binding: ActivityLoginPasswordBinding
    private val VM = AuthVModel()

    var email: String = ""
    var password : String = ""
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //local資料存取
        settings = this.getSharedPreferences("DATA", 0)
        email = settings.getString("email", "").toString()

        initView()
        initVM()
        initClick()
    }

    private fun initVM() {
        VM.loginLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {

                    if (it.data.toString() == "登入成功!") {
                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT ).show()

                        val intent = Intent(this, ShopmenuActivity::class.java)
                        startActivity(intent)

                    }else {
                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT ).show()
                    }

                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })

        VM.verifycodeLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {
                    if (it.data.toString().equals("已寄出驗證碼!")) {

                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_LONG).show()
                        val intent = Intent(this, RetrieveEmailVerifyActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val text1: String = it.data.toString() //設定顯示的訊息
                        val duration1 = Toast.LENGTH_SHORT //設定訊息停留長短
                        Toast.makeText(this, text1,duration1).show()
                    }

                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })
    }

    private fun initView() {

        binding.txtViewLoginEmail.setText(email!!)

        binding.titleBack.setOnClickListener {

            finish()
        }

        initEditText()
        initClick()

    }

    private fun initClick() {

        binding.goRetrieve.setOnClickListener {
            VM.verifycode(this, email!!)
        }

        //hide showPassword eye and hidePassword eye show
        binding.showPassBtn.setOnClickListener {
            ShowHidePass(it)
        }

        binding.btnLogin.setOnClickListener {

            password = binding.edtPassword.text.toString()
            VM.login(this, email!!, password!!)

        }

    }

    private fun initEditText() {

//        binding.editEmail.addTextChangedListener(this)
        binding.edtPassword.addTextChangedListener(this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit


    override fun afterTextChanged(s: Editable?) {
//        val email = binding.editEmail.text.toString()
        password = binding.edtPassword.text.toString()
        if (password!!.isEmpty()) {
            binding.btnLogin.disable()
        } else {
            binding.btnLogin.enable()
        }
    }

    fun ShowHidePass(view: View) {
        if (view.getId() === R.id.show_pass_btn) {
            if (binding.edtPassword.getTransformationMethod()
                    .equals(PasswordTransformationMethod.getInstance())
            ) {
                (view as ImageView).setImageResource(R.mipmap.ic_eyeon)
                //Show Password
                binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            } else {
                (view as ImageView).setImageResource(R.mipmap.ic_eyeoff)
                //Hide Password
                binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
        }
    }
}