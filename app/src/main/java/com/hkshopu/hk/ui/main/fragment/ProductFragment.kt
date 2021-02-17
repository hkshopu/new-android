package com.hkshopu.hk.ui.main.fragment


import android.os.Bundle
import android.util.Log
import android.view.View
import com.hkshopu.hk.Base.BaseFragment
import com.hkshopu.hk.R
import com.tiper.MaterialSpinner


class ProductFragment : BaseFragment() {
    companion object {
        fun newInstance() : ProductFragment{
            val args = Bundle()
            val fragment = ProductFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private val listener by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                Log.v("MaterialSpinner", "onItemSelected parent=${parent.id}, position=$position")
                parent.focusSearch(View.FOCUS_UP)?.requestFocus()
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                Log.v("MaterialSpinner", "onNothingSelected parent=${parent.id}")
            }
        }
    }
    override fun getLayoutId() = R.layout.fragment_product

    override fun initView() {
        initClick()


    }

    fun initClick() {

    }

    override fun initData() {

    }


}