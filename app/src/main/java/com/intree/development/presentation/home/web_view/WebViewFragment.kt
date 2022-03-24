package com.intree.development.presentation.home.web_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.intree.development.R
import com.intree.development.databinding.WebviewFragmentBinding


class WebViewFragment : Fragment() {
    private var _binding: WebviewFragmentBinding? = null

    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.webview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = WebviewFragmentBinding.bind(view)

        _binding?.toolbar?.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        _binding?.toolbar?.setNavigationOnClickListener { activity?.onBackPressed() }

        if (_binding != null) {
            val mWebView : WebView = _binding!!.webView
            mWebView.loadUrl(args.url)

            val webSettings: WebSettings = mWebView.settings
            //for test
            webSettings.javaScriptEnabled = true
            mWebView.webViewClient = WebViewClient()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}