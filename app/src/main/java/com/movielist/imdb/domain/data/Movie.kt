package com.movielist.imdb.domain.data

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@Entity(
    tableName = "movie"
)
data class Movie(
    @ColumnInfo(defaultValue = "")
    @SerializedName("originalTitle")
    var originalTitle: String? = null,
    @ColumnInfo(defaultValue = "")
    @SerializedName("overview")
    var overview: String? = null,
    @ColumnInfo(defaultValue = "")
    @SerializedName("popularity")
    var popularity: Double? = null,
    @ColumnInfo(defaultValue = "")
    @SerializedName("posterPath")
    var posterPath: String? = null,
    @ColumnInfo(defaultValue = "")
    @SerializedName("releaseDate")
    var releaseDate: String? = null,
    @ColumnInfo(defaultValue = "")
    @SerializedName("title")
    var title: String? = null,
    @ColumnInfo(defaultValue = "")
    @SerializedName("voteAverage")
    var voteAverage: Double? = null,
    @ColumnInfo(defaultValue = "")
    @SerializedName("voteCount")
    var voteCount: Int? = null
) : Serializable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0
}