package com.sacramento.data.db.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sacramento.data.db.tables.movie_tables.GenreEntity
import com.sacramento.data.db.tables.movie_tables.MovieGenreCrossRef
import com.sacramento.data.db.tables.movie_tables.MovieEntity

data class MovieWithGenresDBModel(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genres: List<GenreEntity>
)