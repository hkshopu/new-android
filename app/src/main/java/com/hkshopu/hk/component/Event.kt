package com.hkshopu.hk.component

import com.hkshopu.hk.data.bean.ShopCategoryBean

/**
 * Created by Administrator on 2018/4/20 0020.
 */
class EventLoginSuccess

class EventLogout

class EventShopNameUpdated(val shopName: String? = null)

class EventShopDesUpdated(val shopDes: String? = null)

class EventShopCatSelected(val list: ArrayList<ShopCategoryBean>)

class EventAddShopSuccess()

class EventGetShopListSuccess(val shopNums: Int = 0)

class EventRechargeSuccess

class EventRefreshHome

class EventReadHistoryUpdated(val bookId: Int? = null)

class EventReadToHome

class EventHideBottomBar

class EventShowBottomBar

class EventReturnComic

class EventToMine

class EventToRecharge

class EventToBulletin

class EventAutoSwitch
