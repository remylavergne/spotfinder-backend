package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.services.CommentsService
import dev.remylavergne.spotfinder.utils.getResponseObject
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.commentsController() {

    val commentsService: CommentsService = get()

    post("/comment/create") {
        call.getResponseObject<CreateCommentDto>()?.let { data: CreateCommentDto ->
            val commentJson = commentsService.createComment(data)

            if (commentJson != null) {
                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = commentJson,
                    status = HttpStatusCode.OK
                )
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } ?: call.respond(HttpStatusCode.InternalServerError)
    }

}