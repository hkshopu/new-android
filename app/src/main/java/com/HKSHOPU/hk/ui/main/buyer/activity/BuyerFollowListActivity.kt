package com.HKSHOPU.hk.ui.main.buyer.activity

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.data.bean.ProductLikedBean
import com.HKSHOPU.hk.data.bean.ProductShopPreviewBean
import com.HKSHOPU.hk.data.bean.StoreFollowBean

import com.HKSHOPU.hk.databinding.*
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.buyer.adapter.ProductLikedAdapter
import com.HKSHOPU.hk.ui.main.buyer.adapter.StoreFollowAdapter
import com.HKSHOPU.hk.ui.main.homepage.adapter.ProductShopPreviewAdapter
import com.HKSHOPU.hk.ui.main.homepage.adapter.StoreRecommendAdapter
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


class BuyerFollowListActivity : BaseActivity() {
    private lateinit var binding: ActivityBuyerFollowedBinding
    val keyword = ""

    private val adapter = StoreFollowAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerFollowedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.isVisible = true
        initView()
        initClick()
        doGetFollewList(keyword)
    }

    private fun initView() {
        binding!!.etSearchKeyword.setOnEditorActionListener() { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val keyWord = binding!!.etSearchKeyword.text.toString()
                    if(keyWord.isNotEmpty()){
                        doGetFollewList(keyWord)
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


    private fun initClick() {
        binding.ivBack.setOnClickListener {

            finish()
        }


    }
    private fun initRecyclerView(){


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerviewFollow.layoutManager = layoutManager

        binding.recyclerviewFollow.adapter = adapter
        adapter.itemClick = {

        }

    }

    private fun doGetFollewList(keyword: String) {
        var userId = MMKV.mmkvWithID("http").getString("UserId", "");
        var url = ApiConstants.API_HOST + "user_detail/user_followed/"

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<StoreFollowBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("BuyerFollowListActivity", "返回資料 resStr：" + resStr)
                    Log.d("BuyerFollowListActivity", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonArray: JSONArray = json.getJSONArray("data")
                        Log.d("BuyerFollowListActivity", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val storeFollowBean: StoreFollowBean =
                                Gson().fromJson(jsonObject.toString(), StoreFollowBean::class.java)
                            list.add(storeFollowBean)
                        }

                    }

                    if(list.size > 0){
                        adapter.setData(list)
                        runOnUiThread {
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