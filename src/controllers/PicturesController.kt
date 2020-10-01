package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.services.PicturesService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.request.receiveMultipart
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.get
import org.koin.ktor.ext.get
import java.io.File

fun Route.picturesController() {

    val pictureService: PicturesService = get()

    static("/pictures") {
        /*files("css")
        default("index.html")*/
        resources("pictures")
    }

    post("/upload/picture") {
        pictureService.savePicture(call.receiveMultipart()).let { response ->
            call.respondText(text = response, status = HttpStatusCode.OK, contentType = ContentType.Text.Plain)
        }
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

    /**
     * Expose Pictures Static Content
     */
    get("/picture/id/{pictureId}/") {
        pictureService.getStaticContentPicture(call)?.let { file: File ->
            call.respondFile(file)
        } ?: call.respondText(
            text = "Error, picture not found",
            status = HttpStatusCode.NotFound,
            contentType = ContentType.Text.Plain
        )
    }
}
