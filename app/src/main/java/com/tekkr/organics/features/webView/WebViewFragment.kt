package com.tekkr.organics.features.webView

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.common.toast
import com.tekkr.organics.databinding.FragmentWebViewBinding
import java.net.URLEncoder

class WebViewFragment : BaseAbstractFragment<WebViewViewModel, FragmentWebViewBinding>(R.layout.fragment_web_view) {

    override fun setViewModel(): WebViewViewModel =
            ViewModelProvider(this@WebViewFragment, ViewModelFactory {
                WebViewViewModel(requireActivity().application)
            }).get(WebViewViewModel::class.java)

    override fun setupViews(): FragmentWebViewBinding.() -> Unit = {

        setWebview()

    }

    private fun setWebview() {

        mBinding.webView.settings.javaScriptEnabled = true
        mBinding.webView.settings.builtInZoomControls = true
        mBinding.webView.settings.setSupportZoom(true)
        mBinding.webView.settings.loadWithOverviewMode = true
        mBinding.webView.settings.useWideViewPort = true
        mBinding.webView.settings.displayZoomControls = false

        mBinding.webView.loadUrl("http://tekkrorganics.in/")

    }

    override fun setupObservers(): WebViewViewModel.() -> Unit = {

    }

}
