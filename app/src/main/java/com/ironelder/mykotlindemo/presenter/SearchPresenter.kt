package com.ironelder.mykotlindemo.presenter

import androidx.annotation.NonNull
import com.ironelder.mykotlindemo.common.SearchForKakaoRepo

class SearchPresenter:SearchContract.searchPresenterContract {
    private val viewContract:SearchContract.searchViewContract
    private val searchForKakaoRepo:SearchForKakaoRepo

    constructor(@NonNull viewContract:SearchContract.searchViewContract, @NonNull searchForKakaoRepo:SearchForKakaoRepo){
        this.viewContract = viewContract
        this.searchForKakaoRepo = searchForKakaoRepo
    }

    override fun searchForKakao(type: String, page: Int, queryData: String) {
    }
}