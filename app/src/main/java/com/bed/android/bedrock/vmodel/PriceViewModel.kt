package com.bed.android.bedrock.vmodel

import androidx.lifecycle.MutableLiveData
import com.bed.android.bedrock.model.Product

class PriceViewModel {
    // private val _img = MutableLiveData<String>()
    // val img: LiveData<String>
    //  get() = _img

    var imgUrl:MutableLiveData<String> = MutableLiveData()
        set(value){
            field=value
            imgUrl.postValue(value.toString())
        }
    var price: MutableLiveData<String> =MutableLiveData()
        set(value){
            field=value
            price.postValue(value.toString())
        }
    var storeUrl: String=""
        set(value){
            field=value
        }




}