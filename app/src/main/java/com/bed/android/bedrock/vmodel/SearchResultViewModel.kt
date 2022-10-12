package com.bed.android.bedrock.vmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchResultViewModel() : ViewModel()
{

    var products: MutableLiveData<List<Product>> =MutableLiveData()


    var searchKeyword:MutableLiveData<String> = MutableLiveData()
        set(value){
            field=value
            searchKeyword.postValue(value.toString())

        }
    var searchCount: MutableLiveData<String> =MutableLiveData()
        set(value){
            field=value
            searchCount.postValue(value.toString())
        }


    fun initProducts(){
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("SearchViewModel",searchKeyword.value.toString())
            var prodList=croller.croll_list(searchKeyword.value.toString())
            GlobalScope.launch(Dispatchers.Main) {
                products.value=prodList
                products.postValue(prodList)
            }
        }
    }


}
