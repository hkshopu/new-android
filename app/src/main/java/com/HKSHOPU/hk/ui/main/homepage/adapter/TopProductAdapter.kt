package com.HKSHOPU.hk.ui.main.homepage.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.HKSHOPU.hk.R

import com.HKSHOPU.hk.data.bean.TopProductBean
import com.HKSHOPU.hk.utils.extension.inflate
import com.HKSHOPU.hk.utils.extension.loadNovelCover
import com.HKSHOPU.hk.widget.view.click


import org.jetbrains.anko.find
import java.util.*

class TopProductAdapter (var currency: Currency): RecyclerView.Adapter<TopProductAdapter.TopProductLinearHolder>(){
    private var mData: ArrayList<TopProductBean> = ArrayList()
    private var newData: ArrayList<TopProductBean> = ArrayList()
    var itemClick : ((id: String) -> Unit)? = null
    var likeClick : ((id: String,like:String) -> Unit)? = null
    private var like_inner = ""
    fun setData(list : ArrayList<TopProductBean>){
        list?:return
        mData.clear()
        mData.addAll(list)
        newData = mData
        notifyDataSetChanged()
    }
    fun add(list: ArrayList<TopProductBean>) {
        list?:return
        newData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductLinearHolder {
        val v = parent.context.inflate(R.layout.item_top_products,parent,false)

        return TopProductLinearHolder(v)
    }

    override fun getItemCount(): Int {
        return newData.size
    }
    //更新資料用
    fun updateData(like: String){
        like_inner =like
        this.notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: TopProductLinearHolder, position: Int) {
        val item = newData.get(position)
        holder.bindShop(item)
    }

    inner class TopProductLinearHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val container = itemView.find<RelativeLayout>(R.id.layout_product)
        val image = itemView.find<ImageView>(R.id.iv_product)
        val like = itemView.find<ImageView>(R.id.iv_product_like)
        val like_click = itemView.find<ImageView>(R.id.iv_product_like_click)
        val title = itemView.find<TextView>(R.id.tv_productname)
        val shopname = itemView.find<TextView>(R.id.tv_shopname)
        val price = itemView.find<TextView>(R.id.tv_price)
        fun bindShop(topProductBean : TopProductBean){

            container.click {
                itemClick?.invoke(topProductBean.id)
            }
            image.loadNovelCover(topProductBean.pic_path)
            title .text = topProductBean.product_title
            shopname.text = topProductBean.shop_title
            price.text = currency.toString()+ topProductBean.price.toString()
            if(like_inner.equals("Y")){
                like.setImageResource(R.mipmap.ic_heart_red)
                like_click.click {
                    likeClick?.invoke(topProductBean.id,"N")
                }
            }else{
                like.setImageResource(R.mipmap.ic_heart_white)
                like_click.click {
                    likeClick?.invoke(topProductBean.id,"Y")
                }
            }


        }
    }



}