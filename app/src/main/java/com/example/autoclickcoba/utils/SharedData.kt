package com.example.alarmmanager.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedData(context : Context) {
    val reader : SharedPreferences
    var editor: SharedPreferences.Editor
    var dataString : String = ""

    init {
        reader = PreferenceManager.getDefaultSharedPreferences(context)
        editor = reader.edit()
    }


    fun getData(key: String?): String? {
        dataString = reader.getString(key, "")!!
        return dataString
    }

    fun getDataLong(key: String?): Long {
        var dataLong = reader.getLong(key, 0)
        return dataLong
    }

    fun removeData(key: String?) {
        editor.remove(key).commit()
    }

    fun setDataString(key: String?, data: String?) {
        editor.putString(key, data).commit()
    }

    fun setDataLong(key: String?, data: Long?) {
        if (data != null) {
            editor.putLong(key, data).commit()
        }
    }

    fun resetData() {
        editor.clear().commit()
    }
}