package com.HKSHOPU.hk.ui.main.buyer.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.databinding.*
import com.HKSHOPU.hk.ui.main.store.activity.OnBoardActivity
import com.paypal.android.sdk.payments.*
import org.json.JSONException
import java.math.BigDecimal

//import kotlinx.android.synthetic.main.activity_main.*

class UserNameEditActivity : BaseActivity() {

    private lateinit var binding: ActivityUsernameeditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsernameeditBinding.inflate(layoutInflater)
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