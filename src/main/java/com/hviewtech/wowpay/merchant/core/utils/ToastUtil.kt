package com.hviewtech.wowpay.merchant.core.utils
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.hviewtech.wowpay.merchant.core.R
import kotlinx.android.synthetic.main.toast_layout.view.*

object ToastUtil {
    private var toast: Toast? = null
    private var showWord = ""
    fun showToast(context: Context?, message: String?) {
        if (context == null || message.isNullOrEmpty()) {
            return
        }
        showWord = message
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        toast?.setGravity(Gravity.CENTER, 0, 0)
        toast?.view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null, false)
        toast?.view?.text_show?.text = showWord
        toast?.show()
    }

    fun isSameToastShow(message: String?): Boolean {
        if (toast == null) {
            return false
        }

        if (toast?.view?.isShown == true) {
            if (showWord == message) {
                return true
            }
            return false
        }

        return false
    }

    fun cancelToast() {
        toast?.cancel()
    }
}
