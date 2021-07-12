package com.HKSHOPU.hk.ui.main.buyer.fragment


import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.HKSHOPU.hk.R
import com.HKSHOPU.hk.component.*
import com.HKSHOPU.hk.data.bean.ResourceMerchant
import com.HKSHOPU.hk.data.bean.ShopAddressBean
import com.HKSHOPU.hk.data.bean.ShopInfoBean
import com.HKSHOPU.hk.databinding.FragmentPurchaselistBinding
import com.HKSHOPU.hk.databinding.FragmentShopinfoBinding
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.product.activity.AddNewProductActivity
import com.HKSHOPU.hk.ui.main.product.activity.MyMerchantsActivity
import com.HKSHOPU.hk.ui.main.store.activity.*
import com.HKSHOPU.hk.utils.extension.loadNovelCover
import com.HKSHOPU.hk.utils.rxjava.RxBus
import com.tencent.mmkv.MMKV
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class PurchaseListFragment : Fragment(R.layout.fragment_purchaselist) {

    companion object {
        fun newInstance(): PurchaseListFragment {
            val args = Bundle()
            val fragment = PurchaseListFragment()
            fragment.arguments = args
            return fragment
        }
    }
    var page_id=""
    private var binding: FragmentPurchaselistBinding? = null
    private var fragmentPurchaselistBinding: FragmentPurchaselistBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        page_id = requireArguments().getString("page_id", "")
        binding = FragmentPurchaselistBinding.bind(view)
        fragmentPurchaselistBinding = binding

        initView()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //go to previous fragemnt
                    //perform your fragment transaction here
                    //pass data as arguments
                    requireActivity().supportFragmentManager.beginTransaction().remove(this@PurchaseListFragment).commit()
                    return@OnKeyListener true
                }
            }
            false
        })


    }



    fun initView() {

        initClick()
        initVM()
        initEvent()
        initFragment()

    }

    private fun initFragment() {

        binding!!.mviewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceMerchant.pagerFragments_purchaselist[position]
            }

            override fun getItemCount(): Int {
                return ResourceMerchant.tabList_purchaselist.size
            }

        }

        TabLayoutMediator(binding!!.tabs, binding!!.mviewPager) { tab, position ->
            tab.text = getString(ResourceMerchant.tabList_purchaselist[position])
        }.attach()
        binding!!.mviewPager.setUserInputEnabled(false);


        binding!!.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        if(page_id.isNotEmpty()) {
            requireActivity().runOnUiThread {
                binding!!.mviewPager.setCurrentItem(page_id.toInt(), false)
            }
        }
//        binding.setViewPager(binding.mviewPager, arrayOf(getString(R.string.product),getString(R.string.info)))
    }

    fun initVM() {

    }

    @SuppressLint("CheckResult")
    fun initEvent() {
        RxBus.getInstance().toMainThreadObservable(this, Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventRefreshShopInfo -> {

                    }

                }
            }, {
                it.printStackTrace()
            })

    }

    fun initClick() {



        binding!!.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this@PurchaseListFragment).commit()
        }

        binding!!.ivNotify.setOnClickListener {
            val intent = Intent(activity, ShopNotifyActivity::class.java)
            startActivity(intent)
        }




    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentPurchaselistBinding = null
        super.onDestroyView()
    }

}