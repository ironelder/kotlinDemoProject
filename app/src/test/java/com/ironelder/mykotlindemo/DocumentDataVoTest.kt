package com.ironelder.mykotlindemo


import com.ironelder.mykotlindemo.common.CARD_TYPE_CAFE
import com.ironelder.mykotlindemo.common.CommonUtils
import com.ironelder.mykotlindemo.common.VIEW_TYPE_CARD
import com.ironelder.mykotlindemo.common.VIEW_TYPE_DETAIL
import com.ironelder.mykotlindemo.dao.DocumentDataVo
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DocumentDataVoTest {
    private lateinit var mDocumentDataVo: DocumentDataVo

    @Before
    fun setUp(){
        mDocumentDataVo = DocumentDataVo("testTitle",
            "testContent",
            "url",
            "cafe",
            null,
            "thumbnail",
            "2019-07-02T11:28:54.000+09:00")
    }

    @Test
    fun typeTest(){
        assertEquals(CARD_TYPE_CAFE, mDocumentDataVo.getType())
    }

    @Test
    fun nameTest(){
        assertEquals("cafe", mDocumentDataVo.getName())
    }

    @Test
    fun resourceTest(){
        assertEquals(R.mipmap.cafe, mDocumentDataVo.getTypeImageResourceId())
    }


    @Test
    fun dateExceptionTest(){
        assertEquals("", CommonUtils.getCustomDateTime("sdf", VIEW_TYPE_DETAIL))
    }

    @Test
    fun dateTest(){
        //2019-09-14T10:23:19.000+09:00
        assertEquals("2019년 04월 17일", CommonUtils.getCustomDateTime("2019-04-17T09:36:37.000+09:00", VIEW_TYPE_CARD))
    }

}