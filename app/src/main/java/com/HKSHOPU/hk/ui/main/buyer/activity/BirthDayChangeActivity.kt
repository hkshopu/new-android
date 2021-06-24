package com.HKSHOPU.hk.ui.main.buyer.activity


import android.os.Bundle

import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.databinding.*

import com.paypal.android.sdk.payments.*


//import kotlinx.android.synthetic.main.activity_main.*

class BirthDayChangeActivity : BaseActivity() {

    private lateinit var binding: ActivityBirthdaychangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBirthdaychangeBinding.inflate(layoutInflater)
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


    }

    public override fun onDestroy() {
        // Stop service when done

        super.onDestroy()
    }


}