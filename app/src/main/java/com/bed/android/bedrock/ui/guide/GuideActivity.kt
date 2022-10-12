package com.bed.android.bedrock.ui.guide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.*
import com.bed.android.bedrock.ui.MainActivity
import kotlin.reflect.KClass

class GuideActivity : AppCompatActivity() {

    private var _binding: ActivityGuideBinding? = null
    private val binding: ActivityGuideBinding get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_guide)
        _binding?.lifecycleOwner = this

        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.wormDotsIndicator.attachTo(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 3) {
                    binding.textJump.isVisible = false
                    binding.textNext.apply {
                        isVisible = true
                        setOnClickListener { goToMainActivity() }
                    }
                    binding.arrow.isVisible = false
                } else {
                    binding.textJump.apply {
                        isVisible = true
                        setOnClickListener { goToMainActivity() }
                    }

                    binding.textNext.isVisible = false
                    binding.arrow.apply {
                        setOnClickListener { goNext(position) }
                        isVisible = true
                    }
                }
            }
        })
    }

    fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun goNext(position: Int) {
        binding.viewPager.setCurrentItem(position + 1, true)
    }

    private class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun createFragment(position: Int): Fragment {
            return GuideFragment(position)
        }

        override fun getItemCount() = 4
    }

}
