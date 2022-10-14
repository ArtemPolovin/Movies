package com.sacramento.data.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieFilterParams(
    val movieCategory: String? = null,
    val genreId: String? = null,
    val releaseYear : String? = null,
    val sortByPopularity: String? = null,
    val rating: Int? = null
): Parcelable