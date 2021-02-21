package dev.remylavergne.spotfinder

import dev.remylavergne.spotfinder.controllers.*
import dev.remylavergne.spotfinder.data.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlin.time.ExperimentalTime

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@ExperimentalTime
@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    JWTTool.init(environment)
    loadModules()
    FileHelper.getUploadDir(environment)
    DatabaseProvider.initialize(this)
    SpotLocationUpdater.init()
    EmailManager.initialize(this)

    routing {
        defaultController()
        usersController()
        authenticate {
            spotsController()
            picturesController()
            searchController()
            commentsController()
            likesController()
        }
        metricsController()
    }
}