package com.HKSHOPU.hk.component

import com.HKSHOPU.hk.data.bean.ShopBankAccountBean
import com.HKSHOPU.hk.data.bean.ShopCategoryBean

//Login Events
class EventLoginSuccess
class EventLogout

//Add Shop Events
class EventShopNameUpdated(val shopName: String? = null)
class EventShopDesUpdated(val shopDes: String? = null)
class EventShopCatSelected(val list: ArrayList<ShopCategoryBean>)

class EventChangeShopCategory(val list: ArrayList<ShopCategoryBean>)
class EventAddShopSuccess()
class EventGetShopCatSuccess(val list: ArrayList<String>)
class EventAddShopBriefSuccess(val description: String?)
class EventChangeShopPhoneSuccess(val phone: String?)
class EventChangeShopEmailSuccess(val email: String?)
class EventChangeShopTitleSuccess(val shopname: String?)
class EventGetBankAccountSuccess(val list: ArrayList<ShopBankAccountBean>)
//Add Product Events

class EventProductCatSelected(val selectrdId: Int = 1, var c_product_category: String)
class EventProductCatSelectedToSearch(val selectrdId: String = "", var c_product_category_selected: String)
class EventProductCatLastPostion(val postion: Int = 1)

class EventTransferToFragmentAfterUpdate(val index : Int)

class EventCheckShipmentEnableBtnOrNot(val boolean : Boolean)

class EventCheckFirstSpecEnableBtnOrNot(val boolean : Boolean)
class EventCheckSecondSpecEnableBtnOrNot(val boolean : Boolean)
class EventCheckInvenSpecEnableBtnOrNot(val boolean : Boolean)

class EventInvenSpecDatasRebuild(val boolean : Boolean)

//Other Events (Not Used)

//Add Product Events
class EventProductSearch(val keyword: String = "")
class EventProductDelete(val boolean: Boolean)
class EventdeleverFragmentAfterUpdateStatus(val action : String)
class EventRefreshShopInfo
//Other Events (Not Used)
class EventRefreshUserInfo
class EventPhoneShow(val show:Boolean,val phone: String? = null)


class EventRefreshShopList
class EventRefreshAddressList
class EventRefreshUserAddressList
class EventRefreshFpsAccountList

class EventToUserProfile
class EventToShopSearch
class EventToProductSearch()

class EventEmailShow(val show:Boolean,val email: String? = null)

class EventSyncBank

class EventShopPreViewRankAll(val shopId: String? = null)
