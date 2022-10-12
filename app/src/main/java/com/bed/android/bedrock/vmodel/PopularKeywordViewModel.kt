package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PopularKeywordViewModel:ViewModel() {
    lateinit var keywords:List<String>
    lateinit var k_idx:MutableLiveData<Int>
    lateinit var keyword:MutableLiveData<String>
}