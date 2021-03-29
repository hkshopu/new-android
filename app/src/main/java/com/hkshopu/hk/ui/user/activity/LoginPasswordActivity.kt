package com.hkshopu.hk.ui.user.activity


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.databinding.ActivityLoginPasswordBinding
import com.hkshopu.hk.net.ApiConstants
import com.hkshopu.hk.net.Web
import com.hkshopu.hk.net.WebListener
import com.hkshopu.hk.ui.main.activity.AddShopActivity
import com.hkshopu.hk.ui.main.activity.ShopmenuActivity
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable
import com.tencent.mmkv.MMKV
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginPasswordActivity : BaseActivity(), TextWatcher {

    private lateinit var binding: ActivityLoginPasswordBinding
    private val VM = AuthVModel()

    var getstring : String? = null
    var password : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)



        InitIntent()
        initView()
        initVM()
        initClick()
    }

    private fun initVM() {
        VM.loginLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {
//                    Log.d("LoginPasswordActivity", "LoginReturn value"+it.ret_val)
                    if (it.ret_val!!.equals("登入成功!")) {
                        runOnUiThread {
                            val intent = Intent(this, ShopmenuActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(this, it.ret_val.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }

                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })


    }

    private fun InitIntent() {

        //取得LoginPage傳來的email address
        getstring = intent.getStringExtra("email").toString()

    }
    private fun initView() {

        binding.txtViewLoginEmail.setText(getstring!!)

        initEditText()
        initClick()

    }

    private fun initClick() {
        binding.layoutPwd.setOnClickListener {
            KeyboardUtil.hideKeyboard(binding.txtViewLoginEmail)
        }

        binding.titleBack.setOnClickListener {

           finish()
        }

        binding.goRetrieve.setOnClickListener {


            //傳送email address給Retrieve Page
            var bundle = Bundle()
            bundle.putString("email", getstring)

            val intent = Intent(this, Retrieve::class.java)
            intent.putExtra("bundle", bundle)

            startActivity(intent)
        }

        //hide showPassword eye and hidePassword eye show
        binding.showPassword.setOnClickListener {
            binding.showPassword.visibility = View.INVISIBLE
            binding.hidePassword.visibility = View.VISIBLE
            binding.edtPassword.transformationMethod= PasswordTransformationMethod.getInstance()
        }

        //hide hidePassword eye and showPassword eye show
        binding.hidePassword.setOnClickListener {
            binding.hidePassword.visibility = View.INVISIBLE
            binding.showPassword.visibility = View.VISIBLE
            binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
        binding.btnLogin.setOnClickListener {


            password = binding.edtPassword.text.toString()
            val url = ApiConstants.API_HOST+"/user/loginProcess/"
//            VM.login(this, getstring!!, password!!)

            doLogin(url, getstring!!, password!!)


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

    private fun doLogin(url: String, email: String, password: String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("LoginPasswordActivity", "返回資料 resStr：" + resStr)
                    Log.d("LoginPasswordActivity", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("登入成功!")) {
                        var user_id: Int = json.getInt("user_id")
                        MMKV.mmkvWithID("http").putInt("UserId", user_id)
                        val intent = Intent(this@LoginPasswordActivity, ShopmenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@LoginPasswordActivity, ret_val.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
//                        initRecyclerView()


                } catch (e: JSONException) {

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onErrorResponse(ErrorResponse: IOException?) {

            }
        })
        web.Do_Login(url, email, password)
    }
}