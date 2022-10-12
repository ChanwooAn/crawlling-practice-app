package com.bed.android.bedrock.ui.search.result

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentDetailtabPricelistBinding
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.model.Store
import com.bed.android.bedrock.ui.BaseFragment
import com.bed.android.bedrock.ui.adapter.StoreAdapter
import com.bed.android.bedrock.ui.product.ProductDetailViewModel

class TabPriceList : BaseFragment<FragmentDetailtabPricelistBinding>(R.layout.fragment_detailtab_pricelist) {

    private val viewModel by activityViewModels<ProductDetailViewModel>()
    private var callbacks: Callbacks? = null
    private val adapter = StoreAdapter(diffUtil, ::buttonClick) { store ->
        callbacks?.onPriceSelected(store.storeLink)
    }

    interface Callbacks {
        fun onPriceSelected(storeUrl: String)
        fun goToOCR(store: Store)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Callbacks) {
            callbacks = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewPrice.adapter = adapter

        binding.viewModel = viewModel

    }

    private fun buttonClick(item: Store) {
        callbacks?.goToOCR(item)
    }


    companion object {

        private const val TAG = "TabPriceList"

        fun newInstance(product: Product): TabPriceList {
            return TabPriceList().apply {
                arguments = Bundle().apply {
                    putParcelable("product", product)
                }
            }
        }

        private val diffUtil = object : DiffUtil.ItemCallback<Store>() {

            override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Store, newItem: Store) =
                oldItem.thumbnail == newItem.thumbnail
        }
    }
}