package com.bed.android.bedrock.ui.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.databinding.FragmentGuideFirstBinding
import com.bed.android.bedrock.databinding.FragmentGuideFourthBinding
import com.bed.android.bedrock.databinding.FragmentGuideSecondBinding
import com.bed.android.bedrock.databinding.FragmentGuideThirdBinding

class GuideFragment(private val position: Int) : Fragment() {

    private var binding: ViewDataBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return when (position) {
            0 -> {
                FragmentGuideFirstBinding.inflate(inflater, container, false).let {
                    binding = it
                    it.root
                }
            }
            1 -> {
                FragmentGuideSecondBinding.inflate(inflater, container, false).let {
                    binding = it
                    it.root
                }
            }
            2 -> {
                FragmentGuideThirdBinding.inflate(inflater, container, false).let {
                    binding = it
                    it.root
                }
            }
            else -> {
                FragmentGuideFourthBinding.inflate(inflater, container, false).let {
                    binding = it
                    it.root
                }
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.apply {
//            when (this) {
//                is FragmentGuideFirstBinding -> {
//                    textJump.setOnClickListener {
//                        (requireActivity() as GuideActivity).goToMainActivity()
//                    }
//
//                    textNext.setOnClickListener {
//                        (requireActivity() as GuideActivity).goNext(position)
//                    }
//                }
//                is FragmentGuideSecondBinding -> {
//                    textJump.setOnClickListener {
//                        (requireActivity() as GuideActivity).goToMainActivity()
//                    }
//
//                    textNext.setOnClickListener {
//                        (requireActivity() as GuideActivity).goNext(position)
//                    }
//                }
//                is FragmentGuideThirdBinding -> {
//                    textJump.setOnClickListener {
//                        (requireActivity() as GuideActivity).goToMainActivity()
//                    }
//
//                    textNext.setOnClickListener {
//                        (requireActivity() as GuideActivity).goNext(position)
//                    }
//                }
//                is FragmentGuideFourthBinding -> {
//                    textNext.setOnClickListener {
//                        (requireActivity() as GuideActivity).goToMainActivity()
//                    }
//                }
//                else -> Unit
//            }
//        }
//    }

    companion object {

        fun newInstance(id: Int) = GuideFragment(id)
    }
}
