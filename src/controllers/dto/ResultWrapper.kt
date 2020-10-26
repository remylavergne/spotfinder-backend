package dev.remylavergne.spotfinder.controllers.dto

import com.squareup.moshi.JsonClass
import dev.remylavergne.spotfinder.utils.MoshiHelper

@JsonClass(generateAdapter = true)
data class ResultWrapper<T>(
    val statusCode: Int,
    val time: Long = System.currentTimeMillis(),
    val result: T,
    val pagination: Pagination?
) {
    inline fun <reified T> toJson(type: Class<T>): String {
        // return MoshiHelper.wrapperToJson(this, type)
        return ""
    }
}