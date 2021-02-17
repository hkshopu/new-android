package com.hkshopu.hk.ui.main.fragment


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.hkshopu.hk.Base.BaseFragment
import com.hkshopu.hk.R
import com.hkshopu.hk.application.App
import com.hkshopu.hk.data.bean.ItemData
import com.hkshopu.hk.ui.main.adapter.CategoryMultiAdapter
import com.hkshopu.hk.ui.main.adapter.CategorySingleAdapter
import com.roan.lyde.qoqo.component.EventShopDesUpdated
import com.roan.lyde.qoqo.component.EventShopNameUpdated
import com.roan.lyde.qoqo.utils.rxjava.RxBus
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_shopinfo.*
import kotlinx.android.synthetic.main.fragment_shopinfo.iv_shopImg
import kotlinx.android.synthetic.main.fragment_shopinfo.iv_shopImgadd
import kotlinx.android.synthetic.main.fragment_shopinfo.tv_shoptitle


class ShopInfoFragment : BaseFragment() {

    companion object {
        fun newInstance(): ShopInfoFragment {
            val args = Bundle()
            val fragment = ShopInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

//    val selectDatas: ArrayList<ItemData> = ArrayList()
    private val pickCoverImage = 100
    private val pickImage = 200
    private var imageUri: Uri? = null
    override fun getLayoutId() = R.layout.fragment_shopinfo
    private val adapter = CategorySingleAdapter()

    override fun initView() {
        initClick()
        initVM()
        initEvent()
        initListView()

    }

    private fun initListView() {
        val layoutManager = LinearLayoutManager(context)

        list_category.layoutManager = layoutManager
        list_category.isNestedScrollingEnabled = false
        list_category.itemAnimator = DefaultItemAnimator()
        (list_category.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        list_category.adapter = adapter
        adapter!!.setOnItemClickLitener(object : CategorySingleAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int,bean:ItemData) {
                adapter.setSelection(position)
                tv_category1.text = bean.title
                layout_category1.visibility = View.VISIBLE
                tv_category.visibility = View.GONE
                layout_categorylist.visibility = View.GONE
//                if (bean.isSelect) {
//                    selectDatas.add(bean)
//
//                } else {
//                    selectDatas.remove(bean)
//                }
//                if (selectDatas.size == 1) {
//                    tv_category1.text = selectDatas[0].title
//                    layout_category1.visibility = View.VISIBLE
//                } else if (selectDatas.size == 2) {
//                    tv_category1.text = selectDatas[0].title
//                    layout_category1.visibility = View.VISIBLE
//                    tv_category2.text = selectDatas[1].title
//                    layout_category2.visibility = View.VISIBLE
//                } else {
//                    tv_category1.text = selectDatas[0].title
//                    layout_category1.visibility = View.VISIBLE
//                    tv_category2.text = selectDatas[1].title
//                    layout_category2.visibility = View.VISIBLE
//                    tv_category3.text = selectDatas[2].title
//                    layout_category3.visibility = View.VISIBLE
//                }
            }

        })

    }

    fun initVM() {
        val list: ArrayList<ItemData> = ArrayList()
        list.add(ItemData("護膚化妝", false))
        list.add(ItemData("護理保健", false))
        list.add(ItemData("母嬰育兒", false))
        list.add(ItemData("寵物用品", false))
        list.add(ItemData("電子電器", false))
        list.add(ItemData("家品傢俬", false))
        list.add(ItemData("吃喝玩樂", false))
        list.add(ItemData("運動旅行", false))
        list.add(ItemData("時尚服飾", false))
        adapter.setData(list)
    }

    fun initEvent() {
        RxBus.getInstance().toMainThreadObservable(App.instance, Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventShopNameUpdated -> {
                        tv_shoptitle.text = it.shopName
                    }

                }
            }, {
                it.printStackTrace()
            })
        RxBus.getInstance().toMainThreadObservable(App.instance, Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventShopDesUpdated -> {
                        tv_shopDes.text = it.shopDes
                    }

                }
            }, {
                it.printStackTrace()
            })

    }

    fun initClick() {
        iv_shopCover.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickCoverImage)
        }
        iv_shopImgadd.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        btn_shop_setting.setOnClickListener {
            if (layout_shopsetting.visibility == View.GONE) {
                layout_shopsetting.visibility = View.VISIBLE

            } else {
                layout_shopsetting.visibility = View.GONE
            }
        }

        layout_shoptitle.setOnClickListener {
            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
            val ft = fragmentManager!!.beginTransaction()
            val prev = fragmentManager!!.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            // Create and show the dialog.
            val addNameDialogFragment = AddNameDialogFragment.newInstance()
            addNameDialogFragment.setCancelable(false)
            addNameDialogFragment.show(ft, "ShowEditName")
        }


        layout_addShop_detail.setOnClickListener {
            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
            val ft = fragmentManager!!.beginTransaction()
            val prev = fragmentManager!!.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            // Create and show the dialog.
            val addDesDialogFragment = AddDesDialogFragment.newInstance()
            addDesDialogFragment.setCancelable(false)
            addDesDialogFragment.show(ft, "ShowEditDetail")
        }


        tv_category.setOnClickListener {
            if (layout_categorylist.visibility == View.GONE) {
                layout_categorylist.visibility = View.VISIBLE
                layout_categorylist.bringToFront()
            } else {
                layout_categorylist.visibility = View.GONE
            }
        }
        delete_category1.setOnClickListener {
            layout_category1.visibility = View.GONE
            tv_category1.text = ""
            tv_category.visibility = View.VISIBLE
        }
        delete_category2.setOnClickListener {
            layout_category2.visibility = View.GONE
            tv_category2.text = ""
        }
        delete_category3.setOnClickListener {
            layout_category3.visibility = View.GONE
            tv_category3.text = ""
        }
        delete_fee1.setOnClickListener {
            layout_fee_no.visibility = View.GONE
        }
        delete_fee2.setOnClickListener {
            layout_fee_fix.visibility = View.GONE
        }
        tv_fee_setting_add.setOnClickListener {
            layout_fee_setting
            if (layout_fee_setting.visibility == View.GONE) {
                layout_fee_setting.visibility = View.VISIBLE
            } else {
                layout_fee_setting.visibility = View.GONE
            }
        }
        tv_confirm_shipping.setOnClickListener {
            layout_categorylist.visibility = View.GONE
        }
    }

    override fun initData() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickCoverImage) {
            imageUri = data?.data
            iv_shopCover.setImageURI(imageUri)
            tv_shopCoverset.visibility = View.INVISIBLE
        } else if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            iv_shopImg.setImageURI(imageUri)
        }
    }

}