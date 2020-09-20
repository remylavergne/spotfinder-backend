package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.services.PicturesService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get
import java.io.File

fun Route.picturesController(uploadDir: File) {

    val pictureService: PicturesService = get()

    post("/upload/picture") {
        val multipart = call.receiveMultipart()
        val informations = mutableMapOf<String, String>()

        pictureService.savePicture()

        // Processes each part of the multipart input content of the user
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "spotId" -> informations["spotId"] = part.value
                        else -> throw Exception("Unknown part value")
                    }
                }
                is PartData.FileItem -> {
                    try {
                        val ext = File(part.originalFileName ?: "no_name").extension

                        val file = File(
                            uploadDir,
                            "${informations["spotId"]}-${part.originalFileName}-${System.currentTimeMillis()}.$ext"
                        )

                        part.streamProvider().use { its ->
                            file.outputStream().buffered().use { test ->
                                its.copyTo(test)
                            }
                        }
                    } catch (e: Exception) {
                        println()
                    }
                }
                else -> throw Exception("Else branch")
            }

            part.dispose()
        }

        call.respond(HttpStatusCode.OK, "Image uploaded")
    }
}
