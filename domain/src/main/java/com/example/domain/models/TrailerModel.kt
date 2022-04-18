package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class TrailerModel(
    val thumbnailUrl: String?,
    val videoKey:String?,
    val id:String
)