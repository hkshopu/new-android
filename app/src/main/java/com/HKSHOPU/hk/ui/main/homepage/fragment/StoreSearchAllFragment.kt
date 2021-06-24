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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.HKSHOPU.hk.R
import com.HKSHOPU.hk.component.EventToShopSearch

import com.HKSHOPU.hk.data.bean.ShopRecommendBean
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.homepage.activity.SearchActivity
import com.HKSHOPU.hk.ui.main.homepage.adapter.StoreRecommendAdapter
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

class StoreSearchAllFragment : Fragment() {

    companion object {
        fun newInstance(): StoreSearchAllFragment {
            val args = Bundle()
            val fragment = StoreSearchAllFragment()
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var allStore :RecyclerView
    lateinit var progressBar:ProgressBar
    private val adapter = StoreRecommendAdapter()
    var keyword = ""
    var categoryId = 0
    var userId = ""
    var max_seq = 0
    var mode = "overall"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ranking_all_store, container, false)
        refreshLayout = v.find<SmartRefreshLayout>(R.id.refreshLayout)
        progressBar = v.find<ProgressBar>(R.id.progressBar_all_store)

        allStore = v.find<RecyclerView>(R.id.recyclerview_rankall_store)

        initView()
        initEvent()
        initRefresh()
        return v
    }

    private fun initView(){
        progressBar.isVisible = true
        val activity: SearchActivity? = activity as SearchActivity?
        userId = activity!!.getUserId()!!
        categoryId = MMKV.mmkvWithID("http")!!.getInt("product_category_id",0)
        val url = ApiConstants.API_HOST+"/shop/get_shop_analytics_with_keyword_in_pages/"
        getSearchStoreOverAll(url,userId!!,mode,max_seq.toString(),categoryId,keyword!!)
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

            val url = ApiConstants.API_HOST+"/shop/get_shop_analytics_with_keyword_in_pages/"
            max_seq++
            if(keyword.isNotEmpty()){
                categoryId = 0
            }else{
                keyword =""
            }
            getSearchStoreOverAllMore(
                url,
                userId,
                mode,
                max_seq.toString(),
                categoryId,
                keyword
            )
//            VM.loadMore(this)
        }
    }

    @SuppressLint("CheckResult")
    fun initEvent() {
        RxBus.getInstance().toMainThreadObservable(requireActivity(), Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventToShopSearch -> {
                        progressBar.isVisible = true
                        val keyword = MMKV.mmkvWithID("http")!!.getString("keyword","").toString()
                        val url = ApiConstants.API_HOST+"/shop/get_shop_analytics_with_keyword_in_pages/"

                        getSearchStoreOverAll(url,userId!!,mode,max_seq.toString(),categoryId,keyword!!)
                    }
                }

            })
    }

    private fun initRecyclerView(){


        val layoutManager = LinearLayoutManager(requireActivity())
        allStore.layoutManager = layoutManager

        allStore.adapter = adapter
        adapter.itemClick = {

        }

    }

    private fun getSearchStoreOverAll(url: String,userId:String,mode:String,max_seq:String,product_category_id:Int,keyword:String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ShopRecommendBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("StoreSearchAllFragment", "返回資料 resStr：" + resStr)
                    Log.d("StoreSearchAllFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val jsonArray: JSONArray = jsonObject.getJSONArray("shops")
                        Log.d("StoreSearchAllFragment", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val shopRecommendBean: ShopRecommendBean =
                                Gson().fromJson(jsonObject.toString(), ShopRecommendBean::class.java)
                            list.add(shopRecommendBean)
                        }

                    }

                    Log.d("RankingAllFragment", "返回資料 list：" + list.toString())

                    if(list.size > 0){

                        activity!!.runOnUiThread {
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
        web.Do_GetSearchStore(url,userId,mode,max_seq,product_category_id,keyword)
    }

    private fun getSearchStoreOverAllMore(url: String,userId:String,mode:String,max_seq:String,product_category_id:Int,keyword:String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ShopRecommendBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("StoreSearchAllFragment", "返回資料 resStr：" + resStr)
                    Log.d("StoreSearchAllFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonObject: JSONObject = json.getJSONObject("data")
                        val jsonArray: JSONArray = jsonObject.getJSONArray("shops")
                        Log.d("StoreSearchAllFragment", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val shopRecommendBean: ShopRecommendBean =
                                Gson().fromJson(jsonObject.toString(), ShopRecommendBean::class.java)
                            list.add(shopRecommendBean)
                        }

                    }

                    if(list.size > 0){

                        activity!!.runOnUiThread {
                            adapter.add(list)
                            refreshLayout.finishLoadMore()
                        }
                    }else{
                        activity!!.runOnUiThread {

                            refreshLayout.finishLoadMore()
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
        web.Do_GetSearchStore(url,userId,mode,max_seq,product_category_id,keyword)
    }

}