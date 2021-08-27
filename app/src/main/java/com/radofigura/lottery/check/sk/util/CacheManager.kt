package com.radofigura.lottery.check.sk.util

import android.content.Context
import androidx.core.content.edit

object CacheManager {
    val SHARED_PREF_NAME = "vattery";

    fun addString(key: String, value: String, context: Context ) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String?, context: Context): String? {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue)
    }

    fun addBoolean(key: String, value: Boolean, context: Context ) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean, context: Context): Boolean? {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(key, defaultValue)
    }
}