package com.hkshopu.hk.ui.main.fragment

import android.content.Intent
import androidx.recyclerview.widget.*
import com.hkshopu.hk.Base.BaseFragment
import com.hkshopu.hk.R
import com.hkshopu.hk.data.bean.ShopInfoBean
import com.hkshopu.hk.ui.main.activity.AddShopActivity
import com.hkshopu.hk.ui.main.adapter.ShopInfoAdapter
import com.hkshopu.hk.ui.user.activity.LoginActivity
import com.hkshopu.hk.utils.extension.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_shopmanage.*

class ShopManageFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_shopmanage
    private val adapter = ShopInfoAdapter()
    override fun initView() {
        initVM()
        initClick()
        val layoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.itemAnimator = DefaultItemAnimator()
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = adapter
        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as ShopInfoAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun initVM() {
        val list = ArrayList<ShopInfoBean>()
        val shopInfoBean = ShopInfoBean("1","","Test")
        list.add(shopInfoBean)
        adapter.setData(list)
    }

    fun initClick() {
        layout_addShop.setOnClickListener {

            val intent = Intent(context, AddShopActivity::class.java)
            context?.startActivity(intent)

        }
    }

    override fun initData() {

    }


}