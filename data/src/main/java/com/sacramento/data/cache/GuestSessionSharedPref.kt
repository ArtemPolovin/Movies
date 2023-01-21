package com.sacramento.data.cache

import android.content.SharedPreferences
import com.sacramento.data.utils.GUEST_SESSION_ID_KEY

class GuestSessionSharedPref(private val sharPref: SharedPreferences) {

    fun saveGuestSessionId(guestSessionId: String) {
        sharPref.edit().putString(GUEST_SESSION_ID_KEY, guestSessionId).apply()
    }

    fun loadGuestSessionId() =
        sharPref.getString(GUEST_SESSION_ID_KEY, "")

    fun removeGuestSessionId() {
        sharPref.edit().remove(GUEST_SESSION_ID_KEY).apply()
    }
}