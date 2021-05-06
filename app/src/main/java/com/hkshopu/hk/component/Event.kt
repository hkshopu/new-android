package com.hkshopu.hk.component

import com.hkshopu.hk.data.bean.ProductCategoryBean
import com.hkshopu.hk.data.bean.ShopBankAccountBean
import com.hkshopu.hk.data.bean.ShopCategoryBean

/**
 * Created by Administrator on 2018/4/20 0020.
 */

//Login Events
class EventLoginSuccess
class EventLogout

//Add Shop Events
class EventShopNameUpdated(val shopName: String? = null)
class EventShopDesUpdated(val shopDes: String? = null)
class EventShopCatSelected(val list: ArrayList<ShopCategoryBean>)
class EventAddShopSuccess()
class EventGetShopCatSuccess(val list: ArrayList<String>)
class EventAddShopBriefSuccess(val description: String?)
class EventChangeShopPhoneSuccess(val phone: String?)
class EventChangeShopEmailSuccess(val email: String?)
class EventChangeShopTitleSuccess(val shopname: String?)
class EventGetBankAccountSuccess(val list: ArrayList<ShopBankAccountBean>)
//Add Product Events
class EventProductCatSelected(val selectrdId: Int = 1, var c_product_category: String)
class EventProductCatLastPostion(val postion: Int = 1)

//Other Events (Not Used)



class EventEmailShow(val show:Boolean,val email: String? = null)

class EventAutoSwitch
