package com.github.bjhham

import com.github.bjhham.hosting.QBittorrent
import com.github.bjhham.search.PirateBay
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.*
import io.ktor.server.util.getOrFail

fun Application.configureRouting() {
    val testMode: Boolean = property("testMode")
    val source: PirateBay = property("source")
    val qBittorrent: QBittorrent = property("qb")

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

        post("/download/{category}") {
            val category = call.parameters["category"]!!
            val magnetLink = call.receiveParameters().getOrFail("link")
            qBittorrent.addTorrent(category, magnetLink)
            call.respondRedirect("/") // TODO
        }
    }
}