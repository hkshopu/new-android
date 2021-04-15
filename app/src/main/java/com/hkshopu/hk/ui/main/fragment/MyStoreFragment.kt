package com.hkshopu.hk.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.hkshopu.hk.R
import com.hkshopu.hk.ui.main.activity.AddShopActivity
import com.hkshopu.hk.ui.main.activity.AddShopBriefActivity

class MyStoreFragment : Fragment() {

    companion object {
        fun newInstance(): MyStoreFragment {
            val args = Bundle()
            val fragment = MyStoreFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_mystore, container, false)
        var btn_store_addbrief = root.findViewById<ImageButton>(R.id.btn_store_addbrief)
        btn_store_addbrief.setOnClickListener{
            val intent = Intent(activity, AddShopBriefActivity::class.java)
            activity!!.startActivity(intent)

        }

        return root
    }


}