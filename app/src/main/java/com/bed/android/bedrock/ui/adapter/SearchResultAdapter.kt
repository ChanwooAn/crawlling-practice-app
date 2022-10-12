package com.bed.android.bedrock.ui.adapter

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.ListItemProductBinding
import com.bed.android.bedrock.model.Product

class SearchResultAdapter(
    diffUtil: DiffUtil.ItemCallback<Product>,
    private val onClick: (Product) -> Unit
) : ListAdapter<Product, SearchResultHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultHolder {
        val binding = ListItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SearchResultHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

}

class SearchResultHolder(private val binding: ListItemProductBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var isDescription = false

    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: Product, onClick: (Product) -> Unit) {
        binding.root.setOnClickListener { onClick(item) }
        binding.item = item
        binding.textProductName.text = item.name
        binding.detailBtn.apply {
            setOnClickListener {
                if (isDescription) {
                    binding.textProductName.setOnClickListener(null)
                    binding.textProductName.movementMethod = null
                    binding.textProductName.text = item.name
                } else {
                    binding.textProductName.setOnTouchListener { view, _ ->
                        view.parent.requestDisallowInterceptTouchEvent(true)
                        false
                    }
                    binding.textProductName.movementMethod = ScrollingMovementMethod()
                    binding.textProductName.text = item.des
                }
                isDescription = !isDescription
            }
        }
        binding.root.animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.item_create)
    }
}
