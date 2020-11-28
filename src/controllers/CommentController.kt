package dev.remylavergne.spotfinder.controllers

import dev.remylavergne.spotfinder.controllers.dto.CreateCommentDto
import dev.remylavergne.spotfinder.controllers.dto.SearchCommentsDto
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

    get("/comment/{id}/comments") {
        val id = call.parameters["id"]
        val page = call.request.queryParameters["page"]?.toInt()
        val limit = call.request.queryParameters["limit"]?.toInt()

        if (page == null || limit == null) {
            throw Exception("missing pagination infos") // TODO: New Exception for this
        }

        val response = commentsService.getComments(SearchCommentsDto(commentId = id), page, limit)

        call.respondText(
            contentType = ContentType.Application.Json,
            text = response,
            status = HttpStatusCode.OK
        )
    }
}