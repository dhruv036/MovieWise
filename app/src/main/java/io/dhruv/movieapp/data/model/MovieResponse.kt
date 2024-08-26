package io.dhruv.movieapp.data.model

import com.google.gson.annotations.SerializedName
import io.dhruv.movieapp.utils.Genre
import kotlinx.serialization.Serializable

data class MovieResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") var results: List<MovieDetails> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
)

@Serializable
data class MovieDetails(
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("media_type") var mediaType: String? = null,
    @SerializedName("adult") var adult: Boolean? = null,
    @SerializedName("original_language") var originalLanguage: String? = null,
    @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    @SerializedName("popularity") var popularity: Double? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("video") var video: Boolean? = null,
    @SerializedName("vote_average") var voteAverage: Double? = null,
    @SerializedName("vote_count") var voteCount: Int? = null
){
    fun doesMatchSearchQuery(query: String): Boolean{
        val matchingCombinations = listOf(
            "$title",
            "$originalTitle",
            genreIds.joinToString { Genre.fromId(it)?.displayName ?: "" }
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}