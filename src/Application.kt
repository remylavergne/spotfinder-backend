package dev.remylavergne.spotfinder

import dev.remylavergne.spotfinder.controllers.picturesController
import dev.remylavergne.spotfinder.controllers.spotsController
import dev.remylavergne.spotfinder.data.DatabaseProvider
import dev.remylavergne.spotfinder.injection.mainModule
import dev.remylavergne.spotfinder.injection.toolsModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.ApplicationEnvironment
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.Koin
import org.slf4j.event.Level
import java.io.File
import java.text.DateFormat

// TODO: Get and check environment variable at startup

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    installPlugins(this)
    DatabaseProvider.initialize(this)
    val uploadDir = getUploadDir(environment)

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        spotsController()
        picturesController(uploadDir)

        authenticate("myBasicAuth") {
            get("/protected/route/basic") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }
    }

}

fun installPlugins(app: Application) {
    app.install(Koin) {
        modules(mainModule, toolsModule)
    }

    app.install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    app.install(AutoHeadResponse)

    app.install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    app.install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    app.install(Authentication) {
        basic("myBasicAuth") {
            realm = "Ktor Server"
            validate { if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
    }

    app.install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
}

/**
 * Create a directory for uploads
 * Configuration located in application.conf
 */
@KtorExperimentalAPI
fun getUploadDir(environment: ApplicationEnvironment): File {
    val picturesConfig = environment.config.config("pictures")

    val uploadDirPath: String = picturesConfig.property("upload.dir").getString()
    val uploadDir = File(uploadDirPath)

    if (!uploadDir.exists()) {
        uploadDir.mkdirs()
    }

    return uploadDir
}


