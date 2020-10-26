package dev.remylavergne.spotfinder

import dev.remylavergne.spotfinder.controllers.defaultController
import dev.remylavergne.spotfinder.controllers.metricsController
import dev.remylavergne.spotfinder.controllers.picturesController
import dev.remylavergne.spotfinder.controllers.spotsController
import dev.remylavergne.spotfinder.data.DatabaseProvider
import dev.remylavergne.spotfinder.data.FileHelper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.auth.UserIdPrincipal
import io.ktor.response.respondText
import io.ktor.routing.routing
import io.ktor.routing.get
import io.ktor.util.KtorExperimentalAPI

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    loadModules()
    FileHelper.getUploadDir(environment)
    DatabaseProvider.initialize(this)

    routing {
        defaultController()
        spotsController()
        picturesController()
        metricsController()
        // Authentication
        authenticate("myBasicAuth") {
            get("/protected/route/basic") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }
    }
}