package com.HKSHOPU.hk.ui.main.store.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(var mAdapter: ITHelperInterface?): ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        //設定可往上及往下拖曳
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        //設定可往左及往右滑動
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        //若不想要其中一個功能就改成ACTION_STATE_IDLE
        //val dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    //設定item可滑動
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    //上下拖曳監聽事件
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //呼叫ITHelperInterface的方法
        mAdapter!!.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    //左右滑動監聽事件
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //呼叫ITHelperInterface的方法
        mAdapter!!.onItemDissmiss(viewHolder.adapterPosition)
    }
}