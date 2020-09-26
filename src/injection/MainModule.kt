package dev.remylavergne.spotfinder.injection

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.DatabaseHelperImpl
import dev.remylavergne.spotfinder.repositories.PicturesRepository
import dev.remylavergne.spotfinder.repositories.PicturesRepositoryImpl
import dev.remylavergne.spotfinder.repositories.SpotsRepository
import dev.remylavergne.spotfinder.repositories.SpotsRepositoryImpl
import dev.remylavergne.spotfinder.services.PicturesService
import dev.remylavergne.spotfinder.services.PicturesServiceImpl
import dev.remylavergne.spotfinder.services.SpotsService
import dev.remylavergne.spotfinder.services.SpotsServiceImpl
import org.koin.dsl.module

val mainModule = module(createdAtStart = true) {
    // Services
    single<PicturesService> { PicturesServiceImpl(get()) }
    single<SpotsService> { SpotsServiceImpl(get()) }
    // Repositories
    single<PicturesRepository> { PicturesRepositoryImpl(get()) }
    single<SpotsRepository> { SpotsRepositoryImpl(get()) }
    // Database
    single<DatabaseHelper> { DatabaseHelperImpl() }
}
