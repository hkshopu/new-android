package com.HKSHOPU.hk.Base.response

/**
 * @Author: YangYang
 * @Date: 2017/12/28
 * @Version: 1.0.0
 * @Description:List类型的bean
 */
class UIListDataBean<T>(var status: Status, var data: MutableList<T>, var e: Throwable?) {

    constructor(status: Status, data: MutableList<T>) : this(status, data, null)


    /**
     * 判断请求是否成功
     */
    fun isSuccess(): Boolean {
        return status == Status.Success
    }
}