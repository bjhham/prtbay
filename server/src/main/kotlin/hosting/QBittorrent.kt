package com.github.bjhham.hosting

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlin.collections.emptyList

@Serializable
data class QBittorrent(
    val url: String,
    val username: String,
    val password: String,
): TorrentHost {
    private val httpClient by lazy {
        HttpClient(CIO) {
            install(HttpCookies)
            install(DefaultRequest) {
                headers.append(HttpHeaders.UserAgent, "ParrotBay")
            }
        }
    }

    override suspend fun addTorrent(category: String, link: String) {
        login() // ensure we're authenticated
        val response = httpClient.submitFormWithBinaryData("$url/api/v2/torrents/add", formData {
            append("urls", link)
            append("category", category)
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