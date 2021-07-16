package com.HKSHOPU.hk.ui.main.buyer.activity

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.R
import com.HKSHOPU.hk.data.bean.*

import com.HKSHOPU.hk.databinding.*
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.buyer.adapter.BuyerPendingDeliver_OrderDatailAdapter
import com.HKSHOPU.hk.ui.main.buyer.adapter.ProductLikedAdapter
import com.HKSHOPU.hk.ui.main.buyer.adapter.StoreFollowAdapter
import com.HKSHOPU.hk.ui.main.buyer.fragment.LoginFirstDialogFragment
import com.HKSHOPU.hk.ui.main.buyer.fragment.ProductConfirmDialogFragment
import com.HKSHOPU.hk.ui.main.buyer.fragment.PurchaseListFragment
import com.HKSHOPU.hk.ui.main.homepage.adapter.ProductShopPreviewAdapter
import com.HKSHOPU.hk.ui.main.homepage.adapter.StoreRecommendAdapter
import com.HKSHOPU.hk.utils.extension.load
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


class BuyerPurchaseList_recieveActivity : BaseActivity() {
    private lateinit var binding: ActivityBuyerorderdetailReceiveBinding


    private val adapter = BuyerPendingDeliver_OrderDatailAdapter()
    var orderNumber =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerorderdetailReceiveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.isVisible = true
        initView()
        initClick()
        val orderId = intent.getBundleExtra("bundle")!!.getString("order_id")
        doGetData(orderId!!)
    }

    private fun initView() {

    }


    private fun initClick() {
        binding.ivBack.setOnClickListener {

            finish()
        }

        binding.ivOrderdone.setOnClickListener {
            runOnUiThread {

                ProductConfirmDialogFragment(orderNumber).show(
                    getSupportFragmentManager(),
                    "MyCustomFragment"
                )
            }
        }

    }
    private fun initRecyclerView(){


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = layoutManager

        binding.recyclerview.adapter = adapter


    }

    private fun doGetData(order_id: String) {

        var url = ApiConstants.API_HOST + "user_detail/"+order_id +"/order_detail/"

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<MyOrderBean>()
                list.clear()
                val list_product = ArrayList<OrderProductBean>()
                list_product.clear()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("BuyerPurchaseList_recieve", "返回資料 resStr：" + resStr)
                    Log.d("BuyerPurchaseList_recieve", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val jsonObject = json.getJSONObject("data")
                        val jsonArray_product: JSONArray = jsonObject.getJSONArray("productList")
                        for (i in 0 until jsonArray_product.length()) {
                            val state = jsonObject.getString("status")
                            if(state.equals("Pending Good Receive ")) {
                                val myOrderBean: MyOrderBean =
                                    Gson().fromJson(jsonObject.toString(), MyOrderBean::class.java)
                                list.add(myOrderBean)
                                val jsonObject_product: JSONObject = jsonArray_product.getJSONObject(i)
                                for (i in 0 until jsonArray_product.length()) {
                                    val orderProductBean: OrderProductBean =
                                        Gson().fromJson(jsonObject_product.toString(), OrderProductBean::class.java)
                                    list_product.add(orderProductBean)
                                }
                            }
                        }


                    }

                    if(list.size > 0){
                        adapter.setData(list_product)
                        runOnUiThread {
                            binding.tvLogistic.text = list[0].shipment_info
                            binding.tvBuyername.text = list[0].name_in_address
                            binding.tvBuyerphone.text = list[0].phone
                            binding.tvBuyeraddress.text = list[0].full_address
                            binding.ivStore.load(list[0].shop_icon)
                            binding.tvStoreName.text = list[0].shop_title
                            val total_amount= list[0].subtotal + list[0].shipment_price
                            binding.tvTotal.text = "HKD$"+total_amount.toString()
                            binding.tvShippingFee.text = "HKD$"+list[0].shipment_price
                            binding.tvPayment.text = list[0].payment_desc
                            binding.tvOrdernumber.text = list[0].order_number
                            orderNumber = list[0].order_number
                            binding.tvPaytime.text = list[0].pay_time
                            initRecyclerView()
                            binding.progressBar.isVisible = false
                        }
                    }else{
                        runOnUiThread {
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
        web.Get_Data(url)
    }

}