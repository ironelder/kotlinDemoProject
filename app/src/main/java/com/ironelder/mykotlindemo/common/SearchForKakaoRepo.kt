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

    interface CallbackListener{
        fun successResponse(response:Response<DataVo>)
        fun errorResponse()
    }
}