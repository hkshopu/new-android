package com.hkshopu.hk.ui.main.store.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.google.gson.Gson

import com.hkshopu.hk.Base.BaseActivity

import com.hkshopu.hk.R
import com.hkshopu.hk.component.*
import com.hkshopu.hk.component.CommonVariable.Companion.addresslist
import com.hkshopu.hk.data.bean.ShopAddressBean
import com.hkshopu.hk.data.bean.ShopBankAccountBean
import com.hkshopu.hk.data.bean.ShopInfoBean
import com.hkshopu.hk.databinding.ActivityShopinfomodifyBinding
import com.hkshopu.hk.net.ApiConstants
import com.hkshopu.hk.net.Web
import com.hkshopu.hk.net.WebListener

import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.utils.extension.loadNovelCover
import com.hkshopu.hk.utils.rxjava.RxBus
import com.tencent.mmkv.MMKV
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class ShopInfoModifyActivity : BaseActivity() {
    private lateinit var binding: ActivityShopinfomodifyBinding

    private val VM = AuthVModel()
    private val pickImage = 100
    private val pickImage_s = 200
    private var imageUri: Uri? = null
    private var imageUri_s: Uri? = null
    private var isSelectImage = false
    private var isSelectImage_s = false
    val shopId = MMKV.mmkvWithID("http").getInt("ShopId",0)
    var url = ApiConstants.API_HOST + "/shop/" + shopId + "/show/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopinfomodifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getShopInfo(url)
        initVM()
        initEvent()
        initClick()

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
    @SuppressLint("CheckResult")
    fun initEvent() {
        RxBus.getInstance().toMainThreadObservable(this, Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventChangeShopTitleSuccess -> {
                        binding.tvShopName.text  =it.shopname
                    }
                    is EventAddShopBriefSuccess -> {
                       binding.tvShopBrief.text = it.description
                    }

                    is EventChangeShopPhoneSuccess -> {
                        binding.tvShopPhone.text = it.phone
                    }

                    is EventChangeShopEmailSuccess -> {
                        binding.tvUserEmail.text = it.email
                    }
                }

            })
    }


    private fun initClick() {
        binding.ivBack.setOnClickListener {

            finish()
        }

        binding.tvShoppicBAdd.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding.ivShopImgEdit.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage_s)
        }

        binding.ivChevronShopName.setOnClickListener {
            val intent = Intent(this, ShopNameEditActivity::class.java)
            startActivity(intent)
        }

        binding.ivChevronShopBrief.setOnClickListener {
            val intent = Intent(this, AddShopBriefActivity::class.java)
            startActivity(intent)
        }

        binding.ivChevronShopPhone.setOnClickListener{
            val intent = Intent(this, PhoneEditActivity::class.java)
            startActivity(intent)
        }

        binding.ivChevronUserEmail.setOnClickListener{
            val intent = Intent(this, EmailAdd1Activity::class.java)
            startActivity(intent)
        }

        binding.ivChevronUserSocialaccount.setOnClickListener{
            val intent = Intent(this, SocialAccountSetActivity::class.java)
            startActivity(intent)
        }

//        binding.ivChevronPwchange.setOnClickListener{
//
//        }

        binding.tvSave.setOnClickListener {

        }

    }
    private fun getShopInfo(url: String) {

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                val list = ArrayList<ShopInfoBean>()
                list.clear()
                var addresslist = java.util.ArrayList<ShopAddressBean>()
                addresslist.clear()
                val shop_category_id_list = ArrayList<String>()
                shop_category_id_list.clear()
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("ShopInfoFragment", "返回資料 resStr：" + resStr)
                    Log.d("ShopInfoFragment", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("已找到商店資料!")) {

                        val jsonObject: JSONObject = json.getJSONObject("data")
                        Log.d("ShopInfoFragment", "返回資料 Object：" + jsonObject.toString())
                        val shopInfoBean: ShopInfoBean =
                            Gson().fromJson(jsonObject.toString(), ShopInfoBean::class.java)
                        list.add(shopInfoBean)

                        val shopaddress: JSONArray = jsonObject.getJSONArray("shop_address")
                        if (shopaddress.length() > 0) {
                            for (i in 0 until shopaddress.length()) {
                                val address = shopaddress.get(i)
                                val shopAddressBean: ShopAddressBean =
                                    Gson().fromJson(address.toString(), ShopAddressBean::class.java)
                                addresslist.add(shopAddressBean)
                            }
                        }

                        runOnUiThread {
                            binding!!.tvShopName.text = list[0].shop_title
                            binding!!.tvShopBrief.text = list[0].long_description
                            binding.tvShopPhone.text = addresslist[0].phone
                            binding.tvUserEmail.text = list[0].shop_email
                            binding!!.ivShopImg.loadNovelCover(list[0].shop_icon)

                        }


                    }
//                        initRecyclerView()

                } catch (e: JSONException) {

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onErrorResponse(ErrorResponse: IOException?) {

            }
        })
        web.Get_Data(url)
    }

    private fun doShopIconUpdate(postImg: File) {
        val shopId = MMKV.mmkvWithID("http").getInt("ShopId",0)
        var url = ApiConstants.API_PATH+"shop/"+shopId+"/update/"

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("ShopInfoModifyActivity", "返回資料 resStr：" + resStr)

                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {
                        Toast.makeText(this@ShopInfoModifyActivity, ret_val.toString(), Toast.LENGTH_SHORT).show()
                    } else {
                        runOnUiThread {

                            Toast.makeText(this@ShopInfoModifyActivity, ret_val.toString(), Toast.LENGTH_SHORT).show()
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
        web.Do_ShopIconUpdate(url,postImg)
    }
    private fun doShopPicUpdate(postImg: File) {
        val shopId = MMKV.mmkvWithID("http").getInt("ShopId",0)
        var url = ApiConstants.API_PATH+"shop/"+shopId+"/update/"

        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("ShopInfoModifyActivity", "返回資料 resStr：" + resStr)

                    val ret_val = json.get("ret_val")
                    val status = json.get("status")
                    if (status == 0) {
                        Toast.makeText(this@ShopInfoModifyActivity, ret_val.toString(), Toast.LENGTH_SHORT).show()
                    } else {
                        runOnUiThread {

                            Toast.makeText(this@ShopInfoModifyActivity, ret_val.toString(), Toast.LENGTH_SHORT).show()
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
        web.Do_ShopPicUpdate(url,postImg)
    }

    private fun processImage(): File? {
        val drawable = binding.ivShopImg.drawable as BitmapDrawable
        val bmp = drawable.bitmap
        val bmpCompress = getResizedBitmap(bmp, 200)
        val file: File
        val path = getExternalFilesDir(null).toString()
        file = File(path, "image" + ".jpg")
        try {
            var stream: OutputStream? = null
            stream = FileOutputStream(file)
            bmpCompress!!.compress(Bitmap.CompressFormat.JPEG, 85, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) // Catch the exception
        {
            e.printStackTrace()
        }
        return file
    }

    private fun processImageB(): File? {
        val drawable = binding.ivShoppicB.drawable as BitmapDrawable
        val bmp = drawable.bitmap
        val bmpCompress = getResizedBitmap(bmp, 1440)
        val file: File
        val path = getExternalFilesDir(null).toString()
        file = File(path, "image" + ".jpg")
        try {
            var stream: OutputStream? = null
            stream = FileOutputStream(file)
            bmpCompress!!.compress(Bitmap.CompressFormat.JPEG, 85, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) // Catch the exception
        {
            e.printStackTrace()
        }
        return file
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        width = maxSize
        height = (width / bitmapRatio).toInt()
        return Bitmap.createScaledBitmap(image, width, height, true)
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
                            binding!!.ivShoppicB.setImageBitmap(bitmap)

                            isSelectImage = true
                            val file = processImageB()
                            doShopPicUpdate(file!!)

                        } else {

                            isSelectImage = false
                        }
                    }else{
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        if (bitmap != null) {
                            runOnUiThread {
                                binding!!.ivShoppicB.setImageBitmap(bitmap)
                                binding.tvShoppicBAdd.setText(R.string.modify_newbg)
                                val file = processImageB()
                                doShopPicUpdate(file!!)
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

        }else if(resultCode == RESULT_OK && requestCode == pickImage_s){
            imageUri_s = data?.data

            try {
                imageUri_s?.let {
                    if(Build.VERSION.SDK_INT <= 28) {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri_s)
                        if (bitmap != null) {
                            binding!!.ivShopImg.setImageBitmap(bitmap)

                            isSelectImage_s = true
                            val file = processImage()
                            doShopIconUpdate(file!!)

                        } else {

                            isSelectImage_s = false
                        }
                    }else{
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri_s!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        if (bitmap != null) {
                            runOnUiThread {
                                binding!!.ivShopImg.setImageBitmap(bitmap)
                                val file = processImage()
                                doShopIconUpdate(file!!)
                            }

                            isSelectImage_s = true

                        }else{

                            isSelectImage_s = false
                        }
                    }
                }

            } catch (e: Exception) {

                isSelectImage_s = false
                e.printStackTrace()
                //handle exception
            }
        }
    }
}