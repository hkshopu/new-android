package com.HKSHOPU.hk.ui.main.buyer.activity

import android.content.Intent
import android.os.Bundle
import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.databinding.ActivityBuyerevaluationBinding
import com.HKSHOPU.hk.databinding.ActivityBuyerpaymethodBinding
import com.HKSHOPU.hk.databinding.ActivityShopevaluationBinding
import com.HKSHOPU.hk.ui.main.store.activity.ShopNotifyActivity

//import kotlinx.android.synthetic.main.activity_main.*

class BuyerPayMethodActivity : BaseActivity() {
    private lateinit var binding: ActivityBuyerpaymethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerpaymethodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVM()
        initClick()

    }

    private fun initVM() {

    }

    private fun initClick() {

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.layoutFps.setOnClickListener {
            val intent = Intent(this@BuyerPayMethodActivity, BuyerFpsAccountActivity::class.java)
            startActivity(intent)
        }
//
//        btn_Login.setOnClickListener {
//
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//
//        }
//
//        btn_Skip.setOnClickListener {
//            val intent = Intent(this, ShopmenuActivity::class.java)
//            startActivity(intent)
//        }

    }


}