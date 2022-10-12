package com.bed.android.bedrock.kakaovision

import com.google.gson.annotations.SerializedName

data class OCRResult(
//    val boxes: Array<Array<Number>>,

    @SerializedName("recognition_words")
    val words: Array<String>
)
