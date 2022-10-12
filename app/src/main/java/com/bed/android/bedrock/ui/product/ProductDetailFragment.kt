package com.bed.android.bedrock.ui.product

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentProductDetailBinding
import com.bed.android.bedrock.model.Product
import com.bed.android.bedrock.ui.BaseFragment
import com.bed.android.bedrock.ui.adapter.CardStackAdapter
import com.bed.android.bedrock.ui.search.result.TabDescription
import com.bed.android.bedrock.ui.search.result.TabPriceList
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>(R.layout.fragment_product_detail) {

    private val viewModel by activityViewModels<ProductDetailViewModel>()

    private lateinit var adapter:CardStackAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        arguments?.let {
            val product = it.getParcelable("product") as? Product ?: return@let

            lifecycleScope.launch {
                binding.sflSample.startShimmer()
                viewModel.startCrawl(product, ::onCrawlingFinished)
            }
        }
    }

    override fun onDestroyView() {
        viewModel.onDestroyView()
        super.onDestroyView()
    }

    private fun onCrawlingFinished(product: Product) {
        Log.d(TAG, "onCrawlingFinished: $product")

        val pagerAdapter = PagerAdapter(requireActivity())
        pagerAdapter.addFragment(TabDescription.newInstance(product))
        pagerAdapter.addFragment(TabPriceList.newInstance(product))
        adapter= CardStackAdapter(product.img)
        bind {
            stackView.adapter=adapter
            textTitle.isVisible = true
            stackView.isVisible=true

            sflSample.apply {
                stopShimmer()
                visibility = View.GONE
            }
            tabDetail.isVisible = true

            with(viewpager) {
                adapter = pagerAdapter
                TabLayoutMediator(tabDetail, this, true, true, pagerAdapter).attach()
            }
        }
    }


    private inner class PagerAdapter(private val fa: FragmentActivity)
        : FragmentStateAdapter(fa), TabLayoutMediator.TabConfigurationStrategy {

        var fragments: ArrayList<Fragment> = ArrayList()

        private val titles = listOf(
            getString(R.string.detail_info),
            getString(R.string.price_list)
        )

        override fun createFragment(position: Int): Fragment {

            return fragments[position]

        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            tab.text = titles[position]
        }

        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
            notifyItemInserted(fragments.size - 1)
        }

        fun removeFragment() {
            fragments.removeLast()
            notifyItemRemoved(fragments.size)
        }
    }


    companion object {

        private const val TAG = "ProductDetailFragment"

        fun newInstance(product: Product): ProductDetailFragment {
            return ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("product", product)
                }
            }
        }
    }
}