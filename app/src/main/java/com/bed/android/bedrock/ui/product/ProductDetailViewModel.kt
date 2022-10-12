package com.bed.android.bedrock.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.MainActivity.Companion.croller
import kotlinx.coroutines.*

class ProductDetailViewModel : ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    private var crawlingJob: Job? = null

    suspend fun startCrawl(product: Product, callback: (Product) -> Unit) {
        crawlingJob = CoroutineScope(Dispatchers.IO).launch {
            val result = croller.croll_detail(product)
            
            withContext(Dispatchers.Main) {
                if (isActive)
                    callback(result)
            }

            _product.postValue(result)
        }
    }

    fun onDestroyView() {
        crawlingJob?.cancel()
    }

    override fun onCleared() {
        crawlingJob?.cancel()
        super.onCleared()
    }
}