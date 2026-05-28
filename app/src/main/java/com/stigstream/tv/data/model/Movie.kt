package com.stigstream.tv.data.model

import com.google.gson.annotations.SerializedName

// ── Movie / Show card model ──────────────────────────────────────────────────

data class Movie(
    @SerializedName("id")          val id: String,
    @SerializedName("title")       val title: String,
    @SerializedName("poster")      val posterUrl: String,
    @SerializedName("backdrop")    val backdropUrl: String = "",
    @SerializedName("year")        val year: String = "",
    @SerializedName("rating")      val rating: String = "",
    @SerializedName("genre")       val genre: String = "",
    @SerializedName("duration")    val duration: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("stream_url")  val streamUrl: String = "",
    @SerializedName("type")        val type: ContentType = ContentType.MOVIE,
    @SerializedName("quality")     val quality: String = "HD"
)

enum class ContentType { MOVIE, TV_SHOW }

// ── Content row for home screen ─────────────────────────────────────────────

data class ContentRow(
    val id: String,
    val title: String,
    val items: List<Movie>
)

// ── API response wrapper ─────────────────────────────────────────────────────

data class ApiResponse<T>(
    @SerializedName("status")  val status: String,
    @SerializedName("data")    val data: T?,
    @SerializedName("message") val message: String = ""
)

data class MoviesResponse(
    @SerializedName("movies") val movies: List<Movie> = emptyList(),
    @SerializedName("total")  val total: Int = 0,
    @SerializedName("page")   val page: Int = 1
)

// ── Search result ────────────────────────────────────────────────────────────

data class SearchResult(
    val query: String,
    val results: List<Movie>
)
