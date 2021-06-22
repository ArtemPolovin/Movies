package com.example.movies.utils

import android.os.Bundle
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> Bundle.putKSerializable(key: String, value: T) {
    this.putString(key, Json.encodeToString(value))
}

inline fun <reified T> Bundle.getKSerializable(key: String): T? {
    return this.getString(key)?.let { Json.decodeFromString<T>(it) }
}