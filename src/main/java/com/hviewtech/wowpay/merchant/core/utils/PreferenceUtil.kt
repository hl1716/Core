package com.hviewtech.wowpay.merchant.core.utils

import com.hviewtech.wowpay.merchant.core.base.BaseApplication
import android.content.Context
import android.content.SharedPreferences

object PreferenceUtil {
    private lateinit var sp: SharedPreferences

    private const val SP_NAME = "sp"
    private const val TOKEN = "token"
    private const val FIRE_BASE_TOKEN = "fire_base_token"
    private const val FIRST_LOGIN = "first_login"
    private const val LOGIN_STATUS = "login_status"
    private const val QUEST_ID = "quest_id"

    fun init(application: BaseApplication) {
        sp = application.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        sp.edit().putString(TOKEN, token).apply()
    }

    fun loadToken(): String? {
        return sp.getString(TOKEN, "")
    }

}
