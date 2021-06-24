package com.HKSHOPU.hk.ui.main.store.activity


import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.HKSHOPU.hk.Base.BaseActivity
import com.HKSHOPU.hk.R
import com.HKSHOPU.hk.component.CommonVariable
import com.HKSHOPU.hk.component.EventChangeShopCategory
import com.HKSHOPU.hk.component.EventShopCatSelected
import com.HKSHOPU.hk.data.bean.ShopCategoryBean
import com.HKSHOPU.hk.databinding.ActivityShopcategoryBinding
import com.HKSHOPU.hk.ui.main.store.adapter.CategoryMultiAdapter
import com.HKSHOPU.hk.ui.user.vm.ShopVModel
import com.HKSHOPU.hk.utils.rxjava.RxBus


class ShopCategoryActivity : BaseActivity() {
    private lateinit var binding: ActivityShopcategoryBinding

    private val VM = ShopVModel()
    private val adapter = CategoryMultiAdapter()
    var toShopFunction: Boolean = false
    val choosedlist = ArrayList<ShopCategoryBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopcategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toShopFunction = intent.getBundleExtra("bundle")!!.getBoolean("toShopFunction")
        initView()
        initRecyclerView()
        initVM()
        initEditText()
        initClick()


    }

    private fun initVM() {

    }

    private fun initView() {
        if (binding.tvSelected.text == "未選擇分類") {
            binding.tvSelected.isClickable = false
        }

    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        binding.recyclerview.layoutManager = layoutManager
        adapter.setData(CommonVariable.list)
        binding.recyclerview.adapter = adapter
        choosedlist.clear()
        adapter.itemClick = {

//            Log.d("ShopCategoryActivity", "Item ID：" + id_cat)
//            Log.d("ShopCategoryActivity", "Item selected：" + it.isSelect)
            if (it.isSelect == true) {
                choosedlist.add(it)
            }
            if (it.isSelect == false) {
                choosedlist.remove(it)
            }
            if (choosedlist.isEmpty()) {
                binding.tvSelected.text = "未選擇分類"
                binding.tvSelected.setTextColor(getColor(R.color.turquoise))
                binding.tvSelected.setBackgroundResource(R.drawable.customborder_turquise)
            }else{
                val items = choosedlist.size
                binding.tvSelected.text = "已選擇"+items+"項分類"
                binding.tvSelected.setTextColor(getColor(R.color.white))
                binding.tvSelected.setBackgroundResource(R.drawable.customborder_onboard_turquise_40p)
            }
            if(choosedlist.size > 3){
                binding.tvSelected.isClickable = false
                runOnUiThread {

                    Toast.makeText(this@ShopCategoryActivity, "最多只能選擇3項分類", Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.tvSelected.isClickable = true
            }
            if (choosedlist.size == 1) {
                binding.tvSelected.setTextColor(getColor(R.color.white))
                binding.tvSelected.setBackgroundResource(R.drawable.customborder_onboard_turquise_40p)
            } else if (choosedlist.size == 2) {
                binding.tvSelected.setTextColor(getColor(R.color.white))
                binding.tvSelected.setBackgroundResource(R.drawable.customborder_onboard_turquise_40p)
            } else if (choosedlist.size == 3) {
                binding.tvSelected.setTextColor(getColor(R.color.white))
                binding.tvSelected.setBackgroundResource(R.drawable.customborder_onboard_turquise_40p)
            }

        }
    }

    private fun initClick() {
        binding.titleBackShopcategory.setOnClickListener {

            finish()
        }
        binding.tvSelected.setOnClickListener {

            if (toShopFunction) {
                RxBus.getInstance().post(EventChangeShopCategory(choosedlist))
            } else {
                RxBus.getInstance().post(EventShopCatSelected(choosedlist))

            }
            finish()
        }

    }


    private fun initEditText() {
//        binding.etShopname.addTextChangedListener(this)
//        password1.addTextChangedListener(this)
    }


}