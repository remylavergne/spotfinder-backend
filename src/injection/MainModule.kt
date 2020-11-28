package dev.remylavergne.spotfinder.injection

import dev.remylavergne.spotfinder.data.DatabaseHelper
import dev.remylavergne.spotfinder.data.DatabaseHelperImpl
import dev.remylavergne.spotfinder.repositories.*
import dev.remylavergne.spotfinder.services.*
import io.ktor.util.*
import org.koin.dsl.module

@KtorExperimentalAPI
val mainModule = module(createdAtStart = true) {
    // Services
    single<PicturesService> { PicturesServiceImpl(get(), get()) }
    single<SpotsService> { SpotsServiceImpl(get()) }
    single<UserService> { UserServiceImpl(get()) }
    single<SearchService> { SearchServiceImpl(get()) }
    single<CommentsService> { CommentsServiceImpl(get()) }
    // Repositories
    single<PicturesRepository> { PicturesRepositoryImpl(get()) }
    single<SpotsRepository> { SpotsRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<CommentsRepository> { CommentsRepositoryImpl(get()) }
    // Database
    single<DatabaseHelper> { DatabaseHelperImpl() }
}
