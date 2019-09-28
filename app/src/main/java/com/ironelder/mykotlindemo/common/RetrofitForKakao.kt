package com.ironelder.mykotlindemo.common

import com.ironelder.mykotlindemo.dao.DataVo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


object RetrofitForKakao {
    private var mRetrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getSearchService(): SearchForKakao = mRetrofit.create(SearchForKakao::class.java)

    interface SearchForKakao{
        @Headers("Authorization: KakaoAK afbf0e43176d96bb2f20d59e33ff0035")
        @GET("v2/search/{type}")
        fun requestSearchForKakao(@Path("type") type:String,
                      @Query("page") page: Int,
                      @Query("size") size: Int = 25,
                      @Query("query") query:String="Test"):Call<DataVo>
    }
}