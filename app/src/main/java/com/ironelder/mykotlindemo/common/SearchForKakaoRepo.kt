package com.ironelder.mykotlindemo.common

import androidx.annotation.NonNull
import com.ironelder.mykotlindemo.dao.DataVo
import retrofit2.Call;
import retrofit2.Response;

class SearchForKakaoRepo {
    private val searchForKakaoApi:SearchForKakaoApi

    constructor(@NonNull searchForKakaoApi:SearchForKakaoApi)
    {
        this.searchForKakaoApi = searchForKakaoApi
    }

    fun searchForKakao(
        type: String,
        page: Int,
        query: String,
        callback: CallbackListener
    ) {
        val call = searchForKakaoApi.requestSearchForKakao(type, page, 25, query)
        call.enqueue(object : retrofit2.Callback<DataVo> {
            override fun onResponse(call: Call<DataVo>, response: Response<DataVo>) {
                callback.successResponse(response)
            }

            override fun onFailure(call: Call<DataVo>, t: Throwable) {
                callback.errorResponse()
            }
        })
    }

    interface CallbackListener{
        fun successResponse(response:Response<DataVo>)
        fun errorResponse()
    }
}