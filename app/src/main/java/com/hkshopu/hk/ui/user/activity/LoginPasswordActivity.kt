package com.hkshopu.hk.ui.user.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.databinding.ActivityLoginBinding
import com.hkshopu.hk.databinding.ActivityLoginPasswordBinding
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable
import org.jetbrains.anko.email

class LoginPasswordActivity : BaseActivity(), TextWatcher {

    private lateinit var binding: ActivityLoginPasswordBinding
    private val VM = AuthVModel()

    var email : String? = null
    var password : String? = null

    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //建立local端的儲存與取出
        settings = getSharedPreferences("DATA",0)
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
                        VM.verifycode(this, email!!)

                    }else {
                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_LONG).show()
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

                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_LONG ).show()
                        val intent = Intent(this, EmailVerifyActivity::class.java)
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

        initEditText()
        initClick()

    }

    private fun initClick() {

        binding.goRetrieve.setOnClickListener {

            val intent = Intent(this, Retrieve::class.java)
            startActivity(intent)
        }


        binding.btnLogin.setOnClickListener {

            password = binding.edtPassword.text.toString()

            VM.login(this, email!!, password!!)

        }

    }

    private fun initEditText() {

        binding.edtPassword.addTextChangedListener(this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit


    override fun afterTextChanged(s: Editable?) {

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