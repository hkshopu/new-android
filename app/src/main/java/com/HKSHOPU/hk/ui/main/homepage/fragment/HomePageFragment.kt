package com.HKSHOPU.hk.ui.main.homepage.fragment


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.HKSHOPU.hk.R
import com.HKSHOPU.hk.component.CommonVariable
import com.HKSHOPU.hk.component.EventToUserProfile
import com.HKSHOPU.hk.data.bean.HomeAdBean
import com.HKSHOPU.hk.data.bean.ProductShopPreviewBean
import com.HKSHOPU.hk.data.bean.ShopCategoryBean
import com.HKSHOPU.hk.data.bean.ShopRecommendHomeBean
import com.HKSHOPU.hk.databinding.FragmentHomepageBinding
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.homepage.activity.*
import com.HKSHOPU.hk.ui.main.homepage.adapter.ProductShopPreviewAdapter
import com.HKSHOPU.hk.ui.main.homepage.adapter.StoreRecommendHomeAdapter
import com.HKSHOPU.hk.ui.main.store.activity.OnBoardActivity
import com.HKSHOPU.hk.ui.main.store.activity.ShopNotifyActivity
import com.HKSHOPU.hk.ui.main.store.activity.ShopPreviewActivity
import com.HKSHOPU.hk.ui.main.store.adapter.CategorySingleAdapter
import com.HKSHOPU.hk.ui.main.store.adapter.HomeAdAdapter
import com.HKSHOPU.hk.ui.user.vm.ShopVModel
import com.HKSHOPU.hk.utils.rxjava.RxBus
import com.HKSHOPU.hk.widget.view.KeyboardUtil
import com.tencent.mmkv.MMKV
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import java.util.Locale


class HomePageFragment : Fragment((R.layout.fragment_homepage)) {

    companion object {
        fun newInstance(): HomePageFragment {
            val args = Bundle()
            val fragment = HomePageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val VM = ShopVModel()
    var userId = MMKV.mmkvWithID("http").getString("UserId", "");
    private val adapter_HomeAd = HomeAdAdapter()
    private val adapter_ProductCategory = CategorySingleAdapter()
    private val adapter_ShopRecommend = StoreRecommendHomeAdapter()
    val REQUEST_CODE_SPEECH_INPUT = 1000
    var defaultLocale = Locale.getDefault()
    var currency: Currency = Currency.getInstance(defaultLocale)
    private val adapter_TopProduct = ProductShopPreviewAdapter(currency)
    private var binding: FragmentHomepageBinding? = null
    private var fragmentHomepageBinding: FragmentHomepageBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomepageBinding.bind(view)
        fragmentHomepageBinding = binding

        binding!!.progressBarHomepage.isVisible = true

        var url_homeAd = ApiConstants.API_HOST + "shop/advertisement/"
        getHomeAd(url_homeAd)
        var url = ApiConstants.API_HOST + "shop_category/index/"
        getShopCategory(url)

        getRecommendedStores(userId.toString())

        if (userId!!.isEmpty()) {
            var url_topproduct = ApiConstants.API_HOST + "product/" + "null" + "/product_analytics/"
            getTopProduct(url_topproduct)
        } else {
            var url_topproduct = ApiConstants.API_HOST + "product/" + userId + "/product_analytics/"
            getTopProduct(url_topproduct)
        }

        initRefresh()
//        initVM()
        initView()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //go to previous fragemnt
                    //perform your fragment transaction here
                    //pass data as arguments
                    AlertDialog.Builder(requireActivity())
                        .setTitle("")
                        .setMessage("您確定要離開 ？")
                        .setNegativeButton("確定") {
                            // 此為 Lambda 寫法
                                dialog, which ->
                            requireActivity().finishAffinity()
                        }
                        .setPositiveButton("取消") { dialog, which ->
                            dialog.cancel()

                        }
                        .show()
                    return@OnKeyListener true
                }
            }
            false
        })

//        checkPermission()
//        VM.getTopProduct(requireActivity(),userId)
    }

    private fun initRefresh() {
        binding!!.refreshLayout.setOnClickListener {
            KeyboardUtil.hideKeyboard(it)
        }
        binding!!.refreshLayout.setOnRefreshListener {
//            VM.loadShop(this)
            binding!!.refreshLayout.finishRefresh()
        }
        binding!!.refreshLayout.setOnLoadMoreListener {
            binding!!.refreshLayout.finishLoadMore()
            binding!!.refreshLayout.finishRefresh()
//            VM.loadMore(this)
        }
    }

    private fun initVM() {


    }

    private fun initView() {
        binding!!.ivUserPicClick.setOnClickListener {
            RxBus.getInstance().post(EventToUserProfile())
        }
        binding!!.tvUsername.setOnClickListener {
            RxBus.getInstance().post(EventToUserProfile())
        }

        binding!!.ivShopcarClick.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("toShopFunction", false)
            val intent = Intent(requireActivity(), GoShopActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding!!.ivNotifyClick.setOnClickListener {

            val intent = Intent(requireActivity(), ShopNotifyActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding!!.tvMoreProductcategory.setOnClickListener {

            val intent = Intent(requireActivity(), MerchanCategorySearchActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding!!.tvMoreStorerecommend.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userId", userId)
            val intent = Intent(requireActivity(), StoreRecommendActivity::class.java)
            intent.putExtra("bundle", bundle)
            requireActivity().startActivity(intent)
        }

        binding!!.tvMoreHotsale.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userId", userId)
            val intent = Intent(requireActivity(), TopProductsActivity::class.java)
            intent.putExtra("bundle", bundle)
            requireActivity().startActivity(intent)
        }

        binding!!.etSearchKeyword.doAfterTextChanged {

        }

        binding!!.etSearchKeyword.setOnEditorActionListener() { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val keyWord = binding!!.etSearchKeyword.text.toString()
                    MMKV.mmkvWithID("http")
                        .putString("c_product_sub_category_selected",keyWord)
                    val intent = Intent(requireActivity(), SearchActivity::class.java)
                    requireActivity().startActivity(intent)

                    true
                }

                else -> false
            }
        }

        binding!!.ivMic.setOnClickListener {
//            speak()
            KeyboardUtil.showKeyboard(it)
        }

    }

    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(requireActivity())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding!!.recyclerviewAd.layoutManager = layoutManager
        binding!!.recyclerviewAd.adapter = adapter_HomeAd

        val layoutManager1 = LinearLayoutManager(requireActivity())
        layoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        binding!!.recyclerviewProductcategory.layoutManager = layoutManager1
        binding!!.recyclerviewProductcategory.adapter = adapter_ProductCategory
        adapter_ProductCategory.intentClick = {
            val intent = Intent(requireActivity(), MerchanCategorySearchActivity::class.java)
            requireActivity().startActivity(intent)
        }

        val layoutManager2 = LinearLayoutManager(requireActivity())
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        binding!!.recyclerviewStorerecommend.layoutManager = layoutManager2
        binding!!.recyclerviewStorerecommend.adapter = adapter_ShopRecommend
        adapter_ShopRecommend.itemClick = {
            val bundle = Bundle()
            bundle.putString("shopId", it)
            bundle.putString("userId", userId)
            val intent = Intent(requireActivity(), ShopPreviewActivity::class.java)
            intent.putExtra("bundle", bundle)
            requireActivity().startActivity(intent)
        }

        adapter_ShopRecommend.followClick = { id, follow ->
            if (userId!!.isEmpty()) {
                val intent = Intent(requireActivity(), OnBoardActivity::class.java)
                requireActivity().startActivity(intent)
            } else {
                val url_follow =
                    ApiConstants.API_HOST + "user/" + userId + "/followShop/" + id + "/"
                doStoreFollow(url_follow, follow)

            }

        }

        val layoutManager3 = GridLayoutManager(requireActivity(), 2)
        binding!!.recyclerviewHotsale.layoutManager = layoutManager3
        binding!!.recyclerviewHotsale.adapter = adapter_TopProduct
        adapter_TopProduct.likeClick = { id, like ->
            if (userId!!.isEmpty()) {
                val intent = Intent(requireActivity(), OnBoardActivity::class.java)
                requireActivity().startActivity(intent)
            } else {
                val url_productLike = ApiConstants.API_HOST + "product/like_product/"
                doProductLike(url_productLike, id.toString(), userId.toString(), like)
            }

        }
    }

    private fun speak() {
        // Intent to show speech to text dialogs
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...")

        // Start Intent
        try {
            // If there was no error
            // showing dialogs
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            // If there was some error

            // get Message of error and show
            Toast.makeText(requireActivity(), "" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + requireContext().packageName)
                )
                startActivity(intent)

                Toast.makeText(
                    requireActivity(),
                    "Enable Microphone Permission..!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getHomeAd(url: String) {
        val list = ArrayList<HomeAdBean>()
        list.clear()
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("HomePageFragment", "返回資料 resStr：" + resStr)
                    Log.d("HomePageFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val translations: JSONArray = json.getJSONArray("data")
                        Log.d("HomePageFragment", "返回資料 List：" + translations.toString())
                        for (i in 0 until translations.length()) {
                            val jsonObject: JSONObject = translations.getJSONObject(i)
                            val homeAdBean: HomeAdBean =
                                Gson().fromJson(jsonObject.toString(), HomeAdBean::class.java)

                            list.add(homeAdBean)
                        }

                        requireActivity().runOnUiThread {
                            adapter_HomeAd.setData(list)
                            initRecyclerView()

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

    private fun getShopCategory(url: String) {

        CommonVariable.list.clear()
        CommonVariable.ShopCategory.clear()
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("HomePageFragment", "返回資料 resStr：" + resStr)
                    Log.d("HomePageFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("已取得商店清單!")) {

                        val translations: JSONArray = json.getJSONArray("shop_category_list")
                        Log.d("HomePageFragment", "返回資料 List：" + translations.toString())
                        for (i in 0 until translations.length()) {
                            val jsonObject: JSONObject = translations.getJSONObject(i)
                            val shopCategoryBean: ShopCategoryBean =
                                Gson().fromJson(jsonObject.toString(), ShopCategoryBean::class.java)

                            CommonVariable.list.add(shopCategoryBean)
                            CommonVariable.ShopCategory.put(
                                shopCategoryBean.id.toString(),
                                shopCategoryBean
                            )

                        }

                        requireActivity().runOnUiThread {
                            adapter_ProductCategory.setData(CommonVariable.list)
                            initRecyclerView()

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

    private fun getRecommendedStores(userId: String) {
        val url = ApiConstants.API_HOST + "shop/get_recommended_shops/"
        val list = ArrayList<ShopRecommendHomeBean>()
        list.clear()
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("HomePageFragment", "返回資料 resStr：" + resStr)
                    Log.d("HomePageFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val translations: JSONArray = json.getJSONArray("data")
                        Log.d("HomePageFragment", "返回資料 List：" + resStr)
                        for (i in 0 until translations.length()) {
                            val jsonObject: JSONObject = translations.getJSONObject(i)
                            val shopRecommendHomeBean: ShopRecommendHomeBean =
                                Gson().fromJson(
                                    jsonObject.toString(),
                                    ShopRecommendHomeBean::class.java
                                )
                            list.add(shopRecommendHomeBean)
                        }

                        requireActivity().runOnUiThread {
                            adapter_ShopRecommend.setData(list)
                            initRecyclerView()

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
        web.Do_GetRecommendedShops(url, userId)
    }

    private fun getTopProduct(url: String) {
        val list = ArrayList<ProductShopPreviewBean>()
        list.clear()
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("HomePageFragment", "返回資料 resStr：" + resStr)
                    Log.d("HomePageFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        val translations: JSONArray = json.getJSONArray("data")
                        Log.d("HomePageFragment", "返回資料 List：" + resStr)
                        for (i in 0 until translations.length()) {
                            val jsonObject: JSONObject = translations.getJSONObject(i)
                            val productShopPreviewBean: ProductShopPreviewBean =
                                Gson().fromJson(
                                    jsonObject.toString(),
                                    ProductShopPreviewBean::class.java
                                )
                            list.add(productShopPreviewBean)
                        }

                        requireActivity().runOnUiThread {
                            adapter_TopProduct.setData(list)
                            initRecyclerView()
                            binding!!.progressBarHomepage.isVisible = false
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

    private fun doStoreFollow(url: String, follow: String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("HomePageFragment", "返回資料 resStr：" + resStr)
                    Log.d("HomePageFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {

                        requireActivity().runOnUiThread {
                            if (follow.equals("Y")) {
                                adapter_ShopRecommend.updateData("Y")
                            } else {
                                adapter_ShopRecommend.updateData("N")
                            }
                        }

                    } else {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireActivity(), ret_val.toString(), Toast.LENGTH_SHORT
                            ).show()
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
        web.Store_Follow(url, follow)
    }

    private fun doProductLike(url: String, productId: String, userId: String, like: String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("HomePageFragment", "返回資料 resStr：" + resStr)
                    Log.d("HomePageFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (ret_val.equals("商品收藏成功!")) {

                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireActivity(), ret_val.toString(), Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireActivity(), ret_val.toString(), Toast.LENGTH_SHORT
                            ).show()
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
        web.Product_Like(url, productId, userId, like)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (requestCode != RESULT_OK && null != data) {
                    // get the text array from voice intent
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    // set the voice view
                    binding!!.etSearchKeyword.setText(result!![0])
                }
            }
        }
    }

}