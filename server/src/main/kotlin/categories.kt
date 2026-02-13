package com.github.bjhham.prtbay

import io.ktor.server.application.Application
import io.ktor.server.config.property
import kotlinx.serialization.Serializable

/**
 * Read categories from config.
 */
fun Application.categories(): List<Category> =
    property("categories")

@Serializable
data class Category(
    val name: String,
    val pirateBayId: Int?,
    val qBittorrentName: String?,
)
