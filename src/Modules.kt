package dev.remylavergne.spotfinder

import dev.remylavergne.spotfinder.injection.mainModule
import dev.remylavergne.spotfinder.injection.toolsModule
import io.ktor.application.*
import io.ktor.auth.*
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
import io.ktor.features.Compression
import io.ktor.features.AutoHeadResponse
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.gzip
import io.ktor.features.deflate
import io.ktor.features.minimumSize
import io.ktor.features.ContentNegotiation
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.request.path

fun Application.loadModules() {
    install(Koin) {
        modules(mainModule, toolsModule)
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

    install(AutoHeadResponse)

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(Authentication) {
        basic("myBasicAuth") {
            realm = "Ktor Server"
            validate { if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
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