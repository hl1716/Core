package com.hviewtech.wowpay.merchant.core.net

import com.hviewtech.wowpay.merchant.core.base.BaseApplication
import com.hviewtech.wowpay.merchant.core.utils.ToastUtil
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.hviewtech.wowpay.merchant.core.bean.BaseData
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by hl on 2020/12/2.
 * RetrofitClient封装单例类, 实现网络请求
 */
class RetrofitClient private constructor(
    url: String = baseUrl,
    headers: Map<String, String>? = null
) {
    private var cache: Cache? = null
    private var httpCacheDirectory: File? = null

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the `service` interface.
     */
    fun <T> create(service: Class<T>?): T {
        if (service == null) {
            throw RuntimeException("Api service is null!")
        }
        return retrofit.create(service)
    }

    companion object {
        //超时时间
        private const val DEFAULT_TIMEOUT = 20

        //缓存时间
        private const val CACHE_TIMEOUT = 10 * 1024 * 1024

        //服务端根路径
        @JvmField
        var baseUrl = "https://carpool.wm69.mintennet.com/"
        var imageUrl = "$baseUrl/image"
        private val mContext = BaseApplication.instance.baseContext
        private lateinit var okHttpClient: OkHttpClient
        private lateinit var retrofit: Retrofit

        @JvmStatic
        val instance: RetrofitClient
            get() = SingletonHolder.INSTANCE

        fun <T> execute(observable: Observable<T>, subscriber: Observer<T>?): T? {
            observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber!!)
            return null
        }
    }

    init {
        var url: String? = url
        if (TextUtils.isEmpty(url)) {
            url = baseUrl
        }
        if (httpCacheDirectory == null) {
            httpCacheDirectory = File(mContext.cacheDir, "dynapack_cache")
        }
        try {
            if (cache == null) {
                cache = Cache(httpCacheDirectory, CACHE_TIMEOUT.toLong())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val sslParams = HttpsUtils.getSslSocketFactory()

        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.i("---------", it)
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .addInterceptor(loggingInterceptor)
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
            .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(url)
            .build()

    }


    private fun getLogHttps(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        try {
            val responseBody = response.peekBody(1024 * 1024)
            val baseRequest = Gson().fromJson(responseBody.string(), BaseData::class.java)

            if (baseRequest.code == 401) {
                //EventBus.getDefault().post(OpenDialogEvent(null, baseRequest.msg))
            } else if (baseRequest.code == 504) {
                ToastUtil.showToast(BaseApplication(), "请检查网络！")
            }
        } catch (e: Exception) {

        }
        return response
    }
}