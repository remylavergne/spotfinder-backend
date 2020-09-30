package dev.remylavergne.spotfinder.utils

inline fun <reified T> List<T>.toJson(): String {
    val adapter = MoshiHelper.getListAdapter<T>()
    return adapter.toJson(this)
}