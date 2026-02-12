package com.github.bjhham.prtbay

import com.github.bjhham.prtbay.hosting.MockTorrentHost
import com.github.bjhham.prtbay.hosting.QBittorrent
import com.github.bjhham.prtbay.hosting.TorrentHost
import com.github.bjhham.prtbay.search.MockTorrentSource
import com.github.bjhham.prtbay.search.PirateBay
import com.github.bjhham.prtbay.search.TorrentSource
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

@Suppress("unused")
fun Application.configureRouting() {
    val testMode = propertyOrNull<Boolean>("testMode") == true
    val source: TorrentSource =
        if (testMode) MockTorrentSource
        else property<PirateBay>("source")
    val qBittorrent: TorrentHost =
        if (testMode) MockTorrentHost
        else property<QBittorrent>("qb")

    routing {
        staticResources("/", "/web")

        get("/") {
            val search = call.parameters["search"]
            val categoryString = call.parameters["category"]
            val results = source.fetchResults(categoryString, search)

            call.respondHtml {
                searchPage(
                    search.orEmpty(),
                    categoryString ?: "TV",
                    results
                )
            }
        }

        post("/torrents/{category}") {
            val category = call.parameters["category"]!!
            val magnetLink = call.receiveParameters().getOrFail("link")
            qBittorrent.addTorrent(category, magnetLink)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
