package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.data.models.Picture
import dev.remylavergne.spotfinder.services.PicturesService
import dev.remylavergne.spotfinder.utils.MoshiHelper
import dev.remylavergne.spotfinder.utils.toJson
import io.ktor.application.call
import io.ktor.http.*
import io.ktor.request.receiveMultipart
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.get
import org.koin.ktor.ext.get
import org.litote.kmongo.json
import java.io.File
import java.util.*

fun Route.picturesController(uploadDir: File) {

    val pictureService: PicturesService = get()

    post("/upload/picture") {
        val response = pictureService.savePicture(call.receiveMultipart())
        call.respondText(text = response, status = HttpStatusCode.OK, contentType = ContentType.Text.Plain)
    }

    get("/pictures/spot/{id}") {
        call.parameters["id"]?.let { spotId ->
            val picturesJson = pictureService.getPicturesBySpotId(spotId)
            // Send
            call.respondText(
                contentType = ContentType.Application.Json,
                text = picturesJson,
                status = HttpStatusCode.OK
            )
        } ?: call.respondText(
            text = "Error, id missing",
            status = HttpStatusCode.NotFound,
            contentType = ContentType.Text.Plain
        )
    }
}
