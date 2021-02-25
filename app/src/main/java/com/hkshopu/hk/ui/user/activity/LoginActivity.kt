package com.hkshopu.hk.ui.user.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.databinding.ActivityLaunchBinding
import com.hkshopu.hk.databinding.ActivityLoginBinding
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable



class LoginActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 900
    var email: String = ""

    var to: Int = 0
    private val VM = AuthVModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //remember me
        val sharedPreferences : SharedPreferences = getSharedPreferences("rememberMe", Context.MODE_PRIVATE)
        val checkRememberMe : String? = sharedPreferences.getString("rememberMe", "")
        if (checkRememberMe == "true") {
            //transfer to next page
        }

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

        //hide hidePassword eye and showPassword eye (default)
        binding.hidePassword.visibility = View.INVISIBLE

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
            val intent = Intent(this, Retrieve::class.java)
            startActivity(intent)
        }

        binding.checkBoxStayLogin.setOnClickListener {
            if (binding.checkBoxStayLogin.isChecked()) {
                val sharedPreferences : SharedPreferences = getSharedPreferences("rememberMe", Context.MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.apply {
                    putString("rememberMe", "true")
                }.apply()

            }else{
                val sharedPreferences : SharedPreferences = getSharedPreferences("rememberMe", Context.MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.apply {
                    putString("rememberMe", "false")
                }.apply()
            }
        }
//        binding.goRegister.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        binding.btnGoogleLogin.setOnClickListener {
                GoogleSignIn()
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
        binding.editEmail.addTextChangedListener(this)
        binding.password1.addTextChangedListener(this)
    }

    private fun GoogleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

}