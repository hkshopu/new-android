package com.hkshopu.hk.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hkshopu.hk.R
import com.hkshopu.hk.ui.main.activity.AccountSetupActivity
import com.hkshopu.hk.ui.main.activity.AddShopActivity
import com.hkshopu.hk.ui.main.activity.AddShopBriefActivity

class ShopFunctionFragment : Fragment() {

    companion object {
        fun newInstance(): ShopFunctionFragment {
            val args = Bundle()
            val fragment = ShopFunctionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_shopfunction, container, false)
        var tv_store_add = root.findViewById<TextView>(R.id.tv_more_storeadd)
        tv_store_add.setOnClickListener{
            val intent = Intent(activity, AddShopActivity::class.java)
            activity!!.startActivity(intent)

        }

        var tv_account_setup = root.findViewById<TextView>(R.id.tv_more_acntset)
        tv_account_setup.setOnClickListener{
            val intent = Intent(activity, AccountSetupActivity::class.java)
            activity!!.startActivity(intent)

        }
        return root
    }

}