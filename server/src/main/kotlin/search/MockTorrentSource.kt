package com.github.bjhham.prtbay.search

import kotlinx.serialization.json.Json

val MockTorrentSource = TorrentSource { _, _ ->
    val sampleJson = Thread.currentThread()
        .contextClassLoader.getResourceAsStream("sample.json")
        ?.readBytes() ?: error("Sample JSON not found")
    Json.decodeFromString(sampleJson.decodeToString())
}
