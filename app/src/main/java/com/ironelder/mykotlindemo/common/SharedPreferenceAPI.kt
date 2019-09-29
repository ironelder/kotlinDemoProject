package com.ironelder.mykotlindemo.common

import android.content.Context
import org.json.JSONException
import org.json.JSONArray



object SharedPreferenceAPI {

    fun setStringArrayPref(context: Context, key: String, values: ArrayList<String>) {
        val prefs = context.getSharedPreferences("KotlinDemo",Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until values.size) {
            a.put(values[i])
        }
        if (values.isNotEmpty()) {
            editor.putString(key, a.toString())
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    fun getStringArrayPref(context: Context, key: String): ArrayList<String> {
        val prefs =  context.getSharedPreferences("KotlinDemo",Context.MODE_PRIVATE)
        val json = prefs.getString(key, null)
        val searchArray = ArrayList<String>()
        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val history = a.optString(i)
                    searchArray.add(history)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        return searchArray
    }
}