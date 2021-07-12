package com.HKSHOPU.hk.ui.main.buyer.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.HKSHOPU.hk.R
import com.HKSHOPU.hk.data.bean.BuyerAddressListBean
import com.HKSHOPU.hk.data.bean.BuyerProfileBean
import com.HKSHOPU.hk.databinding.FragmentBuyerprofileBinding
import com.HKSHOPU.hk.net.ApiConstants
import com.HKSHOPU.hk.net.Web
import com.HKSHOPU.hk.net.WebListener
import com.HKSHOPU.hk.ui.main.buyer.activity.*
import com.HKSHOPU.hk.ui.main.store.activity.HelpCenterActivity
import com.HKSHOPU.hk.ui.main.store.fragment.ShopInfoFragment
import com.HKSHOPU.hk.utils.extension.load
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tencent.mmkv.MMKV
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class BuyerProfileFragment : Fragment((R.layout.fragment_buyerprofile)) {
    companion object {
        fun newInstance(): BuyerProfileFragment {
            val args = Bundle()
            val fragment = BuyerProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private var binding: FragmentBuyerprofileBinding? = null
    private var fragmentBuyerprofileBinding: FragmentBuyerprofileBinding? = null
    var userId = MMKV.mmkvWithID("http").getString("UserId", "");
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuyerprofileBinding.bind(view)
        fragmentBuyerprofileBinding = binding


        if (userId!!.isEmpty()) {
            binding!!.tvProfiletitle.setText(R.string.guest)
            binding!!.progressBar5.isVisible = false
        } else {
            binding!!.progressBar5.isVisible = true
            var url_UserPeofile = ApiConstants.API_HOST + "user_detail/"+userId+"/profile/"
            getUserProfile(url_UserPeofile)

        }


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

                    return@OnKeyListener true
                }
            }
            false
        })

    }
    private fun initView(){
        binding!!.ivPencil.setOnClickListener {
            val intent = Intent(requireActivity(), BuyerInfoModifyActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding!!.layoutProfileRate.setOnClickListener{
            val intent = Intent(requireActivity(), BuyerEvaluationActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding!!.layoutPurchaselist.setOnClickListener {
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            val newFragment: PurchaseListFragment = PurchaseListFragment.newInstance()
            val args = Bundle()
//            args.putString("shop_id", it)
            newFragment.arguments = args
            ft.replace(R.id.layout_buyerprofile, newFragment, "PurchaseListFragment")
            ft.commit()
        }
        binding!!.layoutPendingPay.setOnClickListener {
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            val newFragment: PurchaseListFragment = PurchaseListFragment.newInstance()
            val args = Bundle()
            args.putString("page_id", "0")
            newFragment.arguments = args
            ft.replace(R.id.layout_buyerprofile, newFragment, "PurchaseListFragment")
            ft.commit()
        }

        binding!!.layoutPendingDelivery.setOnClickListener {
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            val newFragment: PurchaseListFragment = PurchaseListFragment.newInstance()
            val args = Bundle()
            args.putString("page_id", "1")
            newFragment.arguments = args
            ft.replace(R.id.layout_buyerprofile, newFragment, "PurchaseListFragment")
            ft.commit()
        }
        binding!!.layoutPendingReceive.setOnClickListener {
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            val newFragment: PurchaseListFragment = PurchaseListFragment.newInstance()
            val args = Bundle()
            args.putString("page_id", "2")
            newFragment.arguments = args
            ft.replace(R.id.layout_buyerprofile, newFragment, "PurchaseListFragment")
            ft.commit()
        }
        binding!!.layoutEvaluate.setOnClickListener {

        }
        binding!!.layoutCollects.setOnClickListener {
            val intent = Intent(requireActivity(), BuyerLikedListActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding!!.layoutFavorites.setOnClickListener {
            val intent = Intent(requireActivity(), BuyerFollowListActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding!!.layoutPath.setOnClickListener {
            val intent = Intent(requireActivity(), BuyerBrowsedListActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding!!.layoutMyaddress.setOnClickListener {
            val intent = Intent(requireActivity(), BuyerAddressListActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding!!.layoutMyaccount.setOnClickListener {
            val intent = Intent(requireActivity(), BuyerAccountSetupActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding!!.layoutHelpcenter.setOnClickListener {
            val intent = Intent(requireActivity(), HelpCenterActivity::class.java)
            requireActivity().startActivity(intent)
        }
    }

    private fun getUserProfile(url: String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<BuyerProfileBean>()
                list.clear()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("BuyerProfileFragment", "返回資料 resStr：" + resStr)
                    Log.d("BuyerProfileFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {
                        val jsonObject: JSONObject = json.getJSONObject("data")

                            val buyerProfileBean: BuyerProfileBean =
                                Gson().fromJson(jsonObject.toString(), BuyerProfileBean::class.java)

                            list.add(buyerProfileBean)

                        requireActivity().runOnUiThread {
                            binding!!.ivShopImg.load(list[0].pic)
                            binding!!.tvProfiletitle.text = list[0].name
                            binding!!.ratingBar.setRating(list[0].user_rating.toFloat())
                            binding!!.tvRating.text = list[0].user_rating.toString()
                            var url_UserLikedCount = ApiConstants.API_HOST + "user_detail/"+userId+"/liked_count/"
                            getUserLikedCount(url_UserLikedCount)
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

    private fun getUserLikedCount(url: String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("BuyerProfileFragment", "返回資料 resStr：" + resStr)
                    Log.d("BuyerProfileFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {
                        val likedCount = json.get("data")

                        requireActivity().runOnUiThread {
                            binding!!.myCollect.text = likedCount.toString()
                            var url_UserFollwedCount = ApiConstants.API_HOST + "user_detail/"+userId+"/followed_count/"
                            getUserFollwedCount(url_UserFollwedCount)
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
    private fun getUserFollwedCount(url: String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("BuyerProfileFragment", "返回資料 resStr：" + resStr)
                    Log.d("BuyerProfileFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {
                        val followCount = json.get("data")

                        requireActivity().runOnUiThread {
                            binding!!.myFavorites.text = followCount.toString()
                            var url_UserBrowseCount = ApiConstants.API_HOST + "user_detail/"+userId+"/browsed_count/"
                            getUserBrowseCount(url_UserBrowseCount)
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

    private fun getUserBrowseCount(url: String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("BuyerProfileFragment", "返回資料 resStr：" + resStr)
                    Log.d("BuyerProfileFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {
                        val browseCount = json.get("data")

                        requireActivity().runOnUiThread {
                            binding!!.myPath.text = browseCount.toString()
                           binding!!.progressBar5.isVisible = false
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