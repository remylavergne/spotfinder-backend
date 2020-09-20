package dev.remylavergne.spotfinder.injection

import dev.remylavergne.spotfinder.services.PicturesService
import dev.remylavergne.spotfinder.services.PicturesServiceImpl
import org.koin.dsl.module

val mainModule = module {
    single<PicturesService> { PicturesServiceImpl() }
}