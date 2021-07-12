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
import com.HKSHOPU.hk.component.EventToProductSearch
import com.HKSHOPU.hk.data.bean.ProductSearchBean

import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.homepage.adapter.ProductSearchAdapter
import com.HKSHOPU.hk.utils.rxjava.RxBus
import com.HKSHOPU.hk.widget.view.KeyboardUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import okhttp3.Response
import org.jetbrains.anko.find
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class RankingLatestSearchFragment : Fragment() {

    companion object {
        fun newInstance(): RankingLatestSearchFragment {
            val args = Bundle()
            val fragment = RankingLatestSearchFragment()
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var latestProduct :RecyclerView
    lateinit var progressBar: ProgressBar
    var defaultLocale = Locale.getDefault()
    var currency: Currency = Currency.getInstance(defaultLocale)
    private val adapter = ProductSearchAdapter(currency)
    var keyword = ""
    var categoryId = ""
    var sub_categoryId = ""
    var max_seq = 0
    var userId= ""
    var mode = "new"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ranking_latest, container, false)
        refreshLayout = v.find<SmartRefreshLayout>(R.id.refreshLayout)
        progressBar = v.find<ProgressBar>(R.id.progressBar_product_new)
        progressBar.isVisible = true

        latestProduct = v.find<RecyclerView>(R.id.recyclerview_ranklatest)

        initView()
        initRefresh()
        initEvent()
        return v
    }

    private fun initView(){
        progressBar.isVisible = true

        categoryId = MMKV.mmkvWithID("http").getString("product_category_id","").toString()
//        sub_categoryId = MMKV.mmkvWithID("http").getInt("sub_product_category_id",0)
        val url = ApiConstants.API_HOST+"/product/"+mode +"/product_analytics_pages_keyword/"
        getSearchProductOverAll(url,userId,categoryId,sub_categoryId,max_seq.toString(),keyword!!)
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

            var url = ApiConstants.API_HOST+"product/"+mode+"/product_analytics_pages_keyword"
            max_seq ++
            if(keyword.isNotEmpty()){
                categoryId = ""
            }else{
                keyword =""
            }
            getSearchProductOverAllMore(url,userId,categoryId,sub_categoryId.toString(),max_seq.toString(),keyword)
//            VM.loadMore(this)
        }
    }

    private fun initRecyclerView(){


        val layoutManager = GridLayoutManager(requireActivity(),2)
        latestProduct.layoutManager = layoutManager

        latestProduct.adapter = adapter
        adapter.itemClick = {

        }

    }

    @SuppressLint("CheckResult")
    fun initEvent() {
        RxBus.getInstance().toMainThreadObservable(requireActivity(), Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventToProductSearch -> {
                        progressBar.isVisible = true
                        keyword = MMKV.mmkvWithID("http").getString("keyword","").toString()
                        Log.d("RankingLatestSearchFragment", "返回資料 Event：" + keyword)
                        categoryId = ""
//                        sub_categoryId = MMKV.mmkvWithID("http").getInt("sub_product_category_id",0)
                        val url = ApiConstants.API_HOST+"/product/"+mode +"/product_analytics_pages_keyword/"
                        getSearchProductOverAll(url,userId,categoryId,sub_categoryId.toString(),max_seq.toString(),keyword!!)
                    }
                }

            })
    }

    private fun getSearchProductOverAll(url: String,user_id:String,category_id:String,sub_category_id:String,max_seq:String,keyword:String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ProductSearchBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("RankingAllSearch", "返回資料 resStr：" + resStr)
                    Log.d("RankingAllSearch", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonObject: JSONObject = json.getJSONObject("data")

                        val jsonArray:JSONArray = jsonObject.getJSONArray("productsList")

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val productSearchBean: ProductSearchBean =
                                Gson().fromJson(jsonObject.toString(), ProductSearchBean::class.java)
                            list.add(productSearchBean)
                        }

                    }

                    if(list.size > 0){
                        if(userId.isEmpty()){
                            userId = list[0].user_id
                        }

                        requireActivity().runOnUiThread {
                            adapter.setData(list)
                            initRecyclerView()
                            progressBar.isVisible = false
                        }
                    }else{
                        requireActivity().runOnUiThread {
                            adapter.clear()
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
        web.Do_GetSearchProduct(url,user_id,category_id,sub_category_id,max_seq,keyword)
    }

    private fun getSearchProductOverAllMore(url: String,user_id:String,category_id:String,sub_category_id:String,max_seq:String,keyword:String) {
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ProductSearchBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)

                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonArray: JSONArray = json.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val productSearchBean: ProductSearchBean =
                                Gson().fromJson(jsonObject.toString(), ProductSearchBean::class.java)
                            list.add(productSearchBean)
                        }
                        refreshLayout.finishLoadMore()
                    }

                    if(list.size > 0){

                        requireActivity().runOnUiThread {
                            adapter.add(list)

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
        web.Do_GetSearchProduct(url,user_id,category_id,sub_category_id,max_seq,keyword)
    }

}