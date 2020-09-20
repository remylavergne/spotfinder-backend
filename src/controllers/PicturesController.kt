package dev.remylavergne.spotfinder.controllers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream

fun Route.picturesController() {

    post("/upload/picture") {
        val multipart = call.receiveMultipart()
        val informations = mutableMapOf<String, String>()

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
                            "dev-test",
                            "${informations["spotId"]}-${part.originalFileName}-${System.currentTimeMillis()}.$ext"
                        )


                        var scope: Job
                        part.streamProvider().use { its ->
                            file.outputStream().buffered().use { test ->
                                scope = CoroutineScope(Dispatchers.IO + Job()).launch {
                                    its.copyToSuspend(test)
                                }
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

/**
 * Utility boilerplate method that suspending,
 * copies a [this] [InputStream] into an [out] [OutputStream] in a separate thread.
 *
 * [bufferSize] and [yieldSize] allows to control how and when the suspending is performed.
 * The [dispatcher] allows to specify where will be this executed (for example a specific thread pool).
 */
suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}