package com.hviewtech.wowpay.merchant.core.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Animatable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hviewtech.wowpay.merchant.core.R
import com.hviewtech.wowpay.merchant.core.bean.BaseResponse
import com.hviewtech.wowpay.merchant.core.config.Const.REQUEST_CODE
import com.hviewtech.wowpay.merchant.core.utils.ToastUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_base_loading.*

const val NEED_LOADING = "need_loading"

const val LOADING_BLOCK_ALL = 1
const val LOADING_BLOCK_NONE = 2

abstract class BaseActivity : AppCompatActivity() {
    private var loadingCount = 0
    private var isOpenDialog = false
    protected val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //限制整个app横屏显示
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (intent.hasExtra(NEED_LOADING) && intent.getBooleanExtra(NEED_LOADING, false)) {
            setContentView(getContentView())
        } else {
            val loading = layoutInflater.inflate(R.layout.activity_base_loading, null)
            val normal = layoutInflater.inflate(getContentView(), loading as ViewGroup)
            setContentView(normal)
            loading_image.bringToFront()
            if (loading_image.drawable is Animatable) {
                (loading_image.drawable as Animatable).start()
            }
        }
        initView()
        initData()
        initEvent()
    }

    abstract fun getContentView(): Int
    abstract fun initView()

    open fun initData() {}
    open fun initEvent() {}

    open fun canGoBack(): Boolean {
        return true
    }

    override fun onBackPressed() {
        isOpenDialog = false
        if (!canGoBack()) {
            return
        }
        super.onBackPressed()
    }

    fun showLoading(blockType: Int) {
        if (loadingCount == 0) {
            loading_image?.visibility = View.VISIBLE
            when (blockType) {
                LOADING_BLOCK_ALL -> {
                    loading_image?.setOnClickListener {
                        onLoadingClicked()
                    }
                }
                LOADING_BLOCK_NONE -> {
                }
            }
        }
        loadingCount++
    }

    fun hideLoading() {
        if (--loadingCount > 0) {
            return
        }
        loadingCount = 0
        loading_image?.visibility = View.GONE
        loading_image?.setOnClickListener(null)
    }

    open fun onLoadingClicked() {

    }

    fun setStatusBarColor(userTrans: Boolean) {
        if (userTrans) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            }
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            }
        }
    }

    fun checkResponseWithToast(bean: BaseResponse, isToast: Boolean? = true): Boolean {
        if (bean.code != REQUEST_CODE) {
            if (isToast!!) {
                ToastUtil.showToast(this, bean.msg)
            }
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    open fun openActivity(context: Context) {
        val intent = Intent(context, this::class.java)
        context.startActivity(intent)
    }
}