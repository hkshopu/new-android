package com.HKSHOPU.hk.data.bean

import com.google.gson.annotations.SerializedName

class BuyerProductsBean {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("product_title")
    var product_title: String= ""

    @SerializedName("price")
    var price: String= ""

    @SerializedName("min_price")
    var min_price: Int = 0

    @SerializedName("max_price")
    var max_price: Int = 0

    @SerializedName("shop_title")
    var shop_title: Int = 0

}