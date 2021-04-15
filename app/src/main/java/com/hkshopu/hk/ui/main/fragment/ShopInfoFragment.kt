package com.hkshopu.hk.ui.main.fragment


import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.hkshopu.hk.R
import com.hkshopu.hk.application.App
import com.hkshopu.hk.component.EventAddShopSuccess
import com.hkshopu.hk.data.bean.ResourceStore
import com.hkshopu.hk.data.bean.ShopCategoryBean
import com.hkshopu.hk.databinding.FragmentShopinfoBinding
import com.hkshopu.hk.net.Web
import com.hkshopu.hk.net.WebListener
import com.hkshopu.hk.ui.main.activity.MyMerchantsActivity
import com.hkshopu.hk.ui.main.activity.ShopAttentionActivity
import com.hkshopu.hk.ui.main.activity.ShopIncomeActivity
import com.hkshopu.hk.ui.main.activity.ShopInfoModifyActivity
import com.hkshopu.hk.ui.user.activity.LoginActivity
import com.hkshopu.hk.utils.rxjava.RxBus
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ShopInfoFragment : Fragment(R.layout.fragment_shopinfo){

    companion object {
        fun newInstance(): ShopInfoFragment {
            val args = Bundle()
            val fragment = ShopInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private var binding: FragmentShopinfoBinding? = null
    private var fragmentShopInfoBinding: FragmentShopinfoBinding? = null
    private val pickCoverImage = 100
    private val pickImage = 200
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShopinfoBinding.bind(view)
        fragmentShopInfoBinding = binding

        initView()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                getActivity()!!.getSupportFragmentManager().beginTransaction().remove(this@ShopInfoFragment).commit();
            }
        })
    }

    fun initView() {
        var shop_id = arguments!!.getInt("shop_id",0)
        var url = "https://hkshopu-20700.df.r.appspot.com/shop/"+shop_id +"/show/"
        getShopInfo(url)
        binding!!.ratingBar.setRating(4.7f);

        initClick()
        initVM()
        initEvent()
        initFragment()

    }
    private fun initFragment() {
        binding!!.mviewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceStore.pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return ResourceStore.tabList.size
            }
        }
        TabLayoutMediator(binding!!.tabs, binding!!.mviewPager) { tab, position ->
            tab.text = getString(ResourceStore.tabList[position])

        }.attach()
//        binding.setViewPager(binding.mviewPager, arrayOf(getString(R.string.product),getString(R.string.info)))
    }


    fun initVM() {

    }

    @SuppressLint("CheckResult")
    fun initEvent() {

    }

    fun initClick() {

        binding!!.ivBack.setOnClickListener {
            getActivity()!!.getSupportFragmentManager().beginTransaction().remove(this).commit();
        }

        binding!!.ivShopImg.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding!!.layoutShopRate.setOnClickListener {

        }

        binding!!.layoutMerchants.setOnClickListener {
            val intent = Intent(activity, MyMerchantsActivity::class.java)
            activity!!.startActivity(intent)
        }

        binding!!.layoutAttention.setOnClickListener {
            val intent = Intent(activity, ShopAttentionActivity::class.java)
            activity!!.startActivity(intent)
        }
        binding!!.layoutIncome.setOnClickListener {
            val intent = Intent(activity, ShopIncomeActivity::class.java)
            activity!!.startActivity(intent)
        }



//        binding!!.layoutShoptitle.setOnClickListener {
//            // DialogFragment.show() will take care of adding the fragment
//            // in a transaction.  We also want to remove any currently showing
//            // dialog, so make our own transaction and take care of that here.
//            val ft = fragmentManager!!.beginTransaction()
//            val prev = fragmentManager!!.findFragmentByTag("dialog")
//            if (prev != null) {
//                ft.remove(prev)
//            }
//            ft.addToBackStack(null)
//
//            // Create and show the dialog.
//            val addNameDialogFragment = AddNameDialogFragment.newInstance()
//            addNameDialogFragment.setCancelable(false)
//            addNameDialogFragment.show(ft, "ShowEditName")
//        }

        binding!!.ivPencil.setOnClickListener {
            val intent = Intent(activity, ShopInfoModifyActivity::class.java)
            activity!!.startActivity(intent)
        }


    }
    private fun getShopInfo(url: String) {
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("ShopInfoFragment", "返回資料 resStr：" + resStr)
                    Log.d("ShopInfoFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("已找到商店資料!")) {

                        val translations: JSONArray = json.getJSONArray("shop")
                        Log.d("ShopInfoFragment", "返回資料 List：" + translations.toString())


                    }

                    activity!!.runOnUiThread {

//                        initRecyclerView()

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


    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentShopInfoBinding = null
        super.onDestroyView()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickCoverImage) {
            imageUri = data?.data

        } else if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding!!.ivShopImg.setImageURI(imageUri)
        }
    }

}