package dev.remylavergne.spotfinder.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeolocationInfos(
    val place_id: Long?,
    val licence: String?,
    val osm_type: String?,
    val osm_id: Long?,
    val lat: Double?,
    val lon: Double?,
    val place_rank: Int?,
    val category: String?,
    val type: String?,
    val importance: Double?,
    val addresstype: String?,
    val name: String?,
    val display_name: String?
)