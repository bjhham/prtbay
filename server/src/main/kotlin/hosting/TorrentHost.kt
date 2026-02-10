package com.github.bjhham.hosting

fun interface TorrentHost {
    suspend fun addTorrent(category: String, link: String)
}