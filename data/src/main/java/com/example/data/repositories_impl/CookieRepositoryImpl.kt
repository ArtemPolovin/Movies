package com.example.data.repositories_impl

import android.webkit.CookieManager
import com.example.domain.repositories.CookieRepository

class CookieRepositoryImpl: CookieRepository {

    override fun clearCookies() {
        CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
        }
    }
}