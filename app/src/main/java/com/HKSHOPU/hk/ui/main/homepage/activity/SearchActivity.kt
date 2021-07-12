package com.HKSHOPU.hk.ui.main.homepage.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.component.EventToProductSearch
import com.HKSHOPU.hk.component.EventToShopSearch
import com.HKSHOPU.hk.data.bean.ResourceSearch

import com.HKSHOPU.hk.databinding.*
import com.HKSHOPU.hk.utils.rxjava.RxBus
import com.HKSHOPU.hk.widget.view.KeyboardUtil
import com.tencent.mmkv.MMKV


//import kotlinx.android.synthetic.main.activity_main.*

class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding
    var keyword:String =""
    var keyword_catrgory:String =""
    var category_id = ""
    val userId = MMKV.mmkvWithID("http")!!.getString("UserId", "");
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        keyword_catrgory = MMKV.mmkvWithID("http")!!.getString("c_product_sub_category_selected","").toString()
        category_id = MMKV.mmkvWithID("http")!!.getString("product_category_id","").toString()
        initVM()
        initView()
        initFragment()
        initClick()

    }

    private fun initVM() {

    }
    private fun initView() {
        binding.layoutSearchMerchants.setOnClickListener {
            KeyboardUtil.showKeyboard(it)
        }
        binding.etSearchKeyword.setOnClickListener {
            KeyboardUtil.showKeyboard(it)
        }
        binding.etSearchKeyword.setText(keyword_catrgory)
        binding.etSearchKeyword.doAfterTextChanged {
            keyword = binding.etSearchKeyword.text.toString()

        }
        binding!!.etSearchKeyword.setOnEditorActionListener() { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    MMKV.mmkvWithID("http")!!
                        .putString("keyword",keyword)
                    RxBus.getInstance().post(EventToProductSearch())
                    RxBus.getInstance().post(EventToShopSearch())
                    KeyboardUtil.hideKeyboard(v)
                    true
                }

                else -> false
            }
        }

    }
    private fun initFragment() {
        binding!!.mviewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceSearch.pagerFragments_Search[position]
            }

            override fun getItemCount(): Int {
                return ResourceSearch.tabList_search.size
            }
        }
        TabLayoutMediator(binding!!.tabs, binding!!.mviewPager) { tab, position ->
            tab.text = getString(ResourceSearch.tabList_search[position])

        }.attach()

        binding!!.mviewPager.isSaveEnabled = false

//        binding.setViewPager(binding.mviewPager, arrayOf(getString(R.string.product),getString(R.string.info)))
    }
    private fun initClick() {

        binding.ivBackClick.setOnClickListener {

            finish()
        }

    }
    @JvmName("getUserId1")
    fun getUserId(): String? {
        return userId
    }

    fun getKeyWord(): String? {
        return keyword!!
    }

}