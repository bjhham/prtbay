package com.github.bjhham.prtbay

import com.github.bjhham.prtbay.hosting.TorrentHost
import com.github.bjhham.prtbay.search.TorrentSource
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

@Suppress("unused")
fun Application.configureRouting(
    categories: List<Category>,
    source: TorrentSource,
    qBittorrent: TorrentHost
) {
    routing {
        staticResources("/", "/web")

        get("/") {
            val search = call.parameters["search"]
            val categoryString = call.parameters["category"]
            val results = source.fetchResults(categoryString, search)

            call.respondHtml {
                searchPage(
                    categories,
                    search.orEmpty(),
                    categoryString ?: "All",
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
