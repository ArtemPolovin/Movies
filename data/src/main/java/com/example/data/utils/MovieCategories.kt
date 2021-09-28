package com.example.data.utils

sealed class MovieCategories{
    object Popular{val category: String = "Popular"}
    object Upcoming{val category: String = "Upcoming"}
    object TopRated{val category: String = "Top rated"}

}
