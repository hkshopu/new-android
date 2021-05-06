package com.hkshopu.hk.ui.main.store.activity

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged

import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.component.EventAddShopBriefSuccess
import com.hkshopu.hk.component.EventGetShopCatSuccess

import com.hkshopu.hk.databinding.ActivityAddshopbriefBinding
import com.hkshopu.hk.net.ApiConstants
import com.hkshopu.hk.net.Web
import com.hkshopu.hk.net.WebListener
import com.hkshopu.hk.ui.user.activity.BuildAccountActivity


import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.utils.rxjava.RxBus
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKV.mmkvWithID
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class AddShopBriefActivity : BaseActivity() {
    private lateinit var binding: ActivityAddshopbriefBinding

    private val VM = AuthVModel()
    private val pickImage = 200
    private var imageUri: Uri? = null
    private var isSelectImage = false
    private lateinit var description:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddshopbriefBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initVM()
        initClick()



    }
    private fun initView(){
        val phoneShow:Boolean = mmkvWithID("http").getBoolean("PhoneShow",false)
        val phone:String? = mmkvWithID("http").getString("phone","")
        if(phoneShow){
            binding.tvAddshopbriefContact.visibility = View.VISIBLE
            binding.ivAddshopbriefContact1.visibility = View.VISIBLE
            binding.tvAddshopbriefPhone.text = phone
        }
        val emailShow:Boolean = mmkvWithID("http").getBoolean("EmailShow",false)
        val email:String? = mmkvWithID("http").getString("email","")
        if(emailShow){
            binding.tvAddshopbriefContact.visibility = View.VISIBLE
            binding.ivAddshopbriefEmail.visibility = View.VISIBLE
            binding.tvAddshopbriefEmail.text = email
        }
        val addressShow:Boolean = mmkvWithID("http").getBoolean("AddressShow",false)
        val address:String? = mmkvWithID("http").getString("address","")
        if(addressShow){
            binding.tvAddshopbriefContact.visibility = View.VISIBLE
            binding.ivAddshopbriefAddress.visibility = View.VISIBLE
            binding.tvAddshopbriefAddress.text = address
        }
        binding.etAddshopbrief.doAfterTextChanged {
            description =  binding.etAddshopbrief.text.toString()
        }
    }
    private fun initVM() {
//        VM.socialloginLiveData.observe(this, Observer {
//            when (it?.status) {
//                Status.Success -> {
//                    if (url.isNotEmpty()) {
//                        toast("登录成功")
//
//                    }
//
//                    finish()
//                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
//            }
//        })
    }

    private fun initClick() {
        binding.layoutAddshopbrief.setOnClickListener {
            KeyboardUtil.hideKeyboard(it)
        }
        binding.titleBackAddshopbrief.setOnClickListener {

            finish()
        }
        binding.layoutShopbg.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        binding.ivAddshopbriefShare.setOnClickListener {

        }

        binding.ivAddshopbriefSave.setOnClickListener {
            doShopDesUpdate(description)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data

            try {
                imageUri?.let {
                    if(Build.VERSION.SDK_INT <= 28) {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri)
                        if (bitmap != null) {
                            binding.ivNoimage.visibility = View.INVISIBLE
                            binding!!.ivShopimage.setImageBitmap(bitmap)

                            isSelectImage = true


                        } else {

                            isSelectImage = false
                        }
                    }else{
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        if (bitmap != null) {
                            runOnUiThread {
                                binding.ivNoimage.visibility = View.INVISIBLE
                                binding!!.ivShopimage.setImageBitmap(bitmap)

                            }

                            isSelectImage = true

                        }else{

                            isSelectImage = false
                        }
                    }
                }

            } catch (e: Exception) {

                isSelectImage = false
                e.printStackTrace()
                //handle exception
            }

        }
    }

    private fun doShopDesUpdate(description: String) {
        val shopId = mmkvWithID("http").getInt("ShopId",0)
        var url = ApiConstants.API_PATH+"shop/"+shopId+"/update/"

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("AddShopBriefActivity", "返回資料 resStr：" + resStr)
                    Log.d("AddShopBriefActivity", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {
                        RxBus.getInstance().post(EventAddShopBriefSuccess(description))
                        finish()
                    } else {
                        runOnUiThread {

                            Toast.makeText(this@AddShopBriefActivity, ret_val.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: JSONException) {

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onErrorResponse(ErrorResponse: IOException?) {

            }
        })
        web.Do_ShopDesUpdate(url, description)
    }


}