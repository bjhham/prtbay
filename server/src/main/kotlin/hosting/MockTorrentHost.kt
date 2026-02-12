package com.github.bjhham.prtbay.hosting

import kotlinx.coroutines.delay

object MockTorrentHost : TorrentHost {
    override suspend fun addTorrent(category: String, link: String) {
        delay(3000L)
        println("Added torrent ${link.take(10)}... to $category")
    }
}
