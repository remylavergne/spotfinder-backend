package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.SpotCreationDto

interface SpotsService {
    fun createNewSpot(dto: SpotCreationDto): String
}
