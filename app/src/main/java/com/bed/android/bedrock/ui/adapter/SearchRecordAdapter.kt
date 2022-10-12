package com.bed.android.bedrock.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.databinding.ListItemSearchrecordBinding
import com.bed.android.bedrock.model.SearchRecord

class SearchRecordAdapter(
    diffCallback: DiffUtil.ItemCallback<SearchRecord>,
    private val onClick: ((SearchRecord) -> Unit)
) : ListAdapter<SearchRecord, SearchRecordHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecordHolder {
        val binding = ListItemSearchrecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SearchRecordHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchRecordHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}

class SearchRecordHolder(
    private val binding: ListItemSearchrecordBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(searchRecord: SearchRecord, onClick: ((SearchRecord) -> Unit)) {
        binding.root.setOnClickListener { onClick.invoke(searchRecord) }
        binding.item = searchRecord
    }
}
