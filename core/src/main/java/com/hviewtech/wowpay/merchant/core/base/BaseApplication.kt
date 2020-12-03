package com.hviewtech.wowpay.merchant.core.base

import android.app.Application
import com.hviewtech.wowpay.merchant.core.utils.PreferenceUtil

class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        PreferenceUtil.init(this)
        instance = this
    }
}