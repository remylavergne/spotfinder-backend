package dev.remylavergne.spotfinder.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiHelper {

    val moshi: Moshi

    init {
        moshi = getMoshiInstance()
    }

    private fun getMoshiInstance(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // API

    inline fun <reified T> getAdapter(): JsonAdapter<T> {
        return moshi.adapter(T::class.java)
    }

    inline fun <reified T> fromJson(json: String): T? {
        return getAdapter<T>().fromJson(json)
    }
}