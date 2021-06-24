package com.HKSHOPU.hk.ui.main.homepage.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.HKSHOPU.hk.R

import com.HKSHOPU.hk.data.bean.TopProductBean
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.homepage.adapter.TopProductAdapter
import com.HKSHOPU.hk.utils.rxjava.RxBus
import com.HKSHOPU.hk.widget.view.KeyboardUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import okhttp3.Response
import org.jetbrains.anko.find
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class RankingTopSaleTopFragment : Fragment() {

    companion object {
        fun newInstance(): RankingTopSaleTopFragment {
            val args = Bundle()
            val fragment = RankingTopSaleTopFragment()
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var topProduct :RecyclerView
    lateinit var progressBar: ProgressBar
    var defaultLocale = Locale.getDefault()
    var currency: Currency = Currency.getInstance(defaultLocale)
    private val adapter = TopProductAdapter(currency)
    var max_seq = 0
    var userId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ranking_hotsales, container, false)
        refreshLayout = v.find<SmartRefreshLayout>(R.id.refreshLayout)
        progressBar = v.find<ProgressBar>(R.id.progressBar_product_top)
        progressBar.isVisible = true

        val mode = "top_sale"
        var url = ApiConstants.API_HOST+"/product/"+mode+"/product_analytics_pages/"
        topProduct = v.find<RecyclerView>(R.id.recyclerview_top)
        getProductOverAll(url,userId,max_seq)

        initView()
        initEvent()

        return v
    }

    private fun initView(){

    }
    private fun initRefresh() {
        refreshLayout.setOnClickListener {
            KeyboardUtil.hideKeyboard(it)
        }
        refreshLayout.setOnRefreshListener {
//            VM.loadShop(this)
            refreshLayout.finishRefresh()
        }
        refreshLayout.setOnLoadMoreListener {

            val mode = "top_sale"
            var url = ApiConstants.API_HOST+"/product/"+mode+"/product_analytics_pages/"
            max_seq ++
            getProductOverAllMore(url,userId,max_seq)
//            VM.loadMore(this)
        }
    }
    @SuppressLint("CheckResult")
    fun initEvent() {
        RxBus.getInstance().toMainThreadObservable(requireActivity(), Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
//                    is EventAddShopBriefSuccess -> {
//
//                    }
                }

            })
    }







    private fun initRecyclerView(){


        val layoutManager = GridLayoutManager(requireActivity(),2)
        topProduct.layoutManager = layoutManager

        topProduct.adapter = adapter
        adapter.itemClick = {

        }

    }

    private fun getProductOverAll(url: String,user_id:String,max_seq:Int) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<TopProductBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("RankingTopSaleTop", "返回資料 resStr：" + resStr)
                    Log.d("RankingTopSaleTop", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("已取得商品清單!")) {

                        val jsonArray: JSONArray = json.getJSONArray("data")
                        Log.d("RankingTopSaleTop", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val topProductBean: TopProductBean =
                                Gson().fromJson(jsonObject.toString(), TopProductBean::class.java)
                            list.add(topProductBean)
                        }

                    }

                    if(list.size > 0){
                        if(userId.isEmpty()){
                            userId = list[0].user_id
                        }
                        activity!!.runOnUiThread {
                            adapter.setData(list)
                            initRecyclerView()
                            progressBar.isVisible = false
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
        web.Do_GetShopTopProduct(url,user_id,max_seq)
    }
    private fun getProductOverAllMore(url: String,user_id:String,max_seq:Int) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<TopProductBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonArray: JSONArray = json.getJSONArray("data")

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val topProductBean: TopProductBean =
                                Gson().fromJson(jsonObject.toString(), TopProductBean::class.java)
                            list.add(topProductBean)
                        }

                    }

                    if(list.size > 0){

                        activity!!.runOnUiThread {
                            adapter.add(list)

                        }
                    }
                    refreshLayout.finishLoadMore()
                } catch (e: JSONException) {

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onErrorResponse(ErrorResponse: IOException?) {

            }
        })
        web.Do_GetShopTopProduct(url,user_id,max_seq)
    }

}