package com.hviewtech.wowpay.merchant.core.net

import android.content.Context
import com.google.gson.Gson
import com.hviewtech.wowpay.merchant.core.bean.BaseResponse
import com.hviewtech.wowpay.merchant.core.utils.PreferenceUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIServer {
    private lateinit var api: APIFace
    private lateinit var mContext: Context
    private  var apiHost=""

    fun init() {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor {

                it.proceed(it.request())
            }
            .addInterceptor {
                addHeaderArgument(it)
            }
            .addInterceptor {
                getTokenHeader(it)
            }
            .addInterceptor {
                getBodyStatus(it)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(apiHost)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        api = retrofit.create(APIFace::class.java)
    }

    fun api(): APIFace {
        return api
    }

    private fun addHeaderArgument(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
        val token = PreferenceUtil.loadToken()
        if (!token.isNullOrEmpty()) {
            request.addHeader("Authorization", token)
        }
      //  request.addHeader("Version", getVersionName())
        request.addHeader("Platform", "android")
        return chain.proceed(request.build())
    }

    private fun getTokenHeader(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val token = response.headers().get("Authorization")
        if (!token.isNullOrEmpty()) {
            PreferenceUtil.saveToken(token)
        }
        val firebaseToken = response.headers().get("Base-Token")
        /*     if (!firebaseToken.isNullOrEmpty()) {
                 FirebaseUtil.updateToken(firebaseToken)
             }*/
        return response
    }

    private fun getBodyStatus(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        try {
            val responseBody = response.peekBody(1024 * 1024)
            val baseRequest = Gson().fromJson(responseBody.string(), BaseResponse::class.java)
            /*  if (baseRequest.code == 401) {
                  EventBus.getDefault().post(OpenDialogEvent(null, baseRequest.message))
              } else if (baseRequest.status == 403) {
                  PreferenceUtil.saveLoginState(false)
              }*/
        } catch (e: Exception) {

        }
        return response
    }

   /* private fun getVersionName(): String {
        val packageManager = App.instance.packageManager
        val packInfo = packageManager.getPackageInfo(App.instance.packageName, 0)
        return packInfo.versionName
    }*/

}
