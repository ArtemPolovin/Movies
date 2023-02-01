package com.sacramento.movies.ui.authorization

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentAuthorizationBinding
import com.sacramento.movies.utils.AUTH_URI
import com.sacramento.movies.utils.LOGOUT_URL_IN_WEB_VIEW
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        openWebPageForAuthorization(savedInstanceState)
    }

    private val mWebViewClient: WebViewClient by lazy {
        object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()

                if (url == LOGOUT_URL_IN_WEB_VIEW) {
                    viewModel.logout()
                    viewModel.getRequestToken()
                    return true
                }

                view?.loadUrl(url)

                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (viewModel.isAuthorizationApproved(url?:"")) {
                    viewModel.saveSessionId()
                    viewModel.isSessionIdSaved.observe(viewLifecycleOwner){isSessionIdSaved ->
                        if(isSessionIdSaved) findNavController().navigate(R.id.action_authorizationFragment_to_homeFragment)
                    }
                }
                binding.progressBarWebView.visibility = View.VISIBLE
                binding.textProgress.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressBarWebView.visibility = View.GONE
                binding.textProgress.visibility = View.GONE
                super.onPageFinished(view, url)
            }
        }
    }

private val mWebChromeClient: WebChromeClient by lazy {
    object : WebChromeClient() {
        @SuppressLint("SetTextI18n")
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            binding.progressBarWebView.progress = newProgress
            binding.textProgress.text =
                newProgress.toString() + resources.getString(R.string.char_percent)
            super.onProgressChanged(view, newProgress)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun openWebPageForAuthorization(savedInstanceState: Bundle?) {
    viewModel.requestToken.observe(viewLifecycleOwner) { requestToken ->
        binding.webView.apply {
            webViewClient = mWebViewClient
            webChromeClient = mWebChromeClient
            settings.javaScriptEnabled = true
            if (savedInstanceState == null) {
                binding.webView.loadUrl("$AUTH_URI$requestToken")
            }
        }
    }
}

override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    binding.webView.saveState(outState)
}

override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    savedInstanceState?.let { binding.webView.restoreState(it) }
}

}