package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import com.bed.android.bedrock.model.SearchRecord

class SearchRecordViewModel {
    val keyword:MutableLiveData<String> = MutableLiveData()
    val searchDate:MutableLiveData<String> = MutableLiveData()
    var searchRecord:SearchRecord? = null
        set(value) {
            field = value
            keyword.postValue(value?.productName)
            searchDate.postValue(value?.searchDate)
        }
}