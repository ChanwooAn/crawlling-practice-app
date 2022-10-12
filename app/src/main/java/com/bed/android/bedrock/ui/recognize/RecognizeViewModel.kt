package com.bed.android.bedrock.ui.recognize

import androidx.lifecycle.ViewModel
import com.bed.android.bedrock.kakaovision.TextRecognizeUtil
import com.bed.android.bedrock.model.Store
import com.bed.android.bedrock.util.BitmapUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecognizeViewModel : ViewModel() {

    fun loadImageFromUrl(store: Store, onResult: (List<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = BitmapUtil.getBitmapFromUrl(store.productImg.replace("https:https:", "https:"))

            bitmap?.let {
                val sendPart = TextRecognizeUtil.createFormDataFromBitmap(it)
                val ocrResult = TextRecognizeUtil.getTextFromOCR(sendPart) { list ->
                    onResult(list)
//                    callback(store, list) {
//                        onResult()
//                    }
                }
            }
        }
    }

//    private fun callback(store: Store, list: List<String>, onResult: () -> Unit) {
//        val price = store.price.replace(",", "").toDouble()
//        val shortPrice = (price / 10000).toInt()
//
//        val filtered = list.filter {
//            it.contains("\\d[만원]\$".toRegex())
//        }.map {
//            it.replace("[^\\d]".toRegex(), "").trim()
//        }.filterNot {
//            it.isBlank()
//        }.filter {
//            it.length in shortPrice.toString().length - 1..price.toInt().toString().length
//        }
//
//        if (filtered.isEmpty()) {
//
//        }
//
//    }

}