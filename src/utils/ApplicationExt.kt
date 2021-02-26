package dev.remylavergne.spotfinder.utils

import io.ktor.application.*
import io.ktor.request.*

suspend inline fun <reified T> ApplicationCall.getResponseObject(): T? {
    return try {
        MoshiHelper.fromJson<T>(this.receiveText())
    } catch (e: Exception) {
        println(e.localizedMessage)
        null
    }
}
