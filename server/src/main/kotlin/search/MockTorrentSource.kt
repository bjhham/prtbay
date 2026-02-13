package com.github.bjhham.prtbay.search

import kotlinx.serialization.json.Json

class MockTorrentSource: TorrentSource {
    override suspend fun fetchResults(
        category: String?,
        search: String?
    ): List<TorrentResult>? {
        val sampleJson = Thread.currentThread()
            .contextClassLoader.getResourceAsStream("sample.json")
            ?.readBytes() ?: error("Sample JSON not found")
        return Json.decodeFromString(sampleJson.decodeToString())
    }
}
