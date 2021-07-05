package com.HKSHOPU.hk.ui.main.store.activity


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.ViewPager
import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.component.*
import com.HKSHOPU.hk.databinding.ActivityMainBinding
import com.HKSHOPU.hk.ui.main.buyer.fragment.BuyerProfileFragment
import com.HKSHOPU.hk.ui.main.buyer.fragment.LoginFirstDialogFragment
import com.HKSHOPU.hk.ui.main.homepage.fragment.HomePageFragment
import com.HKSHOPU.hk.ui.main.store.fragment.*
import com.HKSHOPU.hk.utils.rxjava.RxBus
import com.tencent.mmkv.MMKV


class ShopmenuActivity : BaseActivity(), ViewPager.OnPageChangeListener {
    private lateinit var binding: ActivityMainBinding

    lateinit var manager: FragmentManager
    var page_position = 0
    val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
    var userId = MMKV.mmkvWithID("http").getString("UserId", "");
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initEvent()
        initClick()
        initFragment()


    }

    private val fragments = mutableListOf<Fragment>()
    private fun initFragment() {
        manager = supportFragmentManager
//        if (fragments.isNotEmpty()) return
        val homePageFragment = HomePageFragment.newInstance()
//        val FirstFragment = ShopInfoFragment.newInstance()
        val SecondFragment = BuyerProfileFragment.newInstance()
        val ShopListFragment = ShopListFragment.newInstance()
        fragments.add(homePageFragment)
        fragments.add(SecondFragment)
        fragments.add(ShopListFragment)
        binding.viewPager.adapter =
            object : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                override fun getItem(position: Int) = fragments[position]
                override fun getCount() = fragments.size
            }
        binding.viewPager.setPagingEnabled(false)
        binding.viewPager.addOnPageChangeListener(this)
//        binding.setViewPager(binding.mviewPager, arrayOf(getString(R.string.product),getString(R.string.info)))
    }

    fun initView() {
        binding.bottomNavigationViewLinear.setNavigationChangeListener { view, position ->
            Log.d("ShopMenuActivity", "BottomView position：" + position)
            page_position = position
            binding.viewPager.setCurrentItem(position, true);

        }

    }
    @SuppressLint("CheckResult")
    fun initEvent() {
        RxBus.getInstance().toMainThreadObservable(this, Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventToUserProfile -> {
                        binding.bottomNavigationViewLinear.setCurrentActiveItem(1)
                        binding.viewPager.setCurrentItem(1, true)
                    }

                }

            })
    }
    fun initClick() {


    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(page_position !=0){
            binding.bottomNavigationViewLinear.setCurrentActiveItem(0)
            binding.viewPager.setCurrentItem(0, true)
        }else{

            AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("您確定要離開 ？")
                .setNegativeButton("確定"){
                    // 此為 Lambda 寫法
                        dialog, which ->
                    finishAffinity()
                }
                .setPositiveButton("取消"){ dialog, which -> dialog.cancel()

                }
                .show()
        }

    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if(position == 1) {
            if(userId!!.isEmpty()) {
                runOnUiThread {

                    LoginFirstDialogFragment().show(
                        getSupportFragmentManager(),
                        "MyCustomFragment"
                    )
                }
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }


}
