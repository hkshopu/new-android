package com.HKSHOPU.hk.ui.main.homepage.fragment

import android.annotation.SuppressLint
import android.content.Intent
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

import com.HKSHOPU.hk.data.bean.ShopRecommendBean
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.homepage.activity.StoreRecommendActivity
import com.HKSHOPU.hk.ui.main.homepage.adapter.StoreRecommendAdapter
import com.HKSHOPU.hk.ui.main.store.activity.ShopPreviewActivity
import com.HKSHOPU.hk.utils.rxjava.RxBus
import com.HKSHOPU.hk.widget.view.KeyboardUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import okhttp3.Response
import org.jetbrains.anko.find
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class StoreRankingLatestFragment : Fragment() {

    companion object {
        fun newInstance(): StoreRankingLatestFragment {
            val args = Bundle()
            val fragment = StoreRankingLatestFragment()
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var latestStore :RecyclerView
    lateinit var progressBar: ProgressBar
    private val adapter = StoreRecommendAdapter()
    var userId = ""
    var max_seq = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ranking_latest_store, container, false)
        refreshLayout = v.find<SmartRefreshLayout>(R.id.refreshLayout)
        val activity: StoreRecommendActivity? = activity as StoreRecommendActivity?
        userId = activity!!.getUserId().toString()
        val mode = "new"
        var url = ApiConstants.API_HOST+"shop/get_shop_analytics_in_pages/"
        latestStore = v.find<RecyclerView>(R.id.recyclerview_latest_store)
        progressBar = v.find<ProgressBar>(R.id.progressBar_latest_store)
        progressBar.isVisible = true
        getStoreOverAll(url,userId!!,mode,max_seq)

        initView()
        initEvent()
        initRefresh()
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
            val activity: StoreRecommendActivity? = activity as StoreRecommendActivity?
            val userId: String? = activity!!.getUserId()
            val mode = "new"
            var url = ApiConstants.API_HOST+"shop/get_shop_analytics_in_pages/"
            max_seq ++
            getStoreOverAllMore(url,userId!!,mode,max_seq)
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

        val layoutManager = LinearLayoutManager(requireActivity())
        latestStore.layoutManager = layoutManager

        latestStore.adapter = adapter
        adapter.itemClick = {
            val bundle = Bundle()
            bundle.putString("shopId",it)
            bundle.putString("userId",userId)
            val intent = Intent(requireActivity(), ShopPreviewActivity::class.java)
            intent.putExtra("bundle",bundle)
            requireActivity().startActivity(intent)
        }

    }

    private fun getStoreOverAll(url: String,userId:String,mode:String,max_seq:Int) {
        Log.d("StoreRankingLatest", "資料 url：" + url)
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ShopRecommendBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("StoreRankingLatest", "返回資料 resStr：" + resStr)
                    Log.d("StoreRankingLatest", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonArray: JSONArray = json.getJSONArray("data")
                        Log.d("StoreRankingLatest", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val shopRecommendBean: ShopRecommendBean =
                                Gson().fromJson(jsonObject.toString(), ShopRecommendBean::class.java)
                            list.add(shopRecommendBean)
                        }
                    }


                    if(list.size > 0){

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
        web.Do_GetShopRecommend(url,userId,mode,max_seq)
    }

    private fun getStoreOverAllMore(url: String,userId:String,mode:String,max_seq:Int) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ShopRecommendBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("StoreRankingAll", "返回資料 resStr：" + resStr)
                    Log.d("StoreRankingAll", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonArray: JSONArray = json.getJSONArray("data")
                        Log.d("StoreRankingAll", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val shopRecommendBean: ShopRecommendBean =
                                Gson().fromJson(jsonObject.toString(), ShopRecommendBean::class.java)
                            list.add(shopRecommendBean)
                        }
                        refreshLayout.finishLoadMore()
                    }

                    if(list.size > 0){

                        activity!!.runOnUiThread {
                            adapter.add(list)
//                            initRecyclerView()

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
        web.Do_GetShopRecommend(url,userId,mode,max_seq)
    }

}