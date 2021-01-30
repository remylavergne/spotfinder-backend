package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.LikeDto
import dev.remylavergne.spotfinder.services.LikesService
import dev.remylavergne.spotfinder.utils.getResponseObject
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.likesController() {

    val likesService: LikesService = get()

    post("/like/update-state") {
        call.getResponseObject<LikeDto>()?.let { data ->
            val response = likesService.updateLikeState(data)

            call.respondText(
                contentType = ContentType.Application.Json,
                text = response,
                status = HttpStatusCode.OK
            )
        }
    }

    post("/like/statistics") {
        call.getResponseObject<LikeDto>()?.let { data ->
            val response = likesService.updateLikeState(data)

            call.respondText(
                contentType = ContentType.Application.Json,
                text = response,
                status = HttpStatusCode.OK
            )
        }
    }

}