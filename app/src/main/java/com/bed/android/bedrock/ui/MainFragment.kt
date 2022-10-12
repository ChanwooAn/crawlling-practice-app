package com.bed.android.bedrock.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentMainBinding
import com.bed.android.bedrock.ui.search.SearchBarFragment

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind {
            ObjectAnimator.ofFloat(searchBarView, "translationY", -1000f, -((root.height - searchBarView.height) / 2 - TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8f, requireContext().resources.displayMetrics))).apply {
                interpolator = DecelerateInterpolator()
                duration = 350
                start()
            }

            ObjectAnimator.ofFloat(titleText, "translationY", -1000f, 0f).apply {
                duration = 350
                interpolator = DecelerateInterpolator()
                start()
            }

            // 검색창 눌러서 프래그먼트 전환 되기전에 애니메이션
            searchBarView.setOnClickListener {
                searchBarView.text = "" // 안내 텍스트 지우기
                // 검색창을 위한 Animation
                ObjectAnimator.ofFloat(searchBarView, "translationY", -((root.height - searchBarView.height) / 2 - TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8f, requireContext().resources.displayMetrics))).apply {
                    duration = 350
                    interpolator = DecelerateInterpolator()
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fragment_container, SearchBarFragment.newInstance())
                                addToBackStack("MainFragment")
                                commit()
                            }
                        }
                    })
                    start()
                }

                // 타이틀 글자 위로 올려보내기
                ObjectAnimator.ofFloat(titleText, "translationY", -1000f).apply {
                    duration = 350
                    interpolator = DecelerateInterpolator()
                    start()
                }
            }
        }
    }


    companion object {

        fun newInstance() = MainFragment()

    }
}