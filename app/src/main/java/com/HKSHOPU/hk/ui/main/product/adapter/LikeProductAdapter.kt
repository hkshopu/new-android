package com.HKSHOPU.hk.ui.main.store.adapter

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.HKSHOPU.hk.R
import com.HKSHOPU.hk.data.bean.BuyerProductsBean

import com.HKSHOPU.hk.ui.main.product.activity.MerchandiseActivity
import com.HKSHOPU.hk.utils.extension.inflate
import com.tencent.mmkv.MMKV


import org.jetbrains.anko.find

class LikeProductAdapter() : RecyclerView.Adapter<LikeProductAdapter.ShopInfoLinearHolder>(){

    var itemClick : ((id: Int) -> Unit)? = null

    var MMKV_product_id: Int = 1
    var mutablelist_buyerProductsBean: MutableList<BuyerProductsBean> = mutableListOf()

    fun setData(list : MutableList<BuyerProductsBean>){
        list?:return
        this.mutablelist_buyerProductsBean = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopInfoLinearHolder {
        val v = parent.context.inflate(R.layout.item_like_product,parent,false)

        return ShopInfoLinearHolder(v)
    }

    override fun getItemCount(): Int {
        return mutablelist_buyerProductsBean.size
    }
    fun removeAt(position: Int) {
        mutablelist_buyerProductsBean.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ShopInfoLinearHolder, position: Int) {
        val item = mutablelist_buyerProductsBean.get(position)
        holder.bindShop(item)

        holder.itemView.setOnClickListener{

            MMKV_product_id = mutablelist_buyerProductsBean.get(holder.adapterPosition).id
            MMKV.mmkvWithID("http").putInt("ProductId", MMKV_product_id)

            val intent = Intent(holder.itemView.context, MerchandiseActivity::class.java)
            holder.itemView.context?.startActivity(intent)
        }

    }

    inner class ShopInfoLinearHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val container = itemView.find<RelativeLayout>(R.id.layout_newproduct)
        val img_product = itemView.find<ImageView>(R.id.img_product)
        val tv_product_name = itemView.find<TextView>(R.id.tv_product_name)
        val tv_shop_name = itemView.find<TextView>(R.id.tv_shop_name)
        val tv_unit = itemView.find<TextView>(R.id.tv_unit)
        val price = itemView.find<TextView>(R.id.tv_price)
        val img_like = itemView.find<ImageView>(R.id.img_like)
        var like = false


        fun bindShop(bean : BuyerProductsBean){
            img_product.setImageResource(R.mipmap.no_image)
            tv_product_name.setText(bean.product_title)
            tv_shop_name.setText(bean.shop_title)

            if(bean.price.equals("-1")){
                price.setText("${bean.min_price}~${bean.max_price}")
            }else{
                price.setText(bean.price)
            }

            img_like.setOnClickListener {
                if(like){
                    img_like.setImageResource(R.mipmap.ic_heart_colorless)
                    like = false
                }else{
                    img_like.setImageResource(R.mipmap.ic_heart_colorful)
                    like = true
                }

            }
        }
    }




}