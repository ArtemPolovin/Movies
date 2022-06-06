package com.example.movies.ui.authorization

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.data.cache.RequestTokenDataCache
import com.example.movies.R
import com.example.movies.databinding.FragmentAuthorizationBinding
import com.example.movies.utils.AUTH_URI
import com.example.movies.utils.LOGOUT_URL_IN_WEB_VIEW
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding: FragmentAuthorizationBinding
        get() =
            _binding ?: throw RuntimeException("FragmentAuthorizationBinding == null")

    private val viewModel: AuthorizationViewModel by viewModels()

    @Inject
    lateinit var requestTokenDataCache: RequestTokenDataCache

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
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
                    viewModel.clearCookies()
                    viewModel.deleteTokenFromCache()
                    viewModel.getRequestToken()
                    return true
                }

                view?.loadUrl(url)

                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (viewModel.isAuthorizationApproved(url!!)) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val isAccessIdSaved =
                            withContext(Dispatchers.Default) { viewModel.saveSessionId() }
                        if (isAccessIdSaved) {
                            findNavController().popBackStack(R.id.authorizationFragment, true)
                            findNavController().navigate(R.id.homeFragment)
                        }
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