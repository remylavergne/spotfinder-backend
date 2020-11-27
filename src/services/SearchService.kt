package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.SearchDto

interface SearchService {
    fun searchSpots(data: SearchDto): String
}