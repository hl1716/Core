package com.hviewtech.wowpay.merchant.core.bean

import com.google.gson.Gson

class BaseData(val data: Any?) : BaseResponse() {
    override fun toString(): String {
        var str = ""
        str = try {
            Gson().toJson(this)
        } catch (e: Exception) {
            data.toString()
        }
        return str
    }
}