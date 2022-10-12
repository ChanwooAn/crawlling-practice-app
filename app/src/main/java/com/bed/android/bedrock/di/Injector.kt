package com.bed.android.bedrock.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bed.android.bedrock.R

object Injector {

    lateinit var application: Application
    private var sharedPref: SharedPreferences? = null

    fun getSharedPreferences(): SharedPreferences {
        return sharedPref ?: application.getSharedPreferences(
            application.getString(R.string.keyword_info), Context.MODE_PRIVATE
        ).apply {
            sharedPref = this
        }
    }
}