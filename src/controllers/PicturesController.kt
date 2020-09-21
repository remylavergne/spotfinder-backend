package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.services.PicturesService
import io.ktor.application.call
import io.ktor.http.*
import io.ktor.request.receiveMultipart
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.get
import java.io.File

fun Route.picturesController(uploadDir: File) {

    val pictureService: PicturesService = get()

    post("/upload/picture") {
        val response = pictureService.savePicture(call.receiveMultipart())
        call.respondText(text = response, status = HttpStatusCode.OK, contentType = ContentType.Text.Plain)
    }
}
