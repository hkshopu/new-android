package com.hkshopu.hk.ui.main.store.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.hkshopu.hk.R
import com.hkshopu.hk.databinding.FragmentFirstBinding
import com.hkshopu.hk.databinding.FragmentShoplistBinding

class FirstFragment : Fragment((R.layout.fragment_first)) {

    companion object {
        fun newInstance(): FirstFragment {
            val args = Bundle()
            val fragment = FirstFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private var binding: FragmentFirstBinding? = null
    private var fragmentFirstBinding: FragmentFirstBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)
        fragmentFirstBinding = binding
        initView()

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("FirstFragment", "Fragment back pressed invoked")
                    // Do custom work here

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        AlertDialog.Builder(activity!!)
                            .setTitle("")
                            .setMessage("您確定要離開 ？")
                            .setPositiveButton("確定"){
                                // 此為 Lambda 寫法
                                    dialog, which ->requireActivity().onBackPressed()
                            }
                            .setNegativeButton("取消"){ dialog, which -> dialog.cancel()

                            }
                            .show()

                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }

                }
            }
            )

    }
    private fun initView(){
        binding!!.btnLearnMore.setOnClickListener {
            val url = "http://www.hkshopu.com/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

}