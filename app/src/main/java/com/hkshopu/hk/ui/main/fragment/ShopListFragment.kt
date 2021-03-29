package com.hkshopu.hk.ui.main.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.gson.Gson
import com.hkshopu.hk.R
import com.hkshopu.hk.application.App
import com.hkshopu.hk.component.EventAddShopSuccess
import com.hkshopu.hk.component.EventGetShopListSuccess
import com.hkshopu.hk.data.bean.ShopCategoryBean
import com.hkshopu.hk.databinding.FragmentShoplistBinding
import com.hkshopu.hk.net.Web
import com.hkshopu.hk.net.WebListener
import com.hkshopu.hk.ui.main.activity.AddShopActivity
import com.hkshopu.hk.ui.main.adapter.ShopInfoAdapter
import com.hkshopu.hk.utils.rxjava.RxBus
import com.tencent.mmkv.MMKV
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ShopListFragment : Fragment(R.layout.fragment_shoplist){

    companion object {
        fun newInstance(): ShopListFragment {
            val args = Bundle()
            val fragment = ShopListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var binding: FragmentShoplistBinding? = null
    private var fragmentShopListBinding: FragmentShoplistBinding? = null
    private val adapter = ShopInfoAdapter()
    val userId = MMKV.mmkvWithID("http").getInt("UserId", 0);
    private var url = "https://hkshopu-20700.df.r.appspot.com/user/"+userId+"/shop/"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShoplistBinding.bind(view)
        fragmentShopListBinding = binding
        initView()
        initVM()
        initEvent()
        initFragment()
        initClick()

    }

    fun initView() {
        binding!!.container2.visibility = View.VISIBLE
    }
    private fun initFragment() {

    }


    fun initVM() {

    }

    @SuppressLint("CheckResult")
    fun initEvent() {
        RxBus.getInstance().toMainThreadObservable(activity!!, Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventAddShopSuccess -> {
                        getShopInfo(url)
                    }
                    is EventGetShopListSuccess -> {
                        Log.d("ShopInfoFragment", "RxBus List Size：" + it.shopNums)
                        if (it.shopNums > 0) {

                            binding!!.container1.visibility = View.VISIBLE


                        } else {

                            binding!!.container2.visibility = View.VISIBLE


                        }
                    }

                }
            }, {
                it.printStackTrace()
            })
    }

    fun initClick() {

            binding!!.tvAddonlineshop.setOnClickListener {
                val intent = Intent(activity, AddShopActivity::class.java)
                activity!!.startActivity(intent)

            }

    }
    private fun getShopInfo(url: String) {
        Log.d("ShopInfoFragment", "ShopInfo Url：" + url)
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("ShopInfoFragment", "返回資料 resStr：" + resStr)
                    Log.d("ShopInfoFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("已取得您的商店清單!")) {

                        val translations: JSONArray = json.getJSONArray("shop_list")
                        Log.d("ShopInfoFragment", "返回資料 List：" + translations.toString())
                        val list = ArrayList<ShopCategoryBean>()
                        for (i in 0 until translations.length()) {
                            val jsonObject: JSONObject = translations.getJSONObject(i)
                            val shopCategoryBean: ShopCategoryBean =
                                Gson().fromJson(jsonObject.toString(), ShopCategoryBean::class.java)
                            list.add(shopCategoryBean)
                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                            //處理少量資訊或UI
                            binding!!.container1.visibility = View.VISIBLE

                        }, 200)
//                        adapter.setData(list)
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            //處理少量資訊或UI
                            binding!!.container2.visibility = View.VISIBLE

                        }, 200)


                    }
//                        initRecyclerView()


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


    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentShopListBinding = null
        super.onDestroyView()
    }


}