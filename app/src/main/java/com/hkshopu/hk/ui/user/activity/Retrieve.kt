package com.hkshopu.hk.ui.user.activity

import android.os.Bundle
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.databinding.ActivityRetrieveBinding
import com.hkshopu.hk.ui.user.dialog.NotificationDialogFragment

class Retrieve : BaseActivity() {

    private lateinit var binding: ActivityRetrieveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrieveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //notify
        NotificationDialogFragment().show(supportFragmentManager, "MyCustomFragment")

        initView()

    }
    private fun initView() {
        initClick()
        initEditText()



    }
    private fun initClick() {
        binding.titleBack.setOnClickListener {
            finish()
        }

    }
    private fun initEditText() {


    }



}