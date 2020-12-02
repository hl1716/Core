package com.hviewtech.wowpay.merchant.core.net


@JvmSuppressWildcards
interface ApiService {
/*    *//*获取接口Token*//*
    @POST("api/token/code")
    fun code(): Observable<com.hviewtech.wowpay.merchant.core.bean.BaseData>

    *//*平台价格*//*
    @POST("api/price/page_data")
    fun price(): Observable<BaseDataResponse<Price>>*/
}