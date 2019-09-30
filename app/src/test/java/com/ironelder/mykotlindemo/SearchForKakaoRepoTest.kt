package com.ironelder.mykotlindemo

import com.ironelder.mykotlindemo.common.SearchForKakaoApi
import com.ironelder.mykotlindemo.common.SearchForKakaoRepo
import com.ironelder.mykotlindemo.dao.DataVo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchForKakaoRepoTest {
    private lateinit var mSearchForKakaoRepo:SearchForKakaoRepo

    @Mock
    private lateinit var mSearchForKakaoApi: SearchForKakaoApi

    @Before
    @Throws(Exception::class)
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        mSearchForKakaoRepo = Mockito.spy(SearchForKakaoRepo(mSearchForKakaoApi))
    }

    @SuppressWarnings("unchecked")
    @Test
    fun searchRepos(){
        var searchForKakaoRepoCallback:SearchForKakaoRepo.CallbackListener =
            object : SearchForKakaoRepo.CallbackListener{
                override fun errorResponse() {
                }

                override fun successResponse(response: Response<DataVo>, type: String) {
                }
            }


        var call = Mockito.mock(Call::class.java)
        val searchQuery = "android"
        Mockito.doReturn(call).`when`(mSearchForKakaoApi).requestSearchForKakao("cafe", 1, 25, searchQuery)

        mSearchForKakaoRepo.searchForKakao("cafe", 1, searchQuery, searchForKakaoRepoCallback)
        Mockito.verify(mSearchForKakaoApi, Mockito.times(1)).
            requestSearchForKakao("cafe",1, 25, searchQuery)
    }
}