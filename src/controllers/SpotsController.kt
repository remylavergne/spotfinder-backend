package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.SpotCreationDto
import dev.remylavergne.spotfinder.services.PicturesService
import dev.remylavergne.spotfinder.services.SpotsService
import dev.remylavergne.spotfinder.utils.exceptions.MissingQueryParams
import dev.remylavergne.spotfinder.utils.getResponseObject
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.spotsController() {

    val spotsService: SpotsService = get()
    val pictureService: PicturesService = get()

    post("/spot/create") {
        call.getResponseObject<SpotCreationDto>()?.let {
            // Receive new spot informations
            val spot = spotsService.createNewSpot(it)

            return@post if (spot != null) {
                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = spot,
                    status = HttpStatusCode.OK
                )
            } else {
                call.respondText(
                    text = "Error during Spot creation",
                    status = HttpStatusCode.NotFound,
                    contentType = ContentType.Text.Plain
                )
            }
        }
    }

    get("/spot/all") {
        val jsonSpots = spotsService.getSpots()
        call.respondText(
            contentType = ContentType.Application.Json,
            text = jsonSpots,
            status = HttpStatusCode.OK
        )
    }

    get("/spots") {
        val queryParams: Parameters = call.request.queryParameters
        val response = spotsService.getLatestPaginatedSpots(queryParams)
        call.respondText(
            contentType = ContentType.Application.Json,
            text = response,
            status = HttpStatusCode.OK
        )
    }

    get("/spot/id/{id}") {
        call.parameters["id"]?.let { spotId: String ->
            spotsService.getSpotById(spotId)?.let { jsonSpot: String ->
                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = jsonSpot,
                    status = HttpStatusCode.OK
                )
            } ?: call.respondText(
                text = "Error, spot doesn't exist",
                status = HttpStatusCode.NotFound,
                contentType = ContentType.Text.Plain
            )
        } ?: call.respondText(
            text = "Error, id missing",
            status = HttpStatusCode.NotFound,
            contentType = ContentType.Text.Plain
        )
    }

    // todo: Move to user controller
    get("/spot/rider/{id}") {
        call.parameters["id"]?.let { riderId: String ->
            val jsonSpots = spotsService.getSpotsByRider(riderId)
            call.respondText(
                contentType = ContentType.Application.Json,
                text = jsonSpots,
                status = HttpStatusCode.OK
            )
        } ?: call.respondText(
            text = "Error, id missing",
            status = HttpStatusCode.NotFound,
            contentType = ContentType.Text.Plain
        )
    }

    get("/spot/{id}/pictures") {
        val spotID = call.parameters["id"]
        val page = call.request.queryParameters["page"]?.toInt()
        val limit = call.request.queryParameters["limit"]?.toInt()
        if (page == null || limit == null || spotID == null) {
            throw MissingQueryParams("QueryParams 'page' and 'limit' are mandatory")
        }

        val response = pictureService.getPaginatedPicturesBySpot(spotID, page, limit)

        call.respondText(
            contentType = ContentType.Application.Json,
            text = response,
            status = HttpStatusCode.OK
        )
    }
}
