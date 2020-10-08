package dev.remylavergne.spotfinder.services

import dev.remylavergne.spotfinder.controllers.dto.SpotCreationDto
import dev.remylavergne.spotfinder.data.models.Spot
import dev.remylavergne.spotfinder.repositories.SpotsRepository
import dev.remylavergne.spotfinder.utils.MoshiHelper
import dev.remylavergne.spotfinder.utils.toJson

class SpotsServiceImpl(private val spotsRepository: SpotsRepository) : SpotsService {

    override fun createNewSpot(dto: SpotCreationDto): String {
        // Create a real spot object
        val spot = Spot.fromCreationDto(dto)
        // Save spot into database
        val result = spotsRepository.persistSpot(spot)
        return if (result) { // TODO: Sealed class for Service State status
            "Success"
        } else {
            "Error"
        }
    }

    override fun getSpots(): String {
        return spotsRepository.getSpots().toJson()
    }

    override fun getSpotById(id: String): String? {
        val spot = spotsRepository.getSpotById(id)
        return MoshiHelper.toJson(spot)
    }

    override fun getSpotsByRider(id: String): String {
        return spotsRepository.getSpotsByRider(id).toJson()
    }

    override fun getSpotsByCountry(country: String): String {
        TODO("Not yet implemented")
    }
}
