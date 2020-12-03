package com.hviewtech.wowpay.merchant.core.base

import com.hviewtech.wowpay.merchant.core.utils.ToastUtil
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.hviewtech.wowpay.merchant.core.R
import com.hviewtech.wowpay.merchant.core.bean.BaseResponse
import com.hviewtech.wowpay.merchant.core.config.Const.REQUEST_CODE
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_base_loading.*

abstract class BaseFragment : Fragment() {
    private lateinit var loadingImage: ImageView
    private var loadingCount = 0
    protected val compositeDisposable = CompositeDisposable()
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loading = layoutInflater.inflate(R.layout.activity_base_loading, null)
        val normal = layoutInflater.inflate(getContentView(), loading as ViewGroup)
        loadingImage = loading.findViewById(R.id.loading_image)
        loadingImage.bringToFront()
        if (loadingImage.drawable is Animatable) {
            (loadingImage.drawable as Animatable).start()
        }
        return normal
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    abstract fun getContentView(): Int
    open fun initView() {}
    open fun initEvent() {}

    fun showFragmentLoading() {
        if (loadingCount == 0) {
            loading_image?.visibility = View.VISIBLE
        }
        loadingCount++
    }

    fun hideFragmentLoading() {
        if (--loadingCount > 0) {
            return
        }
        loadingCount = 0
        loading_image?.visibility = View.GONE
        loading_image?.setOnClickListener(null)
    }

    fun checkResponseWithToast(bean: BaseResponse, isToast: Boolean? = true): Boolean {
        if (bean.code != REQUEST_CODE) {
            if (isToast!!) {
                ToastUtil.showToast(context, bean.msg)
            }
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}
