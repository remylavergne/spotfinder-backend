package dev.remylavergne.spotfinder

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlin.time.ExperimentalTime

class ApplicationTest {
    @ExperimentalTime
    @KtorExperimentalAPI
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }
}
