package com.hkshopu.hk.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
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
import com.hkshopu.hk.data.bean.BoardingObjBean
import com.hkshopu.hk.databinding.ActivityOnboardBinding
import com.hkshopu.hk.ui.user.activity.LoginActivity
import com.hkshopu.hk.ui.user.activity.BuildAccountActivity
import com.hkshopu.hk.ui.user.vm.AuthVModel
import java.util.*
import kotlin.collections.ArrayList


class OnBoardActivity : BaseActivity(), ViewPager.OnPageChangeListener {
    private lateinit var binding: ActivityOnboardBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 900;
    lateinit var callbackManager: CallbackManager
    private val VM = AuthVModel()
    lateinit var points: ArrayList<ImageView> //指示器圖片
    val list = ArrayList<BoardingObjBean>()

    private fun setBoardingData() {
        var boardingObj = BoardingObjBean(R.mipmap.online_shopping, R.mipmap.online_shopping)
        list.add(boardingObj)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        setBoardingData()
        initViewPager()
        initVM()
        initClick()

    }

    private fun initViewPager() {
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

            }

        })

        binding.pager.adapter = ImageAdapter(list)
        binding.pager.addOnPageChangeListener(this)
        initPoints()

    }

    private fun initPoints() {
        points = arrayListOf()
        for (i in 0 until list.size) {
            val point = ImageView(this)
            point.setPadding(10, 10, 10, 10)
            point.scaleType = ImageView.ScaleType.FIT_XY

            if (i == 0) {
                point.setImageResource(R.drawable.banner_radius)
                point.layoutParams = ViewGroup.LayoutParams(96, 36)
            } else {
                point.setImageResource(R.drawable.banner_radius_unselect)
                point.layoutParams = ViewGroup.LayoutParams(36, 36)
            }

            binding.indicator.addView(point)
            points.add(point)
        }
    }

    private fun initVM() {
        VM.socialloginLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {
//                    Log.d("OnBoardActivity", "Sign-In Result" + it.data)
                    if (it.data.toString().isNotEmpty()) {
                        val intent = Intent(this, ShopmenuActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        val intent = Intent(this, BuildAccountActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })
    }

    private fun initClick() {
        binding.btnFb.setOnClickListener {
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(
                this, Arrays.asList("public_profile", "email")
            )
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                            Log.d("OnBoardActivity", response.toString())
                            try {
                                // Application code
                                val id = response.jsonObject.getString("id")
                                val email = response.jsonObject.getString("email")
                                VM.sociallogin(this@OnBoardActivity, email, id, "", "")
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
                        Log.d("OnBoardActivity", "Facebook onCancel.")

                    }

                    override fun onError(error: FacebookException) {
                        Log.d("OnBoardActivity", "Facebook onError.")

                    }
                })

        }

        binding.btnGoogle.setOnClickListener {

            GoogleSignIn()
        }

        binding.btnSignup.setOnClickListener {

            val intent = Intent(this, BuildAccountActivity::class.java)
            startActivity(intent)
        }

        binding.tvLogin.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        binding.btnSkip.setOnClickListener {
            val intent = Intent(this, ShopmenuActivity::class.java)
            startActivity(intent)
        }

    }


    private class ImageAdapter internal constructor(arrayList: ArrayList<BoardingObjBean>) :
        PagerAdapter() {
        private val arrayList: ArrayList<BoardingObjBean>


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater =
                container.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.boarding_view, null)
            val boardingObj: BoardingObjBean = arrayList[position]
            val imageView = view.findViewById<View>(R.id.image_view) as ImageView
            imageView.setImageResource(boardingObj.imageResId)
            if (position == 0) {
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            } else {
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            }

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return arrayList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        init {
            this.arrayList = arrayList
        }
    }

    private fun GoogleSignIn() {
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
                Log.d("OnBoardActivity", "Google sign in failed", e)
                // ...
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        for (i in 0 until points.size) {
            val params = points[position].layoutParams
            params.width = 96
            params.height = 36
            points[position].layoutParams = params
            points[position].setImageResource(R.drawable.banner_radius)

            if (position != i) {
                val params1 = points[i].layoutParams
                params1.width = 36
                params1.height = 36
                points[i].layoutParams = params1
                points[i].setImageResource(R.drawable.banner_radius_unselect)

            }
        }

    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}