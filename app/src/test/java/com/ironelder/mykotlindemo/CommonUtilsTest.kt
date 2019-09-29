package com.ironelder.mykotlindemo

import com.ironelder.mykotlindemo.common.CommonUtils
import com.ironelder.mykotlindemo.common.VIEW_TYPE_CARD
import com.ironelder.mykotlindemo.common.VIEW_TYPE_DETAIL
import org.junit.Test

import org.junit.Assert.*

class CommonUtilsTest {
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