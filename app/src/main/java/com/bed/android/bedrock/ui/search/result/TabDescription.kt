package com.bed.android.bedrock.ui.search.result

import android.os.Bundle
import android.view.View
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentDetailtabDescBinding
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.BaseFragment

class TabDescription : BaseFragment<FragmentDetailtabDescBinding>(R.layout.fragment_detailtab_desc) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val product = it.getParcelable("product") as? Product ?: return@let
            binding.textDesc.text = product.des
        }
    }


    companion object {

        fun newInstance(product: Product): TabDescription {
            return TabDescription().apply {
                arguments = Bundle().apply {
                    putParcelable("product", product)
                }
            }
        }
    }
}