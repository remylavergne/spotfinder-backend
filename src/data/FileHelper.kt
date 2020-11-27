package dev.remylavergne.spotfinder.data

import io.ktor.application.*
import io.ktor.util.*
import java.io.File

object FileHelper {
    lateinit var uploadDir: File
        private set

    /**
     * Create a directory for uploads
     * Configuration located in application.conf
     */
    @KtorExperimentalAPI
    fun getUploadDir(environment: ApplicationEnvironment): File {
        val picturesConfig = environment.config.config("pictures")

        val uploadDirPath: String = picturesConfig.property("upload.dir").getString()
        uploadDir = File(uploadDirPath)

        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }

        return uploadDir
    }

    fun getOrCreateUploadDir(absolutePath: String): File {
        val dir = File(uploadDir, absolutePath)

        if (!dir.exists()) {
            dir.mkdirs()
        }

        return dir
    }
}