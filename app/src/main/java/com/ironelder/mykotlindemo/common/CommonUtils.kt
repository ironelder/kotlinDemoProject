package com.ironelder.mykotlindemo.common

import android.text.format.DateUtils
import java.text.SimpleDateFormat


object CommonUtils {
    fun getCustomDateTime(dateTime:String, viewType:Int):String{
        var resultDate = ""
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date = dateFormat.parse(dateTime)

        resultDate = when {
            viewType == VIEW_TYPE_DETAIL -> SimpleDateFormat("yyyy년 MM월dd일 aa hh시mm분").format(date).replace("AM","오전").replace("PM","오후")
            DateUtils.isToday(date.time) -> "Today"
            DateUtils.isToday(date.time + DateUtils.DAY_IN_MILLIS) -> "Yesterday"
            else -> SimpleDateFormat("yyyy년 MM월 dd일").format(date)
        }

        return resultDate
    }
}