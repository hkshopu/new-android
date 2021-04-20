package com.hkshopu.hk.data.service

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.hkshopu.hk.data.bean.BaseResponse
import com.hkshopu.hk.data.bean.ShopInfoBean
import com.hkshopu.hk.net.ApiConstants
import com.hkshopu.hk.net.GsonProvider
import io.reactivex.Observable
import okhttp3.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.File
import java.io.IOException

interface ShopmanageService{

    @FormUrlEncoded
    @POST("${ApiConstants.API_PATH}/shop/checkShopNameIsExistsProcess/")
    fun shopnamecheck(@Field("shop_title") shop_title : String,) : Observable<BaseResponse<Any>>

    @FormUrlEncoded
    @POST("${ApiConstants.API_PATH}/shop/save/")
    fun adddnewshop(@Field("shop_title") shop_title : String,) : Observable<BaseResponse<Any>>

    @FormUrlEncoded
    @POST("${ApiConstants.API_PATH}product/save/")
    fun add_product(@Field("shop_id") shop_id : Int, @Field("product_category_id") product_category_id : Int, @Field("product_sub_category_id") product_sub_category_id: Int, @Field("product_title") product_title: String, @Field("quantity") quantity: Int, @Field("product_description") product_description: String, @Field("product_price") product_price: Int, @Field("shipping_fee") shipping_fee: Int, @Field("weight") weight: Int, @Field("new_secondhand") new_secondhand: String, @Field("product_pic") product_pic : MutableList<File>, product_spec_list :String, user_id: Int) : Observable<BaseResponse<Any>>


}