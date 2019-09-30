package com.ironelder.mykotlindemo.presenter

import androidx.annotation.NonNull
import com.ironelder.mykotlindemo.common.SearchForKakaoRepo
import com.ironelder.mykotlindemo.dao.DataVo
import com.ironelder.mykotlindemo.dao.DocumentDataVo
import retrofit2.Response

class SearchPresenter:SearchContract.searchPresenterContract, SearchForKakaoRepo.CallbackListener {

    private val viewContract:SearchContract.searchViewContract
    private val searchForKakaoRepo:SearchForKakaoRepo

    constructor(@NonNull viewContract:SearchContract.searchViewContract, @NonNull searchForKakaoRepo:SearchForKakaoRepo){
        this.viewContract = viewContract
        this.searchForKakaoRepo = searchForKakaoRepo
    }

    override fun searchForKakao(type: String, page: Int, queryData: String) {
        searchForKakaoRepo.searchForKakao(type, page, queryData, this)
    }


    override fun successResponse(response: Response<DataVo>) {
        if(response.isSuccessful){
            var dataVo: DataVo? = response.body()
            viewContract.searchResults(dataVo?.documents?:ArrayList<DocumentDataVo>(), dataVo?.meta?.is_end?:true)
        } else {
            viewContract.searchError("${response.code()} - ${response.message()}")
        }
    }

    override fun errorResponse() {
        viewContract.searchError()
    }
}