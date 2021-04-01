package com.hviewtech.wowpay.merchant.core.net

import android.content.Context
import com.hviewtech.wowpay.merchant.core.R
import com.hviewtech.wowpay.merchant.core.base.BaseActivity
import com.hviewtech.wowpay.merchant.core.utils.ToastUtil

import java.net.UnknownHostException

object ComErrorProcess {
    private fun errorCoverForBusiness(context: Context?, error: Throwable): String? {
        if (context == null) {
            return error.message
        }
        return when (error) {
            is UnknownHostException -> {
                context.getString(R.string.un_know_host)
            }
            else -> {
                error.message
            }
        }
    }

    fun errorWithToast(context: Context?, error: Throwable) {
        val message = errorCoverForBusiness(context, error)
        if (ToastUtil.isSameToastShow(message)) {
            return
        }
        ToastUtil.showToast(context, message)
    }

    fun errorWithToast(context: Context?, error: String?) {
        if (ToastUtil.isSameToastShow(error)) {
            return
        }
        ToastUtil.showToast(context, error)
    }

    fun errorWithToastCloseLoading(context: Context?, error: Throwable) {
        errorWithToast(context, error)
        if (context is BaseActivity) {
            context.hideLoading()
        }
    }

    fun errorWithToastCloseLoading(context: Context?, error: String?) {
        errorWithToast(context, error)
        if (context is BaseActivity) {
            context.hideLoading()
        }
    }
}
