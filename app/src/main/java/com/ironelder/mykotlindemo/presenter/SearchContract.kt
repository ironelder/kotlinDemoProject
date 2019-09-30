package com.ironelder.mykotlindemo.presenter

import com.ironelder.mykotlindemo.dao.DocumentDataVo

interface SearchContract {
    interface searchViewContract{
        fun searchResults(searchResult:List<DocumentDataVo>, isEnd:Boolean)
        fun searchError()
        fun searchError(errMsg:String)
    }

    interface searchPresenterContract{
        fun searchForKakao(type:String, page:Int, queryData:String)
    }
}