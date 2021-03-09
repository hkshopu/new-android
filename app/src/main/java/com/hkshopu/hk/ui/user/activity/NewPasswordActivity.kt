package com.hkshopu.hk.ui.user.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.databinding.ActivityLoginPasswordBinding
import com.hkshopu.hk.databinding.ActivityNewPasswordBinding
import com.hkshopu.hk.ui.main.activity.ShopmenuActivity
import com.hkshopu.hk.ui.user.vm.AuthVModel

class NewPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPasswordBinding
    private val VM = AuthVModel()

    private lateinit var settings: SharedPreferences
    var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //local端資料存取
        settings = getSharedPreferences("DATA",0)
        email = settings.getString("email", "").toString()

        initView()
        initVM()
    }

    private fun initVM() {

        VM.resetPasswordLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {

                    if (it.data.toString() == "密碼修改成功!") {
                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT ).show()
                        val intent = Intent(this, ShopmenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT ).show()
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

    }


    private fun initClick() {

        var password = binding.edtViewPasswordFirstInput.text.toString()
        var confirm_password = binding.edtViewPasswordSecondInput.text.toString()

        binding.btnLogin.setOnClickListener {
            VM.reset_password(this, email!!, password!!, confirm_password!!)
        }

    }

    private fun initEditText() {



    }

}