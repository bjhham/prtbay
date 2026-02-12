package com.github.bjhham.prtbay.search

import kotlinx.serialization.Serializable

fun interface HtmlParser {
    fun parse(html: String): List<TorrentResult>
}

@Serializable
data class TorrentResult(
    val title: String,
    val link: String,
    val seeders: UInt,
    val leechers: UInt,
)
