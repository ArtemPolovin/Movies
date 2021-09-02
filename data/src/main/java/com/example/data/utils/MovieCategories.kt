package com.example.data.utils

sealed class MovieCategories{
    object Latest{val category: String = "Latest"}
    object Upcoming{val category: String = "Upcoming"}
    object TopRated{val category: String = "Top rated"}

}
