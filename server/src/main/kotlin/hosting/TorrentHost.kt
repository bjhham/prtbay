package com.github.bjhham.prtbay.hosting

fun interface TorrentHost {
    suspend fun addTorrent(category: String, link: String)
}
