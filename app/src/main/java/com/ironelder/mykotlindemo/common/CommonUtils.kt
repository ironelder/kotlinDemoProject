package com.ironelder.mykotlindemo.common

import android.text.format.DateUtils
import java.text.SimpleDateFormat


object CommonUtils {
    fun getCustomDateTime(dateTime:String):String{
        var resultDate = ""
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.parse(dateTime)
        val dateArray = dateFormat.format(date).split("-")

        resultDate = if(DateUtils.isToday(date.time)){
            "Today"
        } else if(DateUtils.isToday(date.time + DateUtils.DAY_IN_MILLIS)){
            "Yesterday"
        } else {
            "${dateArray[0]}년 ${dateArray[1]}월 ${dateArray[2]}일"
        }

        return resultDate
    }
}