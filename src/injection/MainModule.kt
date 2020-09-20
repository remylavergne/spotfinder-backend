package dev.remylavergne.spotfinder.injection

import dev.remylavergne.spotfinder.repositories.PicturesRepository
import dev.remylavergne.spotfinder.repositories.PicturesRepositoryImpl
import dev.remylavergne.spotfinder.services.PicturesService
import dev.remylavergne.spotfinder.services.PicturesServiceImpl
import org.koin.dsl.module

val mainModule = module(createdAtStart = true) {
    single<PicturesService> { PicturesServiceImpl(get()) }
    single<PicturesRepository> { PicturesRepositoryImpl() }
}