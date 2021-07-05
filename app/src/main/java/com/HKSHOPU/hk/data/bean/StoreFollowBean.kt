package com.HKSHOPU.hk.data.bean

import com.google.gson.annotations.SerializedName

class StoreFollowBean {

    @SerializedName("shop_id")
    var shop_id: String= "";

    @SerializedName("pic_path_1")
    var pic_path_1: String= ""

    @SerializedName("pic_path_2")
    var pic_path_2: String= ""

    @SerializedName("pic_path_3")
    var pic_path_3: String= ""

    @SerializedName("shop_icon")
    var shop_icon: String= ""

    @SerializedName("shop_title")
    var shop_title: String= ""

    @SerializedName("rating")
    var rating: Double = 0.0;

    @SerializedName("follow_count")
    var follow_count: Int = 0

}