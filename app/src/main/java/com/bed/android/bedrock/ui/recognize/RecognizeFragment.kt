package com.bed.android.bedrock.ui.recognize

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentRecognizeBinding
import com.bed.android.bedrock.kakaovision.TextRecognizeUtil
import com.bed.android.bedrock.model.Store
import com.bed.android.bedrock.ui.BaseFragment
import com.bed.android.bedrock.util.BitmapUtil
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.min

class RecognizeFragment : BaseFragment<FragmentRecognizeBinding>(R.layout.fragment_recognize) {

    private lateinit var dataPath: String
    private val viewModel by viewModels<RecognizeViewModel>()
    private val url = "https://cdn.011st.com/11dims/resize/600x600/quality/75/11src/pd/v2/9/1/0/9/4/4/yWkGI/3468910944_B.jpg"
    private val requestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            Log.d(TAG, "onLoadFailed: failed to load")
            return true
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            resource?.let {
                binding.imageRecognize.setImageDrawable(it)
//                TextRecognizeUtil.getTextFromBitmapByRecognizer(it.toBitmap(), ::callback)
            }

            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataPath = context?.filesDir?.absolutePath + "/tesseract/"

        arguments?.apply {
            val store = getParcelable(STORE) as? Store ?: return@apply

            viewModel.loadImageFromUrl(store) {
                callback(store, it)
            }
        }
    }

    private fun loadImageFromUrl() {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = BitmapUtil.getBitmapFromUrl(url)

            bitmap?.let {
                // Kakao OCR
                val sendPart = TextRecognizeUtil.createFormDataFromBitmap(it)
//                val ocrResult = TextRecognizeUtil.getTextFromOCR(sendPart, ::callback)
//                Log.d(TAG, "loadImageFromUrl: $ocrResult")
            }
        }
    }


    private fun callback(store: Store, list: List<String>) {
        Log.d(TAG, list.toString())

//        val target = 1520160.0 // ??????????????? ?????? ??????????????? ??????
        val target = store.price.replace(",", "").toDouble()
        val target_short = (target / 10000).toInt() // ??? ????????? ?????????
        var p_val = target

        val t_len = target.toInt().toString().length
        val ts_len = target_short.toString().length

        // ???????????? ??????

        list.filter {
            it.contains("\\d[??????]\$".toRegex())
        }
        var filtered = mutableListOf<String>()
        filtered.addAll(list)

        filtered = filtered.map {
            it.replace("[^\\d]".toRegex(), "")
        }.filter {
            it.isNotBlank() // ?????? ?????????
            it.length in ts_len - 1..t_len
        }.toMutableList()
        Log.d(TAG, "price filtered : $filtered")

        if (filtered.isEmpty()) {
            // ???????????? ??????
            var p_filtered = list.filter {
                it.contains("%")
            }
            p_filtered = p_filtered.map {
                it.replace("[^\\d]".toRegex(), "") // ?????? ????????? ?????? ??? ?????????
            }
            p_filtered = p_filtered.filter {
                it.isNotBlank() // ?????? ?????????
                it.toInt() < 100
            }
            p_filtered.sortedBy { // ??????
                -it.toInt()
            }
            Log.d(TAG, "percentage filtered : $p_filtered")
            if (p_filtered.isNotEmpty()) {
                p_val *= ((100.0 - p_filtered[0].toDouble()) / 100) // ????????? ??????????????? ???????????? ???????????? ?????? ????????? ????????? ?????? ??????
            }
        }

        var t_val = target
        var ts_val = target_short

        val t_limit = t_val * 0.7 // ???????????? ?????? (30% ?????????)
        val ts_limit = ts_val * 0.7
        Log.d(TAG, "limits : $t_limit, $ts_limit")
        filtered.forEach {
            if (it.length in ts_len - 1..ts_len) {
                if (it.toInt() >= ts_limit) {
                    ts_val = min(ts_val, it.toInt())
                }
            } else if (it.length in t_len - 1..t_len) {
                if (it.toInt() >= t_limit) {
                    t_val = min(t_val, it.toDouble())
                }
            }
        }
        ts_val *= 10000

        // ????????? ???????????? ??????, ????????? ?????? ??????
        var min_val = min(ts_val, t_val.toInt())
        if (min_val * 0.95 >= p_val) { // ??????????????? ????????? ?????? or ?????? ?????? ???????????? ??????
            min_val = p_val.toInt()
        }

        Log.d(TAG, "target : $t_val // shortcut : $ts_val // percent : $p_val")
        CoroutineScope(Dispatchers.Main).launch {
            binding.textRecognize.text = list.joinToString(" ") + "\n***????????? ????????? : ${min_val}???***"
        }
    }

    companion object {

        private const val STORE = "STORE"
        private const val TAG = "RecognizeFragment"

        fun newInstance(store: Store) = RecognizeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(STORE, store)
            }
        }

    }
}
