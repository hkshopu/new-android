package com.HKSHOPU.hk.ui.main.homepage.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.HKSHOPU.hk.Base.BaseActivity

import com.HKSHOPU.hk.data.bean.ResourceStoreRanking
import com.HKSHOPU.hk.databinding.*

//import kotlinx.android.synthetic.main.activity_main.*

class StoreRecommendActivity : BaseActivity() {
    private lateinit var binding: ActivityRecommendstoreBinding
    var userId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendstoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getBundleExtra("bundle")!!.getString("userId","")
        initVM()
        initFragment()
        initClick()

    }

    private fun initVM() {

    }
    private fun initFragment() {
        binding!!.mviewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceStoreRanking.pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return ResourceStoreRanking.tabList.size
            }
        }
        TabLayoutMediator(binding!!.tabs, binding!!.mviewPager) { tab, position ->
            tab.text = getString(ResourceStoreRanking.tabList[position])

        }.attach()
//        binding.setViewPager(binding.mviewPager, arrayOf(getString(R.string.product),getString(R.string.info)))
    }
    private fun initClick() {

        binding.ivBack.setOnClickListener {
            finish()
        }


    }

    @JvmName("getUserId1")
    fun getUserId(): String? {
        return userId
    }

}