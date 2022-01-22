package com.example.data.utils

enum class MovieCategories(val categoryName: String, val genreId: String = "") {
    POPULAR("Popular"),
    UPCOMING("Upcoming"),
    TOP_RATED("Top rated"),
    THRILLER("Thriller","53"),
    CRIME("Crime","80"),
    ACTION("Action","28"),
    HORROR("Horror","27"),
    DRAMA("Drama","18"),
    FANTASY("Fantasy","14"),
    COMEDY("Comedy","35"),
    FAMILY("Family","10751"),
    WAR("War","10752"),
    WESTERN("Western","37")


}