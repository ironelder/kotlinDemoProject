package com.ironelder.mykotlindemo


import com.ironelder.mykotlindemo.common.CARD_TYPE_CAFE
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

}