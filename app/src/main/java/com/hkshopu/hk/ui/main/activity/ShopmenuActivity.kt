package com.hkshopu.hk.ui.main.activity


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.Fragment

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson


import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.R
import com.hkshopu.hk.component.EventAddShopSuccess
import com.hkshopu.hk.component.EventGetShopListSuccess
import com.hkshopu.hk.data.bean.ShopCategoryBean
import com.hkshopu.hk.databinding.ActivityMainBinding
import com.hkshopu.hk.net.Web
import com.hkshopu.hk.net.WebListener
import com.hkshopu.hk.ui.main.fragment.FirstFragment
import com.hkshopu.hk.ui.main.fragment.SecondFragment
import com.hkshopu.hk.ui.main.fragment.ShopInfoFragment
import com.hkshopu.hk.ui.main.fragment.ShopListFragment
import com.hkshopu.hk.ui.user.activity.LoginActivity
import com.hkshopu.hk.utils.rxjava.RxBus
import com.tencent.mmkv.MMKV
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ShopmenuActivity: BaseActivity(), ViewPager.OnPageChangeListener {
    private lateinit var binding: ActivityMainBinding

    lateinit var manager: FragmentManager
    val userId = MMKV.mmkvWithID("http").getInt("UserId", 0);
    private var url = "https://hkshopu-20700.df.r.appspot.com/user/"+userId+"/shop/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragment()
        initView()
        initClick()

    }
    private val fragments = mutableListOf<Fragment>()
    private fun initFragment() {
        manager = supportFragmentManager
        if (fragments.isNotEmpty())return
        val FirstFragment = FirstFragment.newInstance()
        val SecondFragment = SecondFragment.newInstance()
        val ShopListFragment = ShopListFragment.newInstance()
        fragments.add(FirstFragment)
        fragments.add(SecondFragment)
        fragments.add(ShopListFragment)
        binding.viewPager.adapter = object : FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
            override fun getItem(position: Int) =  fragments[position]
            override fun getCount() = fragments.size
        }
        binding.viewPager.setPagingEnabled(false)
        binding.viewPager.addOnPageChangeListener(this)
//        binding.setViewPager(binding.mviewPager, arrayOf(getString(R.string.product),getString(R.string.info)))
    }

    fun initView() {
        binding.bottomNavigationViewLinear.setNavigationChangeListener { view, position ->
//            Log.d("ShopMenuActivity", "BottomView position：" + position)
            if(position == 2){
                if(userId == 0){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("提醒")
                    builder.setMessage("請先登入")
                    builder.setPositiveButton("OK"){dialog, which ->
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    builder.setNegativeButton("Cancel"){dialog, which ->
                        binding.bottomNavigationViewLinear.setCurrentActiveItem(0)
                    }
                    val dialog: AlertDialog = builder.create()

                    // Display the alert dialog on app interface
                    dialog.show()
                }else{

                    getShopInfo(url)
                    binding.viewPager.setCurrentItem(position, true);
                }
            }else {
                binding.viewPager.setCurrentItem(position, true);
            }
        }

    }

    fun initClick(){


    }

    private fun getShopInfo(url: String) {
        Log.d("ShopmenuActivity", "ShopInfo Url：" + url)
        val list = ArrayList<ShopCategoryBean>()
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("ShopmenuActivity", "返回資料 resStr：" + resStr)
                    Log.d("ShopmenuActivity", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("已取得您的商店清單!")) {

                        val translations: JSONArray = json.getJSONArray("shop_list")
                        Log.d("ShopmenuActivity", "返回資料 List：" + translations.toString())

                        for (i in 0 until translations.length()) {
                            val jsonObject: JSONObject = translations.getJSONObject(i)
                            val shopCategoryBean: ShopCategoryBean =
                                Gson().fromJson(jsonObject.toString(), ShopCategoryBean::class.java)
                            list.add(shopCategoryBean)
                        }

                    }else{

                    }
                    RxBus.getInstance().post(EventGetShopListSuccess(list.size))
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

    override fun onBackPressed() {
            super.onBackPressed()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }


}
