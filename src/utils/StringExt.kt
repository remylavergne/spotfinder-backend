package dev.remylavergne.spotfinder.utils

fun String.removeExtension(): String {
    val pathParts = this.split(".")
    return if (pathParts.size > 1) {
        pathParts[0]
    } else {
        this
    }
}