package com.sacramento.data.repositories_impl

import android.webkit.CookieManager
import com.sacramento.domain.repositories.CookieRepository

class CookieRepositoryImpl: CookieRepository {

    override fun clearCookies() {
        CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
        }
    }
}