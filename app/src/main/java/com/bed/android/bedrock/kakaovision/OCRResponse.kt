package com.bed.android.bedrock.kakaovision

import com.google.gson.annotations.SerializedName

data class OCRResponse(
    @SerializedName("result")
    val result: List<OCRResult?>
)
