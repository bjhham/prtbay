package com.github.bjhham.search

fun interface TorrentSource {
    suspend fun fetchResults(category: String?, search: String?): List<TorrentResult>?
}