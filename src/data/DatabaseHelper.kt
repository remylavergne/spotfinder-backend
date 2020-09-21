package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture

interface DatabaseHelper {
    fun persistPicture(picture: Picture)
}