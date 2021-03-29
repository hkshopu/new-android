package com.hkshopu.hk.ui.main.adapter

import android.graphics.Bitmap
import android.media.Image
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hkshopu.hk.R
import com.hkshopu.hk.data.bean.ItemPics
import com.hkshopu.hk.data.bean.ItemShippingFare
import com.hkshopu.hk.data.bean.ItemSpecification
import java.util.*

class ShippingFareAdapter: RecyclerView.Adapter<ShippingFareAdapter.mViewHolder>(), ITHelperInterface {

    var mutableList_shipMethod = mutableListOf<ItemShippingFare>()
    lateinit var customPaymentName: String


    inner class mViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        //把layout檔的元件們拉進來，指派給當地變數
        val value_shipping_name = itemView.findViewById<TextView>(R.id.value_shipping_name)
        val imgv_delFare = itemView.findViewById<ImageView>(R.id.imgView_deleteFare)

        init {

//            customPaymentName = value_shipping_name.text as String
//
//            val textWatcher = object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//                    if (s.toString().isEmpty()) {
//                        mutableList_shipMethod.remove(ItemShippingFare("", R.drawable.custom_unit_transparent))
//
//                    }
//                }
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                }
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    if (s.toString().isNotEmpty()) {
//
//                        mutableList_shipMethod.add(ItemShippingFare("", R.drawable.custom_unit_transparent))
//
//                    }
//                }
//            }
//            value_shipping_name.addTextChangedListener(textWatcher)


            imgv_delFare.setOnClickListener(this)

        }

        fun bind(item: ItemShippingFare){
            //綁定當地變數與dataModel中的每個值
            value_shipping_name.setText(item.ship_method_name)
            imgv_delFare.setImageResource(item.btn_delete)

        }

        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.imgView_deleteFare -> onItemDissmiss(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent:ViewGroup,viewType: Int):mViewHolder {

        //載入項目模板
        val inflater = LayoutInflater.from(parent.context)
        val example = inflater.inflate(R.layout.shipping_fare_list_item, parent, false)
        return mViewHolder(example)

    }

    override fun getItemCount() = mutableList_shipMethod.size

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {

        //呼叫上面的bind方法來綁定資料
        holder.bind(mutableList_shipMethod[position])

    }

    //更新資料用
    fun updateList(list:MutableList<ItemShippingFare>){
        mutableList_shipMethod = list
    }

    override fun onItemDissmiss(position: Int) {
        mutableList_shipMethod.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(mutableList_shipMethod,fromPosition,toPosition)
        notifyItemMoved(fromPosition,toPosition)
    }

}