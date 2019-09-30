package com.ironelder.mykotlindemo.common

import com.ironelder.mykotlindemo.dao.DataVo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchForKakaoApi{
    @Headers("Authorization: KakaoAK afbf0e43176d96bb2f20d59e33ff0035")
    @GET("v2/search/{type}")
    fun requestSearchForKakao(@Path("type") type:String,
                              @Query("page") page: Int,
                              @Query("size") size: Int = 25,
                              @Query("query") query:String="Test"): Call<DataVo>
}