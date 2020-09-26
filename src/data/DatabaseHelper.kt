package dev.remylavergne.spotfinder.data

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.data.models.Spot

interface DatabaseHelper {
    fun persistPicture(picture: Picture)
    fun persistSpot(spot: Spot): Boolean
}
