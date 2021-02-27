package dev.remylavergne.spotfinder

import dev.remylavergne.spotfinder.data.JWTTool
import dev.remylavergne.spotfinder.injection.mainModule
import dev.remylavergne.spotfinder.injection.toolsModule
import dev.remylavergne.spotfinder.utils.exceptions.MissingQueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import org.slf4j.event.Level
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.request.path
import io.ktor.response.*
import io.ktor.serialization.*
import io.ktor.util.*

@KtorExperimentalAPI
fun Application.loadModules() {
    install(Koin) {
        modules(mainModule, toolsModule)
    }

    install(CORS) {
        method(HttpMethod.Options)
        header(HttpHeaders.Origin)
        header(HttpHeaders.AccessControlAllowOrigin)
        anyHost()
        // allowCredentials = true
        allowSameOrigin = true
        allowCredentials = true
        allowNonSimpleContentTypes = true
        maxAgeInSeconds = 3600
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    install(StatusPages) {
        exception<MissingQueryParams> { cause ->
            call.respond(HttpStatusCode.NotAcceptable, cause.message)
            throw cause
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
        header("App", "SpotFinder")
    }

    install(Authentication) {
        jwt {
            realm = JWTTool.jwtRealm
            verifier(JWTTool.makeJwtVerifier(JWTTool.jwtIssuer, JWTTool.jwtAudience))
            validate { credential ->
                if (credential.payload.audience.contains(JWTTool.jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }

    install(ContentNegotiation) {
    }

    install(MicrometerMetrics) {
        registry = get<PrometheusMeterRegistry>()
        meterBinders = listOf(
            ClassLoaderMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics(),
            JvmThreadMetrics(),
            FileDescriptorMetrics(),
            UptimeMetrics()
        )
    }
}