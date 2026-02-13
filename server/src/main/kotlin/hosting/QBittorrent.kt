package com.github.bjhham.prtbay.hosting

import com.github.bjhham.prtbay.Category
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.config.property
import kotlinx.serialization.Serializable

fun Application.qBittorrent(categories: List<Category>): TorrentHost =
    property<QBittorrent>("qb").withCategories(categories)

@Serializable
data class QBittorrent(
    val url: String,
    val username: String,
    val password: String,
    val categories: Map<String, String> = emptyMap()
): TorrentHost {
    private val httpClient by lazy {
        HttpClient(CIO) {
            install(HttpCookies)
            install(DefaultRequest) {
                headers.append(HttpHeaders.UserAgent, "ParrotBay")
            }
        }
    }

    fun withCategories(categories: List<Category>) =
        copy(categories = categories.associate { it.name to it.qBittorrentName!! })

    override suspend fun addTorrent(category: String, link: String) {
        login() // ensure we're authenticated
        val response = httpClient.submitFormWithBinaryData("$url/api/v2/torrents/add", formData {
            append("urls", link)
            append("category", categories[category] ?: category) // default to input string if not found
        })
        if (!response.status.isSuccess()) {
            throw IllegalArgumentException("Adding torrent failed: [${response.status}] ${response.bodyAsText()}")
        }
    }

    private suspend fun QBittorrent.login() {
        if (httpClient.cookies(url).any { it.name == "SID" }) return

        val response = httpClient.submitForm("$url/api/v2/auth/login", parameters {
            append("username", username)
            append("password", password)
        })
        if (!response.status.isSuccess()) {
            throw IllegalArgumentException("Login failed: [${response.status}] ${response.bodyAsText()}")
        }
        require(httpClient.cookies(url).any { it.name == "SID" }) {
            "Login failed: no SID cookie"
        }
    }
}
