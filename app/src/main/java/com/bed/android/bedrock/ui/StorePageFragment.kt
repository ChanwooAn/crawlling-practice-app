package com.bed.android.bedrock.ui

import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bed.android.bedrock.R
import com.bed.android.bedrock.databinding.FragmentWebviewBinding

private const val url="Store_page_url"


class StorePageFragment : Fragment(){
    private lateinit var storeUrl:Uri
    private lateinit var binding_webview: FragmentWebviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeUrl=arguments?.getParcelable(url)?:Uri.EMPTY

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_webview =
            DataBindingUtil.inflate(inflater, R.layout.fragment_webview,container,false)

        return binding_webview.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val web_view=binding_webview.webView
        web_view.settings.apply{
            javaScriptEnabled=true
            useWideViewPort=true
            loadWithOverviewMode=true
            builtInZoomControls=true
            supportZoom()
        }
        web_view.webViewClient= object:WebViewClient(){



            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding_webview.progressHorizontal.visibility=View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding_webview.progressHorizontal.visibility=View.INVISIBLE
            }
        }
        web_view.webChromeClient= object:WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding_webview.progressHorizontal.progress=newProgress
            }
        }
        web_view.loadUrl(storeUrl.toString())
    }

    companion object{
        fun newInstance(storeUrl:String):StorePageFragment{
            return StorePageFragment().apply{
                arguments=Bundle().apply{
                    putParcelable(url, Uri.parse(storeUrl))
                }
            }
        }
    }
}