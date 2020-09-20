package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.services.PicturesService
import io.ktor.application.call
import io.ktor.request.receiveMultipart
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.get
import java.io.File

fun Route.picturesController(uploadDir: File) {

    val pictureService: PicturesService = get()

    post("/upload/picture") { pictureService.savePicture(call.receiveMultipart()) }
}
