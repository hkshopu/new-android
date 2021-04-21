package com.hkshopu.hk.ui.main.activity

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast

import com.hkshopu.hk.Base.BaseActivity

import com.hkshopu.hk.R
import com.hkshopu.hk.component.EventPhoneShow
import com.hkshopu.hk.component.EventShopCatSelected
import com.hkshopu.hk.databinding.*
import com.hkshopu.hk.net.ApiConstants
import com.hkshopu.hk.net.Web
import com.hkshopu.hk.net.WebListener

import com.hkshopu.hk.ui.user.vm.AuthVModel
import com.hkshopu.hk.utils.rxjava.RxBus
import com.hkshopu.hk.widget.view.KeyboardUtil
import com.tencent.mmkv.MMKV
import com.zilchzz.library.widgets.EasySwitcher
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.URI


class AddShopAddressActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityShopaddresseditBinding

    private val VM = AuthVModel()
    var companyName: String = ""
    var phone: String = ""
    var country: String = ""
    var admin: String = ""
    var thoroughfare: String = ""
    var feature: String = ""
    var subaddress: String = ""
    var floor: String = ""
    var room: String = ""
    private lateinit var settings: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopaddresseditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settings = this.getSharedPreferences("shopdata", 0)
        initView()
        initVM()
        initClick()

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(p0: Editable?) {
        companyName = binding.editShopname.text.toString()
        phone = binding.editShopphone.text.toString()
        country = binding.editCountry.text.toString()
        admin = binding.editAdmin.text.toString()
        thoroughfare = binding.editthoroughfare.text.toString()
        feature = binding.editfeature.text.toString()
        subaddress = binding.editsubaddress.text.toString()
        floor = binding.editfloor.text.toString()
        room = binding.editroom.text.toString()
    }

    private fun initView() {
        binding.editShopname.addTextChangedListener(this)
        binding.editShopphone.addTextChangedListener(this)
        binding.editCountry.addTextChangedListener(this)
        binding.editAdmin.addTextChangedListener(this)
        binding.editthoroughfare.addTextChangedListener(this)
        binding.editfeature.addTextChangedListener(this)
        binding.editsubaddress.addTextChangedListener(this)
        binding.editfloor.addTextChangedListener(this)
        binding.editroom.addTextChangedListener(this)

        binding.layoutShopaddressEdit.setOnClickListener {
            KeyboardUtil.hideKeyboard(it)
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
        binding.ivBack.setOnClickListener {

            finish()
        }

        binding.tvCreateshop.setOnClickListener {
            var mImageUri = settings.getString("image", null);
            var uri = getMediaUriFromPath(this,mImageUri!!)
            var file = uri.toString()
            var shopName = settings.getString("shopname","")
            var shop_category_id1 = settings.getInt("shop_category_id1",0)
            var shop_category_id2= settings.getInt("shop_category_id2",0)
            var shop_category_id3= settings.getInt("shop_category_id3",0)
            var bankCode = settings.getString("bankcode","")
            var bankName = settings.getString("bankname","")
            var accountName = settings.getString("accountname","")
            var accountNumber = settings.getString("accountnumber","")
//            doAddShop(shopName,userId.toString(),shop_category_id1,shop_category_id2,shop_category_id3,file!!)
        }

    }
    fun getMediaUriFromPath(context: Context, path: String): Uri? {
        val mediaUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor? = context.getContentResolver().query(
            mediaUri,
            null,
            MediaStore.Images.Media.DISPLAY_NAME + "= ?",
            arrayOf(path.substring(path.lastIndexOf("/") + 1)),
            null
        )
        var uri: Uri? = null
        if (cursor!!.moveToFirst()) {
            uri = ContentUris.withAppendedId(
                mediaUri,
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            )
        }
        cursor.close()
        return uri
    }
    private fun doAddShop(
        shop_title: String,
        user_id: String,
        shop_category_id1: Int,
        shop_category_id2: Int,
        shop_category_id3: Int,
        postImg: File
    ) {
        val url = ApiConstants.API_HOST+"/shop/save/"
        val editor = settings.edit()
        val web = Web(object : WebListener {
            override fun onResponse(response: Response) {
                var resStr: String? = ""
                try {
                    resStr = response.body()!!.string()
                    val json = JSONObject(resStr)
                    Log.d("AddShopAddressActivity", "返回資料 resStr：" + resStr)
                    Log.d("AddShopAddressActivity", "返回資料 ret_val：" + json.get("ret_val"))
                    val ret_val = json.get("ret_val")
                    if (ret_val.equals("商店與選擇商店分類新增成功!")) {
                        var user_id: Int = json.getInt("user_id")
                        var shop_id:Int = json.getInt("shop_id")
                        MMKV.mmkvWithID("http").putInt("UserId", user_id)
                        MMKV.mmkvWithID("http").putInt("ShopId", shop_id)
                        editor.clear()
                        val intent = Intent(this@AddShopAddressActivity, ShopmenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@AddShopAddressActivity,
                                ret_val.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
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
        web.Do_ShopAdd(
            url,
            shop_title,
            user_id,
            shop_category_id1,
            shop_category_id2,
            shop_category_id3,
            postImg
        )
    }

}