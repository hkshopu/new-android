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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.HKSHOPU.hk.R

import com.HKSHOPU.hk.data.bean.ProductShopPreviewBean
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.homepage.adapter.ProductShopPreviewAdapter
import com.HKSHOPU.hk.ui.main.store.activity.ShopPreviewActivity
import com.HKSHOPU.hk.utils.rxjava.RxBus
import okhttp3.Response
import org.jetbrains.anko.find
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class RankingTopSaleFragment : Fragment() {

    companion object {
        fun newInstance(): RankingTopSaleFragment {
            val args = Bundle()
            val fragment = RankingTopSaleFragment()
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var topProduct :RecyclerView
    lateinit var progressBar: ProgressBar
    var defaultLocale = Locale.getDefault()
    var currency: Currency = Currency.getInstance(defaultLocale)
    private val adapter = ProductShopPreviewAdapter(currency)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ranking_hotsales, container, false)
        progressBar = v.find<ProgressBar>(R.id.progressBar_product_top)
        progressBar.isVisible = true
        val activity: ShopPreviewActivity? = activity as ShopPreviewActivity?
        val shopId: String? = activity!!.getShopId()
        val userId: String? = activity!!.getUserId()
        var url = ApiConstants.API_HOST+"/product/"+shopId+"/"+"top_sale"+"/shop_product_analytics/"
        topProduct = v.find<RecyclerView>(R.id.recyclerview_top)
        getProductOverAll(url,userId!!)

        initView()
        initEvent()

        return v
    }

    private fun initView(){

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

    private fun getProductOverAll(url: String,userId:String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ProductShopPreviewBean>()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("RankingTopFragment", "返回資料 resStr：" + resStr)
                    Log.d("RankingTopFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("已取得商品清單!")) {

                        val jsonArray: JSONArray = json.getJSONArray("data")
                        Log.d("RankingTopFragment", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val productShopPreviewBean: ProductShopPreviewBean =
                                Gson().fromJson(jsonObject.toString(), ProductShopPreviewBean::class.java)
                            list.add(productShopPreviewBean)
                        }

                    }

                    Log.d("RankingTopFragment", "返回資料 list：" + list.toString())

                    if(list.size > 0){
                        adapter.setData(list)
                        activity!!.runOnUiThread {
                            initRecyclerView()
                            progressBar.isVisible = false
                        }
                    }else{
                        activity!!.runOnUiThread {
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
        web.Do_GetShopProduct(url,userId)
    }


}