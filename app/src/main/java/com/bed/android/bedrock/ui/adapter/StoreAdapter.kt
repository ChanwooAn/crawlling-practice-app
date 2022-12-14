package com.bed.android.bedrock.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.databinding.ListItemPriceBinding
import com.bed.android.bedrock.kakaovision.TextRecognizeUtil
import com.bed.android.bedrock.model.Store
import com.bed.android.bedrock.ui.recognize.RecognizeFragment
import com.bed.android.bedrock.util.BitmapUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.min

class StoreAdapter(
    diffCallback: DiffUtil.ItemCallback<Store>,
    private val buttonClick: (Store) -> Unit,
    private val onClick: (Store) -> Unit
) : ListAdapter<Store, StoreViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ListItemPriceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(getItem(position), buttonClick, onClick)
    }
}

class StoreViewHolder(private val binding: ListItemPriceBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Store, buttonClick: (Store) -> Unit, onClick: (Store) -> Unit) {
        binding.root.setOnClickListener { onClick(item) }
        binding.item = item
        binding.bedrockPrice.isVisible = false
        binding.buttonOcr.apply {
            startShimmer()
            isVisible = true
        }
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = BitmapUtil.getBitmapFromUrl(item.productImg.replace("https:https:", "https:"))
                ?:BitmapUtil.getBitmapFromUrl(item.productImg.replace("https:http","http"))


            bitmap?.let {
                val sendPart = TextRecognizeUtil.createFormDataFromBitmap(it)
                val ocrResult = TextRecognizeUtil.getTextFromOCR(sendPart) { list ->
                    callback(item, list)
                }
            }
        }

//        binding.buttonOcr.setOnClickListener { buttonClick(item) }
    }


    @SuppressLint("SetTextI18n")
    private fun callback(store: Store, list: List<String>) {
        var storeTitle=store.title.split(' ')
        Log.d("PriceInTitle",store.storeName+":"+storeTitle.toString())

        var priceInTitle:String?=""
        storeTitle=storeTitle.filter{it.contains("???")}



        if(!storeTitle.isEmpty()){
            for(e in storeTitle[0]){
                if(e=='???'){
                    break
                }
                if('0'<=e && e<='9'){
                    priceInTitle+=e
                }
            }


            CoroutineScope(Dispatchers.Main).launch {
//            binding.textRecognize.text = list.joinToString(" ") + "\n***????????? ????????? : ${min_val}???***"
                binding.buttonOcr.apply {
                    stopShimmer()
                    visibility = View.INVISIBLE
                }
                binding.bedrockPrice.apply {
                    isVisible = true

                    text = "??? ${priceInTitle}??????"
                }
            }

            Log.d("PriceInTitle","priceInTitle"+priceInTitle.toString())
        }
        else{
            val target = store.price.replace(",", "").toDouble()
            val target_short = (target / 10000).toInt() // ??? ????????? ?????????
            var p_val = target

            val t_len = target.toInt().toString().length
            val ts_len = target_short.toString().length

            // ???????????? ??????

            val a = list.filter {
                val trimmed = it.trim()
                trimmed.contains("\\d[??????]+\$".toRegex()) || trimmed.contains("\\d")
            }

            var filtered = mutableListOf<String>()
            filtered.addAll(a)

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
                Log.d(TAG, "callback: $p_filtered")
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
//            Log.d(RecognizeFragment.TAG, "percentage filtered : $p_filtered")
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
            var min_val = minOf(ts_val, t_val.toInt(), p_val.toInt())
            if (min_val * 0.95 >= p_val) { // ??????????????? ????????? ?????? or ?????? ?????? ???????????? ??????
                min_val = p_val.toInt()
            }
            Log.d(TAG, "target : $t_val // shortcut : $ts_val // percent : $p_val")

            CoroutineScope(Dispatchers.Main).launch {
//            binding.textRecognize.text = list.joinToString(" ") + "\n***????????? ????????? : ${min_val}???***"
                binding.buttonOcr.apply {
                    stopShimmer()
                    visibility = View.INVISIBLE
                }
                binding.bedrockPrice.apply {
                    isVisible = true

                    text = "??? ${min_val / 10000}??????"
                }
            }
        }





    }

    companion object {
        private const val TAG = "StoreAdapter"
    }
}