package com.HKSHOPU.hk.ui.main.homepage.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import okhttp3.Response
import org.jetbrains.anko.find
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class RankingAllFragment : Fragment() {

    companion object {
        fun newInstance(): RankingAllFragment {
            val args = Bundle()
            val fragment = RankingAllFragment()
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var allProduct :RecyclerView
    lateinit var progressBar: ProgressBar
    var defaultLocale = Locale.getDefault()
    var currency: Currency = Currency.getInstance(defaultLocale)
    private val adapter = ProductShopPreviewAdapter(currency)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ranking_all, container, false)

        allProduct = v.find<RecyclerView>(R.id.recyclerview_rankall)
        progressBar = v.find<ProgressBar>(R.id.progressBar_product_all)
        progressBar.isVisible = true
        val activity: ShopPreviewActivity? = activity as ShopPreviewActivity?
        val shopId: Int? = activity!!.getShopId()
        val userId: String? = activity!!.getUserId()
        Log.d("RankingAllFragment", "返回資料 shopId：" + shopId)
        val url = ApiConstants.API_HOST+"/product/"+shopId+"/"+"overall"+"/shop_product_analytics/"

        getProductOverAll(url,userId!!)
        initView()

        return v
    }

    private fun initView(){

    }


    private fun initRecyclerView(){


        val layoutManager = GridLayoutManager(requireActivity(),2)
        allProduct.layoutManager = layoutManager

        allProduct.adapter = adapter
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
                    Log.d("RankingAllFragment", "返回資料 resStr：" + resStr)
                    Log.d("RankingAllFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("已取得商品清單!")) {

                        val jsonArray: JSONArray = json.getJSONArray("data")
                        Log.d("RankingAllFragment", "返回資料 jsonArray：" + jsonArray.toString())

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val productShopPreviewBean: ProductShopPreviewBean =
                                Gson().fromJson(jsonObject.toString(), ProductShopPreviewBean::class.java)
                            list.add(productShopPreviewBean)
                        }

                    }

                    Log.d("RankingAllFragment", "返回資料 list：" + list.toString())

                    if(list.size > 0){
                        adapter.setData(list)
                        activity!!.runOnUiThread {
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
        web.Do_GetShopProduct(url,userId)
    }

}