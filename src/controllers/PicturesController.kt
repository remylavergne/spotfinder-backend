package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.SearchCommentsDto
import dev.remylavergne.spotfinder.services.CommentsService
import dev.remylavergne.spotfinder.services.PicturesService
import dev.remylavergne.spotfinder.utils.exceptions.MissingQueryParams
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
    val commentsService: CommentsService = get()

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

    get("/picture/{id}/comments") {
        val id = call.parameters["id"]
        val page = call.request.queryParameters["page"]?.toInt()
        val limit = call.request.queryParameters["limit"]?.toInt()

        if (page == null || limit == null) {
            throw Exception("missing pagination infos") // TODO: New Exception for this
        }

        val response = commentsService.getComments(SearchCommentsDto(pictureId = id), page, limit)

        call.respondText(
            contentType = ContentType.Application.Json,
            text = response,
            status = HttpStatusCode.OK
        )
    }
}
