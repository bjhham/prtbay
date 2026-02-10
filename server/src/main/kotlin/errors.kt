package com.github.bjhham

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.html.respondHtml
import io.ktor.server.plugins.statuspages.StatusPages
import kotlinx.html.*

fun Application.errorHandling() {
    install(StatusPages) {
        exception<Exception> { call, cause ->
            call.respondHtml {
                head {
                    title("Parrot Bay \uD83E\uDD9C\uD83C\uDFF4")
                    link(rel = "stylesheet", href = "/style.css")
                }
                body {
                    h1 { +"Error" }
                    cause::class.simpleName?.let {
                        h3 { +it }
                    }
                    cause.message?.let {
                        p { +it }
                    }
                    cause.stackTraceToString().let {
                        pre { +it }
                    }
                }
            }
        }
    }
}