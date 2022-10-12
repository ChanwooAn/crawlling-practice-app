package com.bed.android.bedrock

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.model.SearchRecord
import com.bed.android.bedrock.model.Store
import com.bed.android.bedrock.module.GlideApp
import com.bed.android.bedrock.ui.adapter.SearchRecordAdapter
import com.bed.android.bedrock.ui.adapter.SearchResultAdapter
import com.bed.android.bedrock.ui.adapter.StoreAdapter

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    if (url != null) {
        GlideApp.with(imageView.context).load(url).into(imageView)
    }
}

@BindingAdapter("setRecordItems")
fun setRecordItems(recyclerView: RecyclerView?, list: List<SearchRecord>) {
    (recyclerView?.adapter as? SearchRecordAdapter)?.submitList(list)
}

@BindingAdapter("setSearchResultItems")
fun setSearchResultItems(recyclerView: RecyclerView?, list: List<Product>) {
    (recyclerView?.adapter as? SearchResultAdapter)?.submitList(list)
}

@BindingAdapter("setStoreItems")
fun setStoreItems(recyclerView: RecyclerView?, list: List<Store>?) {
    Log.d("asdf", "setStoreItems: $list")
    (recyclerView?.adapter as? StoreAdapter)?.submitList(list)
}
