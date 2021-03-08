package com.hkshopu.hk.ui.user.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.databinding.ActivityBuildacntBinding
import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.hkshopu.hk.widget.view.disable
import com.hkshopu.hk.widget.view.enable
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class BuildAccountActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityBuildacntBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 900;

    lateinit var callbackManager: CallbackManager
    private val VM = AuthVModel()
    private lateinit var settings: SharedPreferences
    var email: String = ""
    var password: String = ""
    var passwordconf: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildacntBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        initView()
        initVM()
        initClick()
    }

    override fun afterTextChanged(s: Editable?) {

        email = binding.editEmailReg.text.toString()
        password = binding.passwordReg.text.toString()
        passwordconf = binding.passwordConf.text.toString()
//        if (email.isEmpty() || password.isEmpty() || passwordcof.isEmpty()) {
//            binding.tvNext.disable()
//        } else {
//            binding.tvNext.enable()
//        }

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
        binding.ivGoogle.setOnClickListener {

            GoogleAccountBuild()
        }
        binding.ivFb.setOnClickListener {

            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(
                this, Arrays.asList("public_profile", "email")
            )
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val request =
                            GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                                Log.d("BuildAccountActivity", response.toString())
                                try {
                                    // Application code
                                    val id = response.jsonObject.getString("id")
                                    val email = response.jsonObject.getString("email")
                                    VM.sociallogin(this@BuildAccountActivity, email, id, "", "")
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email,gender,birthday")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Log.d("BuildAccountActivity", "Facebook onCancel.")

                    }

                    override fun onError(error: FacebookException) {
                        Log.d("BuildAccountActivity", "Facebook onError.")

                    }
                })
        }
        binding.showPassBtn.setOnClickListener {
            ShowHidePass(it)
        }
        binding.showPassconfBtn.setOnClickListener {
            ShowHidePass(it)
        }
        settings = getSharedPreferences("DATA",0)
        binding.tvNext.setOnClickListener {
            if(email.isNotEmpty() && password.isNotEmpty()) {
                settings.edit()
                    .putString("email", email)
                    .putString("password", password)
                    .putString("passwordconf", passwordconf)
                    .apply()
            }
            val intent = Intent(this, UserIofoActivity::class.java)
            startActivity(intent)
            finish()
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

    private fun GoogleAccountBuild() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                val email = account.email.toString()
                val id = account.id.toString()
                VM.sociallogin(this, email, "", id, "")
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.d("BuildAccountActivity", "Google sign in failed", e)
                // ...
            }
        }
    }

    fun ShowHidePass(view: View) {
        if (view.getId() === R.id.show_pass_btn) {
            if (binding.passwordReg.getTransformationMethod()
                    .equals(PasswordTransformationMethod.getInstance())
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