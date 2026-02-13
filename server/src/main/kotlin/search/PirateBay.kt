package com.github.bjhham.prtbay.search

import com.fleeksoft.ksoup.Ksoup
import com.github.bjhham.prtbay.Category
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.config.property
import kotlinx.html.Entities
import kotlinx.serialization.Serializable

fun Application.pirateBay(categories: List<Category>): TorrentSource =
    property<PirateBay>("source").withCategories(categories)

@Serializable
data class PirateBay(
    val url: String,
    val categories: Map<String, Int> = emptyMap()
): TorrentSource {
    private val httpClient by lazy { HttpClient(CIO) }

    fun withCategories(categories: List<Category>) =
        copy(categories = categories.associate { it.name to it.pirateBayId!! })

    override suspend fun fetchResults(
        category: String?,
        search: String?
    ): List<TorrentResult>? {
        val categoryId = when(category) {
            "TV" -> 205
            "Movies" -> 201
            // everything else
            else -> 0
        }
        return search?.let {
            val queryString = parameters {
                append("q", search)
                append("category", categoryId.toString())
                append("page", "0")
                append("orderby", "99")
            }.formUrlEncode()

            val html = httpClient
                .get("$url/s/?$queryString")
                .bodyAsText()

            PirateBayParser.parse(html)
        }
    }

}

val PirateBayParser = HtmlParser { html: String ->
    val doc = Ksoup.parse(html = html)
    doc.select("#searchResult > tbody > tr").mapNotNull { row ->
        try {
            val cells = row.childElementsList().takeIf { it.size >= 4 } ?: return@mapNotNull null
            val titleEl = row.select("a.detLink")
            val magnetEl = row.select("""a[href^="magnet:?"]""")
            val (seedersCell, leechersCell) = cells.subList(2, 4)

            TorrentResult(
                title = titleEl.text(),
                link = magnetEl.attr("href"),
                seeders = seedersCell.text().toUIntOrNull() ?: 0u,
                leechers = leechersCell.text().toUIntOrNull() ?: 0u,
            )
        } catch (_: Exception) {
            // TODO log
            null
        }
    }
}
