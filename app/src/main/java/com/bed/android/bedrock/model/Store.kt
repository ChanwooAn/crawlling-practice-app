package com.bed.android.bedrock.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Store(
    val storeName:String,
    val thumbnail:String,
    val title:String,
    val price:String,
    val storeLink:String,
    var productImg:String
) : Parcelable