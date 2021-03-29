package com.hkshopu.hk.ui.main.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.hkshopu.hk.Base.BaseActivity
import com.hkshopu.hk.Base.response.Status
import com.hkshopu.hk.R
import com.hkshopu.hk.component.EventAddShopSuccess
import com.hkshopu.hk.component.EventShopCatSelected
import com.hkshopu.hk.data.bean.ShopCategoryBean
import com.hkshopu.hk.databinding.ActivityAddshopBinding
import com.hkshopu.hk.ui.user.vm.ShopVModel
import com.hkshopu.hk.utils.rxjava.RxBus
import com.hkshopu.hk.widget.view.KeyboardUtil

import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*

import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class AddShopActivity : BaseActivity(), TextWatcher {

    private lateinit var binding: ActivityAddshopBinding
    val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
        Log.d("AddShopActivity","errorHandler"+throwable)
    }
    private val VM = ShopVModel()
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var isSelectImage = false
    val userId = MMKV.mmkvWithID("http").getInt("UserId", 0);
    var shopName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddshopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                // 执行你的耗时操作代码
                doOnUiCode()
            }
        }

        initView()
        initEditText()
        initClick()
        initEvent()
        initVM()


    }
    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            // 更新你的UI
            binding.etShopname.setOnEditorActionListener() { v, actionId, event ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {

                        VM.shopnamecheck(this@AddShopActivity, shopName); true
                    }
                    else -> false
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

        shopName = binding.etShopname.text.toString()

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    private fun initVM() {
        VM.shopnameLiveData.observe(this, Observer {
            when (it?.status) {
                Status.Success -> {
                    if (it.ret_val.toString().equals("商店名稱未重複!")) {

                            binding.ivStep2.setImageResource(R.mipmap.ic_step2_check)
                            binding.ivStep2Check.visibility = View.VISIBLE

                    } else {
                        KeyboardUtil.showKeyboard(binding.etShopname)
                    }

                }
//                Status.Start -> showLoading()
//                Status.Complete -> disLoading()
            }
        })

        VM.addnewshopLiveData.observe(this, {
            when (it?.status) {
                Status.Success -> {
                    if (it.ret_val!!.equals("商店新增成功!")) {

                        RxBus.getInstance().post(EventAddShopSuccess())
                        finish()

                    } else {
                        val text1: String = it.ret_val.toString() //設定顯示的訊息
                        val duration1 = Toast.LENGTH_SHORT //設定訊息停留長短
                        Toast.makeText(this, text1, duration1)
                    }


                }

            }
        })

    }

    private fun initView() {
        binding.layoutAddshop.setOnClickListener {
            KeyboardUtil.hideKeyboard(binding.etShopname)
        }
        binding.tvAddnewshop.isClickable = false
    }

    @SuppressLint("CheckResult")
    fun initEvent() {
        var list: ArrayList<ShopCategoryBean>
        RxBus.getInstance().toMainThreadObservable(this, Lifecycle.Event.ON_DESTROY)
            .subscribe({
                when (it) {
                    is EventShopCatSelected -> {
                        list = it.list

                            if (list.size == 1) {

                                var storesort1 = list[0].c_shop_category
                                var storesort1_color = list[0].shop_category_background_color
                                binding.tvStoresort1.text = storesort1
                                binding.tvStoresort1.setBackgroundColor(
                                    Color.parseColor(
                                        storesort1_color
                                    )
                                )
                                binding.tvStoresort1.visibility = View.VISIBLE

                            } else if (list.size == 2) {
                                var storesort1 = list[0].c_shop_category
                                var storesort2 = list[1].c_shop_category
                                var storesort1_color = list[0].shop_category_background_color
                                var storesort2_color = list[1].shop_category_background_color
                                binding.tvStoresort1.text = storesort1
                                binding.tvStoresort1.setBackgroundColor(
                                    Color.parseColor(
                                        storesort1_color
                                    )
                                )
                                binding.tvStoresort1.visibility = View.VISIBLE
                                binding.tvStoresort2.text = storesort2
                                binding.tvStoresort2.setBackgroundColor(
                                    Color.parseColor(
                                        storesort2_color
                                    )
                                )
                                binding.tvStoresort2.visibility = View.VISIBLE
                            } else {
                                var storesort1 = list[0].c_shop_category
                                var storesort2 = list[1].c_shop_category
                                var storesort3 = list[2].c_shop_category
                                var storesort1_color = list[0].shop_category_background_color
                                var storesort2_color = list[1].shop_category_background_color
                                var storesort3_color = list[2].shop_category_background_color
                                binding.tvStoresort1.text = storesort1
                                binding.tvStoresort1.setBackgroundColor(
                                    Color.parseColor(
                                        storesort1_color
                                    )
                                )
                                binding.tvStoresort1.visibility = View.VISIBLE
                                binding.tvStoresort2.text = storesort2
                                binding.tvStoresort2.setBackgroundColor(
                                    Color.parseColor(
                                        storesort2_color
                                    )
                                )
                                binding.tvStoresort2.visibility = View.VISIBLE
                                binding.tvStoresort3.text = storesort3
                                binding.tvStoresort3.setBackgroundColor(
                                    Color.parseColor(
                                        storesort3_color
                                    )
                                )
                                binding.tvStoresort3.visibility = View.VISIBLE
                            }
                            binding.layoutStoresortPri.visibility = View.GONE
                            binding.layoutStoresortAct.visibility = View.VISIBLE
                            binding.ivStep3.setImageResource(R.mipmap.ic_step3_on)
                            binding.ivStep3Check.visibility = View.VISIBLE
                            if (binding.ivStep3Check.visibility == View.VISIBLE && binding.ivStep2Check.visibility == View.VISIBLE && binding.ivStep1Check.visibility == View.VISIBLE) {
                                binding.tvAddnewshop.setBackgroundResource(R.drawable.customborder_onboard_turquise_40p)
                                binding.tvAddnewshop.setTextColor(getColor(R.color.white))
                                binding.tvAddnewshop.isClickable = true
                            }
                        }


                }
            }, {
                it.printStackTrace()
            })

    }

    private fun initClick() {
        binding.titleBackAddshop.setOnClickListener {

            finish()
        }
        binding!!.ivShopImg.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

//        binding.etShopname.setOnClickListener{
//            Timer().schedule(1000){
//                KeyboardUtil.showKeyboard(it)
//            }
//        }

        binding.tvAddnewshop.setOnClickListener {
            if(isSelectImage){

            }
            VM.adddnewshop(this, shopName, userId.toString())
        }
        binding.tvMoreStoresort.setOnClickListener {
            val intent = Intent(this, ShopCategoryActivity::class.java)
            startActivity(intent)
        }

    }


    private fun initEditText() {
        binding.etShopname.addTextChangedListener(this)



//        password1.addTextChangedListener(this)
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        width = maxSize
        height = (width / bitmapRatio).toInt()
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onStart() {
        super.onStart()

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
                            binding!!.ivShopImg.setImageBitmap(bitmap)
                            binding.ivStep1Check.visibility = View.VISIBLE
                            binding.ivStep1.setImageResource(R.mipmap.ic_step1_check)
                            binding.ivStep2.setImageResource(R.mipmap.ic_step2_on)
                            isSelectImage = true


                        } else {
                            binding!!.ivShopImg.setImageDrawable(getDrawable(R.mipmap.ic_no_image))
                            isSelectImage = false
                        }
                    }else{
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        if (bitmap != null) {
                            runOnUiThread {
                                binding!!.ivShopImg.setImageBitmap(bitmap)
                                binding.ivStep1Check.visibility = View.VISIBLE
                                binding.ivStep1.setImageResource(R.mipmap.ic_step1_check)
                                binding.ivStep2.setImageResource(R.mipmap.ic_step2_on)
                            }

                            isSelectImage = true

                        }else{
                            binding!!.ivShopImg.setImageDrawable(getDrawable(R.mipmap.ic_no_image))
                            isSelectImage = false
                        }
                    }
                }

            } catch (e: Exception) {
                binding!!.ivShopImg.setImageResource(R.mipmap.ic_no_image)
                isSelectImage = false
                e.printStackTrace()
                //handle exception
            }

        }
    }
}