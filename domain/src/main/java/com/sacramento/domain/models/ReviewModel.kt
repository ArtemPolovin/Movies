package com.sacramento.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ReviewModel(
    val id: String,
    val authorName: String,
    val userName: String,
    val avatarPath: String?,
    val rating: String,
    val content: String,
    val createdAt: String
)