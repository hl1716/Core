/* 
This file is output from yaml by python, if use yaml export file will be rewrite
*/
package com.hviewtech.wowpay.merchant.core.net

import com.hviewtech.wowpay.merchant.core.bean.BaseResponse
import io.reactivex.Observable
import retrofit2.http.*

interface APIFace {

    @GET("video/list/follow")
    fun demo(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Observable<BaseResponse>


    @POST("block/user")
    fun demo2(
        @Query("userId") userId: String
    ): Observable<BaseResponse>
}
