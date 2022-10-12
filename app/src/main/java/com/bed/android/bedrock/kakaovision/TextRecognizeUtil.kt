package com.bed.android.bedrock.kakaovision

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

object TextRecognizeUtil {

    private const val TAG = "TextRecognizeUtil"
    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    // API 서버에 POST할 데이터
    fun createFormDataFromBitmap(bitmap: Bitmap):MultipartBody.Part{
        return MultipartBody.Part.createFormData(
            "image",
            "photo",
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                bitmapToByteArray(bitmap)
            )
        )
    }

    // Response body에서 단어 추출하기
    fun getTextFromOCR(sendPart:MultipartBody.Part, callback: (List<String>) -> Unit){
        val words = mutableListOf<String>()
        val ocrCall = RetrofitInstance.kakaoApi.getOCR(sendPart)
        ocrCall.enqueue(object : Callback<OCRResponse?> {
            override fun onResponse(
                call: Call<OCRResponse?>,
                response: Response<OCRResponse?>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.result

                    result?.let {
                        result.forEach {
                            words.addAll(it!!.words)
                        }
                        Log.d(TAG, "Success, words => ${words}")
                        callback(words)
                    }
                }
                else{
                    Log.d(TAG, "Failed : $response")
                }
            }

            override fun onFailure(call: Call<OCRResponse?>, t: Throwable) {
                Log.d(TAG, "Failure : ${t.message}")
            }
        })
    }

    private fun bitmapToByteArray(bitmap:Bitmap):ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


    fun getTextFromBitmapByRecognizer(bitmap: Bitmap, callback: (List<String>) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image).addOnSuccessListener { result ->
            val list = mutableListOf<String>()
            result.textBlocks.forEach { block ->
                list.add(block.text)
            }

            callback(list)
        }.addOnFailureListener {
            callback(listOf(it.toString()))
        }
    }
}