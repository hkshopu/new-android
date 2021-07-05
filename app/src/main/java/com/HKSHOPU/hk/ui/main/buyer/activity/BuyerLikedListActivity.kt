package com.HKSHOPU.hk.ui.main.buyer.activity

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.data.bean.ProductLikedBean
import com.HKSHOPU.hk.data.bean.ProductShopPreviewBean

import com.HKSHOPU.hk.databinding.*
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.buyer.adapter.ProductLikedAdapter
import com.HKSHOPU.hk.ui.main.homepage.activity.SearchActivity
import com.HKSHOPU.hk.ui.main.homepage.adapter.ProductShopPreviewAdapter
import com.HKSHOPU.hk.widget.view.KeyboardUtil
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

import com.zilchzz.library.widgets.EasySwitcher
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*


class BuyerLikedListActivity : BaseActivity() {
    private lateinit var binding: ActivityBuyerCollectBinding
    val keyword = ""
    val REQUEST_CODE_SPEECH_INPUT = 1000
    var defaultLocale = Locale.getDefault()
    var currency: Currency = Currency.getInstance(defaultLocale)
    private val adapter = ProductLikedAdapter(currency)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerCollectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.isVisible = true
        initView()
        initClick()
        doGetLikedList(keyword)
    }

    private fun initView() {
        binding!!.etSearchKeyword.setOnEditorActionListener() { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val keyWord = binding!!.etSearchKeyword.text.toString()
                    if(keyWord.isNotEmpty()){
                        doGetLikedList(keyWord)
                        KeyboardUtil.hideKeyboard(v)
                    }
                    true
                }

                else -> false
            }
        }

        binding!!.ivMic.setOnClickListener {
            KeyboardUtil.showKeyboard(it)
        }
    }

    private fun speak() {
        // Intent to show speech to text dialogs
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...")

        // Start Intent
        try {
            // If there was no error
            // showing dialogs
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            // If there was some error

            // get Message of error and show
            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (requestCode != RESULT_OK && null != data) {
                    // get the text array from voice intent
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    // set the voice view
                    binding.etSearchKeyword.setText(result!![0])
                }
            }
        }
    }
    private fun initClick() {
        binding.ivBack.setOnClickListener {

            finish()
        }


    }
    private fun initRecyclerView(){


        val layoutManager = GridLayoutManager(this@BuyerLikedListActivity,2)
        binding.recyclerviewCollect.layoutManager = layoutManager

        binding.recyclerviewCollect.adapter = adapter
        adapter.itemClick = {

        }

    }

    private fun doGetLikedList(keyword: String) {
        var userId = MMKV.mmkvWithID("http").getString("UserId", "");
        var url = ApiConstants.API_HOST + "user_detail/user_liked/"

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ProductLikedBean>()
                list.clear()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("BuyerLikedListActivity", "返回資料 resStr：" + resStr)
                    Log.d("BuyerLikedListActivity", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonArray: JSONArray = json.getJSONArray("data")
                        Log.d("BuyerLikedListActivity", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val productLikedBean: ProductLikedBean =
                                Gson().fromJson(jsonObject.toString(), ProductLikedBean::class.java)
                            list.add(productLikedBean)
                        }

                    }

                    if(list.size > 0){

                        runOnUiThread {
                            adapter.setData(list)
                            initRecyclerView()
                            binding.progressBar.isVisible = false
                        }
                    }

                } catch (e: JSONException) {

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onErrorResponse(ErrorResponse: IOException?) {

            }
        })
        web.Do_GetBuyerRecordList(url, userId,keyword)
    }

}