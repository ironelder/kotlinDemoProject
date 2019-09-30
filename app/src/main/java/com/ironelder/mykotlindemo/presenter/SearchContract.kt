package com.ironelder.mykotlindemo.presenter

import com.ironelder.mykotlindemo.dao.DocumentDataVo

open interface SearchContract {
    interface searchViewContract{
        fun searchResults(searchResult:List<DocumentDataVo>, isEnd:Boolean, type:String)
        fun searchError()
        fun searchError(errMsg:String)
    }

    interface searchPresenterContract{
        fun searchForKakao(type:String, page:Int, queryData:String)
    }
}