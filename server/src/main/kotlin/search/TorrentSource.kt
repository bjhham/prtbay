package com.github.bjhham.prtbay.search

fun interface TorrentSource {
    suspend fun fetchResults(category: String?, search: String?): List<TorrentResult>?
}
