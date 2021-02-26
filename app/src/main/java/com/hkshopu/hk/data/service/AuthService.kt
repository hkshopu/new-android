package com.hkshopu.hk.data.service

import com.hkshopu.hk.data.bean.BaseResponse
import com.hkshopu.hk.net.ApiConstants
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
interface AuthService{
    @FormUrlEncoded
    @POST("${ApiConstants.API_PATH}/user/socialLoginProcess/")
    fun sociallogin(@Field("email") email : String,@Field("facebook_account") facebook_account : String,@Field("google_account") google_account : String,@Field("apple_account") apple_account: String) : Observable<BaseResponse<Any>>

    @FormUrlEncoded
    @POST("${ApiConstants.API_PATH}/user/registerProcess/")
    fun register(@Field("account_name") account_name : String,@Field("email") email : String,@Field("password") password : String,@Field("confirm_password") confirm_password : String,@Field("first_name") first_name : String,@Field("last_name") last_name : String,@Field("gender") gender : String,@Field("birthday") birthday : String,@Field("phone") phone : String,@Field("address") address : String) : Observable<BaseResponse<Any>>


    @FormUrlEncoded
    @POST("${ApiConstants.API_PATH}/user/loginProcess/")
    fun login(@Field("email") email : String,@Field("password") password : String) : Observable<BaseResponse<Any>>

    @FormUrlEncoded
    @POST("${ApiConstants.API_PATH}user/reset/password/")
    fun reset(@Field("phone") phone : String,@Field("passwordOrig") password_orig : String,@Field("password") password : String) : Observable<BaseResponse<Any>>

}