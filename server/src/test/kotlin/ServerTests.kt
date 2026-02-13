package com.github.bjhham.prtbay

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ServerTests {

    @Test
    fun testSearch() = testApplication {
        configure("test.conf")

        val html = client.get("?search=test").bodyAsText()
        val rowCount = Regex("<tr>").findAll(html).count()
        assertEquals(30, rowCount)
    }

}
