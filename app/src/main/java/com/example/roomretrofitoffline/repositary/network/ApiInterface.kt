package com.example.roomretrofitoffline.repositary.network

import com.example.roomretrofitoffline.repositary.RemainsData
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/v2/top-headlines")
    fun getNewsDetail(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Single<RemainsData>
}