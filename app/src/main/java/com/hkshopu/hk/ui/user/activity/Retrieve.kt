package com.hkshopu.hk.ui.user.activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.databinding.ActivityRetrieveBinding
import com.hkshopu.hk.ui.main.activity.ShopmenuActivity
import com.hkshopu.hk.ui.user.fragmentdialog.BottomSheeFragment
import com.hkshopu.hk.ui.user.vm.AuthVModel
import java.util.*
import kotlin.concurrent.schedule


class Retrieve : BaseActivity(), TextWatcher {

    private lateinit var binding: ActivityRetrieveBinding
    private val VM = AuthVModel()

    var number1: String = ""
    var number2: String = ""
    var number3: String = ""
    var number4: String = ""
    var validation: String = ""

    private lateinit var settings: SharedPreferences
    var email : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrieveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //local端資料存取
        settings = getSharedPreferences("DATA",0)
        email = settings.getString("email", "").toString()

        initVM()
        initView()

    }


    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit


    private fun initVM() {

        VM.verifycodeLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {
                    if (it.data.toString().equals("已寄出驗證碼!")) {

                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()

                    } else {
                        val text1: String = it.data.toString() //設定顯示的訊息
                        val duration1 = Toast.LENGTH_SHORT //設定訊息停留長短
                        Toast.makeText(this, text1.toString(),duration1).show()
                    }
                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })

        VM.emailverifyLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {

                    if (it.data.toString().equals("驗證成功!")) {
                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, NewPasswordActivity::class.java)
                        startActivity(intent)

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

        //email
        binding.textViewEmail.setText(email!!)

        //notify
//        NotificationDialogFragment().show(supportFragmentManager, "MyCustomFragment")
        initEditText()
        initClick()

    }

    private fun initClick() {
        binding.titleBack.setOnClickListener {
            finish()
        }

        binding.btnResend.setOnClickListener {

            VM.verifycode(this, email!!)

        }

        binding.btnAuthenticate.setOnClickListener {

            number1 =  binding.edtAuthenticate01.text.toString()
            number2 = binding.edtAuthenticate02.text.toString()
            number3 =  binding.edtAuthenticate03.text.toString()
            number4 = binding.edtAuthenticate04.text.toString()

            validation = number1 + number2 +number3 + number4


            VM.emailverify(this, email!!, validation!!)

            binding.btnResend.setTextColor(Color.parseColor("#48484A"))
            Timer().schedule(60000) {
                binding.btnResend.setTextColor(Color.parseColor("#1DBCCF"))
            }

        }


        binding.termsOfService.setOnClickListener {

            val intent = Intent(this, TermsOfServiceActivity::class.java)
            startActivity(intent)

        }
    }

    private fun initEditText() {

        setNextFocus(binding.edtAuthenticate01,binding.edtAuthenticate02)
        setNextFocus(binding.edtAuthenticate02,binding.edtAuthenticate03)
        setNextFocus(binding.edtAuthenticate03,binding.edtAuthenticate04)

    }

    fun setNextFocus(nowEdit: EditText, nextEdit: EditText) {
        nowEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (nowEdit.getText().toString().length == 1) {
                    nextEdit.requestFocus()

                }
            }
        })
    }

}